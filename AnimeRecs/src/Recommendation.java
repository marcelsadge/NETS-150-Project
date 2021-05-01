import java.io.IOException;
import java.util.*;

public class Recommendation {
	
	private Profile user; // the user we are making recommendations for 
	
	// maps each anime to the number of times it was recommended
	private HashMap<String, Integer> animeRecommendedFrequency; 
	
	// sorted list of anime from most recommended to least recommended
	private ArrayList<String> sortedAnimes;
	
	// sorted list of anime recommendations based off of user standards
	private ArrayList<String> sortedRecs;
	
	private AnimePageMap animePageMap;
	
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
		animeRecommendedFrequency = new HashMap<String, Integer>();
		
		for (String query: user.getAnimesWatched()) {
			animePageMap.addAnime(query);
		}

		List<AnimePage> baseAnimeList = new ArrayList<AnimePage>();
		baseAnimeList.addAll(animePageMap.getAnimePages());
		for (AnimePage ap : baseAnimeList) {
			try {
				ap.setRecommendedAnimeToFrequencyMap();
				Map<AnimePage, Integer> numberRecsMap = ap.getRecommendedAnimeToFrequencyMap();

				for (AnimePage recommended : numberRecsMap.keySet()) {
					int scoreToAdd = numberRecsMap.get(recommended);
					String animeName = recommended.getName();

					if (animeRecommendedFrequency.containsKey(animeName)) {
						int currScore = animeRecommendedFrequency.get(animeName);
						animeRecommendedFrequency.put(animeName, currScore + scoreToAdd);
					} else {
						animeRecommendedFrequency.put(animeName, scoreToAdd);
					}
				}
				
			} catch (IOException e) {
				System.out.println("Failed to get recommendations for: " 
						+ ap.getName());
			}
		}
		
		sortRecommendations();
	}
	
	/**
	 * Helper method that sorts the animes recommended based on the number of time it was 
	 * recommended, and then filter them based on the user's standards
	 */
	private void sortRecommendations() {
		Set<String> animeRecs = animeRecommendedFrequency.keySet();
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
			if (animeRecommendedFrequency.get(anime) >= animeRecommendedFrequency.get(curr)) {
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
		AnimePage animePage = animePageMap.getByName(anime);
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
		return new HashMap<String, Integer>(animeRecommendedFrequency);
	}
	
	/**
	 * Gets the sorted list of recommendations to make to the user
	 * @return List of the sorted recommendations
	 */
	public List<String> getRecs() {
		return new LinkedList<String>(sortedRecs);
	}

}
