import java.io.IOException;
import java.util.*;

public class Recommendation {
	
	private Profile user; // the user we are making recommendations for 
	
	// maps each anime to the number of times it was recommended
	private HashMap<String, Integer> animeRecFreq; 
	
	// sorted list of anime from most recommended to least recommended
	private ArrayList<String> sortedAnimes;
	
	// sorted list of anime recommendations based off of user standards
	private ArrayList<String> sortedRecs;
	
	private AnimePageMap animePageMap;
	
	private Set<String> animesWatched;
	
	/**
	 * Constructor for a Recommendation
	 * @param user The user we are recommending anime to
	 */
	public Recommendation(Profile user) {
		this.user = user;

		animePageMap = AnimePageMap.getInstance();

		updateRecommendations();
	}
	
	/**
	 *  Method that updates the user's recommendations
	 */
	public void updateRecommendations() {
		
		// create a map to store the anime and it's corresponding score
		animeRecFreq = new HashMap<String, Integer>();
		
		Set<String> animes = user.getAnimesWatched();
		
		animesWatched = new HashSet<String>();
		
		for (String anime: animes) {
			updateScores(anime);
		}
		
		sortRecommendations();
	}
	
	/**
	 * Method that updates the scores for all the recommendations we receive from one anime
	 * @param anime The anime that the user gets recommendations from
	 */
	private void updateScores(String anime) {
		try {
			AnimePage animePage = new AnimePage(anime);
			
			animePageMap.put(animePage);
			
			animePage.setRecommendedAnimeToFrequencyMap();

			animesWatched.add(animePage.getName());
			
			Map<String, Integer> numberRecsMap = animePage.getRecommendedAnimeToFrequencyMap();

			Set<String> animeRecs = numberRecsMap.keySet();

			for (String animeName : animeRecs) {
				int scoreToAdd = numberRecsMap.get(animeName);
				if (animeRecFreq.containsKey(animeName)) {
					int currScore = animeRecFreq.get(animeName);
					animeRecFreq.put(animeName, currScore + scoreToAdd);
				} else {
					animeRecFreq.put(animeName, scoreToAdd);
				}
			}
		} catch (IOException e) {
			System.out.println("failed to get recommendations for: " + anime);
		}
	}
	
	/**
	 * Helper method that sorts the animes recommended based on the number of time it was 
	 * recommended, and then filter them based on the user's standards
	 */
	private void sortRecommendations() {
		Set<String> animeRecs = animeRecFreq.keySet();
		sortedAnimes = new ArrayList<String>();
		sortedRecs = new ArrayList<String>();
		for (String anime : animeRecs) {
			// place the anime in list if user has not watched it yet
			if (!animesWatched.contains(anime)) {
				placeInSortedList(anime);
			}
		}
		
		// get the top 3 recommended animes that also meet user standards
		for (String anime : sortedAnimes) {	
			if (sortedRecs.size() == 3) {
				break;
			}
			
			if (matchUserStandards(anime)) {
				sortedRecs.add(anime);
			}
		}
	}
	
	
	/**
	 * Helper method that places an anime into a sorted list of animes based on frequency of recs
	 * @param anime The anime to place in the list
	 */
	private void placeInSortedList(String anime) {
		if (sortedAnimes.isEmpty()) {
			sortedAnimes.add(anime);
			return;
		}
		for (int i = 0; i < sortedAnimes.size(); i++) {
			String curr = sortedAnimes.get(i);
			if (animeRecFreq.get(anime) >= animeRecFreq.get(curr)) {
				sortedAnimes.add(i, anime);
				return;
			}
		}
		
		// if anime was never added into the array, add it to the end
		sortedAnimes.add(anime);
	}
	
	/**
	 * Helper method that checks whether an AnimePage meets the user's specific requirements
	 * @param anime The anime we check the standards of
	 * @return boolean Whether or not the anime matched the user's standards
	 */
	private boolean matchUserStandards(String anime) {
		AnimePage animePage;
		if (animePageMap.containsName(anime)) {
			animePage = animePageMap.getByName(anime);
		} else {
			animePage = new AnimePage(anime);
			animePageMap.put(animePage);
		}
		
		if (animePage.getEpisodes() > user.getMaxEpisodes()) {
			return false;
		} else if (animePage.getScore() < user.getMinScore()) {
			return false;
		} else if (animePage.getReleasedYear() < user.getOldestAnimeYear()) {
			return false;
		} else if (!genreMatch(animePage)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Helper function that checks whether the animePage satisfied all the user's
	 * @param animePage The AnimePage of which we check the genres of
	 * @return boolean Whether or not the genres matched
	 */
	private boolean genreMatch(AnimePage animePage) {
		Set<Genre> prefGenres = user.getGenrePrefs();
		List<Genre> animeGenres = animePage.getGenreList();
		for (Genre genre : prefGenres) {
			if (!animeGenres.contains(genre)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Gets the Map of the anime to their respective scores
	 * @return Map of Anime to RecommendedFrequency
	 */
	public Map<String, Integer> getAnimeScore() {
		return new HashMap<String, Integer>(animeRecFreq);
	}
	
	/**
	 * Gets the sorted list of recommendations to make to the user
	 * @return List of the sorted recommendations
	 */
	public List<String> getRecs() {
		return new LinkedList<String>(sortedRecs);
	}
	
	public static void main(String[] args) {
		Profile user = new Profile("Bob");
		user.addAnime("One Piece");
		user.addAnime("Bleach");
//		user.addAnime("Fairy Tail TV");
//		user.addAnime("Darling in the Franxx");
//		user.addAnime("Pokemon");
//		user.addAnime("Dragon Ball");
//		user.addAnime("Hunter x Hunter");
//		user.addAnime("Soul Eater");
//		user.addAnime("Black Clover");
//		user.addAnime("Boku no Hero Academia");
//		user.addAnime("Ao no Exorcist");
//		user.addAnime("One Punch Man");
//		user.addAnime("Nanatsu no Taizai");
//		user.addAnime("Noragami");
//		user.addAnime("Mob Psycho 100");
//		user.addAnime("Saiki Kusuo No");
//		user.addAnime("Kimetsu no Yaiba");
//		user.addAnime("Jujutsu Kaisen (TV)");
//		user.addAnime("Ansatsu Kyoushitsu");
//		user.addAnime("Shingeki no Kyojin");
		user.setMaxEpisodes(200);
		user.setMinScore(8.0);
		user.setOldestAnime(2015);
		user.addGenrePref("Action");
		System.out.println(user.getGenrePrefs());

		Recommendation userRec = new Recommendation(user);
		userRec.updateRecommendations();
		System.out.println(userRec.getAnimeScore());
		System.out.println(userRec.getRecs());
		System.out.println("number of pages stored:" + AnimePageMap.getInstance().size());
	}

}
