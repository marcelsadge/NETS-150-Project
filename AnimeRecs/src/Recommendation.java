import java.io.IOException;
import java.util.*;

public class Recommendation {
	
	private Profile user;
	private HashMap<String, Integer> animeScores;
	
	private ArrayList<String> sortedAnimes;
	private ArrayList<String> recs;
	
	
	// Constructor for the recommendation class
	public Recommendation(Profile user) {
		this.user = user;
		updateRecommendations();
	}
	
	/**
	 *  Method that updates the user's recommendations
	 */
	public void updateRecommendations() {
		animeScores = new HashMap<String, Integer>();
		recs = new ArrayList<String>();
		Set<String> animes = user.getAnimesWatched();
		for (String anime : animes) {
			updateScores(anime);
		}
		sortRecommendations();
	}
	
	/**
	 * 
	 * @param anime
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
	
	private void sortRecommendations() {
		Set<String> animeRecs = animeScores.keySet();
		sortedAnimes = new ArrayList<String>();
		for (String anime : animeRecs) {
			if (!sortedAnimes.contains(anime)) {
				placeInSortedList(anime);
			} 	
		}
	}
	
	private void placeInSortedList(String anime) {
		if (sortedAnimes.isEmpty()) {
			sortedAnimes.add(anime + animeScores.get(anime));
			return;
		}
		for (int i = 0; i < sortedAnimes.size(); i++) {
			String curr = sortedAnimes.get(i);
			if (animeScores.get(anime) >= animeScores.get(curr)) {
				sortedAnimes.add(i, anime + animeScores.get(anime));
				return;
			}
		}
		
		// if anime was never added into the array, add it to the end
		sortedAnimes.add(anime);
	}
	

//	private boolean genreMatch(List<Genre> animeGenres, Set<Genre> prefGenres) {
//		for (Genre genre : prefGenres) {
//			if (!animeGenres.contains(genre)) {
//				return false;
//			}
//		}
//		
//		return true;
//	}
	
	public Map<String, Integer> getAnimeScore() {
		System.out.println(sortedAnimes);
		return new HashMap<String, Integer>(animeScores);
	}
	
	public List<String> getRecs() {
		return new LinkedList<String>(recs);
	}
	

}
