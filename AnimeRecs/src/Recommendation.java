import java.io.IOException;
import java.util.*;

public class Recommendation {
	
	private Profile user; // the user we are making recommendations for 
	
	// maps each anime to the number of times it was recommended
	private HashMap<String, Integer> animeScores; 
	
	// sorted list of anime from most recommended to least recommended
	private ArrayList<String> sortedAnimes;
	
	// sorted list of anime recommendations based off of user standards
	private ArrayList<String> sortedRecs;
	
	
	
	/**
	 * Constructor for a Recommendation
	 * @param user The user we are recommending anime to
	 */
	public Recommendation(Profile user) {
		this.user = user;
		updateRecommendations();
	}
	
	/**
	 *  Method that updates the user's recommendations
	 */
	public void updateRecommendations() {
		
		// create a map to store the anime and it's corresponding score
		animeScores = new HashMap<String, Integer>();
		Set<String> animes = user.getAnimesWatched();
		// get recommendations from each anime that the user has watched
		for (String anime : animes) {
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
			animePage.setRecommendedAnimeToFrequencyMap();
			Map<AnimePage, Integer> numberRecsMap = animePage.getRecommendedAnimeToFrequencyMap();
			Set<AnimePage> animeRecs = numberRecsMap.keySet();
			
			for (AnimePage ap : animeRecs) {
				int scoreToAdd = numberRecsMap.get(ap);
				String animeName = ap.getName();
				if (animeScores.containsKey(animeName)) {
					int currScore = animeScores.get(animeName);
					animeScores.put(animeName, currScore + scoreToAdd);
				} else {
					animeScores.put(animeName, scoreToAdd);
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
		Set<String> animeRecs = animeScores.keySet();
		Set<String> animesWatched = user.getAnimesWatched();
		sortedAnimes = new ArrayList<String>();
		sortedRecs = new ArrayList<String>();
		for (String anime : animeRecs) {
			// place the anime in list if user has not watched it yet
			if (!animesWatched.contains(anime)) {
				placeInSortedList(anime);
			}
		}
		
		// get the top 5 recommended animes that also meet user standards
		for (String anime : sortedAnimes) {	
			if (sortedRecs.size() == 10) {
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
			if (animeScores.get(anime) >= animeScores.get(curr)) {
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
		AnimePage animePage = new AnimePage(anime);
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
		return new HashMap<String, Integer>(animeScores);
	}
	
	/**
	 * Gets the sorted list of recommendations to make to the user
	 * @return List of the sorted recommendations
	 */
	public List<String> getRecs() {
		return new LinkedList<String>(sortedRecs);
	}

}
