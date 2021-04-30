import java.io.IOException;
import java.util.*;

public class Recommendation {
	
	private Profile user;
	private HashMap<String, Integer> animeScores;
	private List<String> recsInOrder;
	
	
	// Constructor for the recommendation class
	public Recommendation(Profile user) {
		this.user = user;
		updateRecommendations();
	}
	
	/**
	 *  Method for updating the user's recommendations
	 */
	public void updateRecommendations() {
		Set<String> animes = user.getAnimesWatched();
		for (String anime : animes) {
			updateScores(anime);
		}
		
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
				int scoreToAdd = animeScores.get(ap);
				String animeName = ap.getName().toLowerCase();
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
	
//	private boolean genreMatch(List<Genre> animeGenres, Set<Genre> prefGenres) {
//		for (Genre genre : prefGenres) {
//			if (!animeGenres.contains(genre)) {
//				return false;
//			}
//		}
//		
//		return true;
//	}
	
	public List<String> getRecs() {
		return new LinkedList<String>(recsInOrder);
	}
	

}
