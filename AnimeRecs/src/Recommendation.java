import java.util.*;

public class Recommendation {
	
	private Profile user;
	private HashMap<String, Integer> animeToScore;
	private List<String> recsInOrder;
	
	public Recommendation(Profile user) {
		this.user = user;
	}
	
	public void updateRecommendations() {
		Set<String> animes = user.getAnimesWatched();
		Set<String> genres = user.getGenrePrefs();
		
//		for (String anime : animes) {
//			AnimePage curr = new AnimePage(anime);
//			curr.setGenreList();
//			List<String> animeGenres = curr.getGenreList();
//			if (genreMatch(animeGenres, genres) {
//				curr.setRecommendedAnimeToFrequencyMap();
//				Map<AnimePage, Integer> scores = curr.getRecommendedAnimeToFrequencyMap();
//			}
//		}
	}
	
	private boolean genreMatch(List<String> animeGenres, Set<String> prefGenres) {
		for (String genre : prefGenres) {
			if (!animeGenres.contains(genre)) {
				return false;
			}
		}
		
		return true;
	}
	
	public List<String> getRecs() {
		return new LinkedList<String>(recsInOrder);
	}
	
	public static void main(String[] args) {
		
	}

}
