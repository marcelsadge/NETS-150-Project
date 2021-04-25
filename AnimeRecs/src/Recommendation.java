import java.io.IOException;
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
		Set<Genre> genres = user.getGenrePrefs();
		
		
		try {
			for (String anime : animes) {
				AnimePage curr = new AnimePage(anime);
				List<Genre> animeGenres = curr.getGenreList();
				if (genreMatch(animeGenres, genres)) {
					curr.setRecommendedAnimeToFrequencyMap();
					Map<AnimePage, Integer> scores = curr.getRecommendedAnimeToFrequencyMap();
				}
			}
		} catch (IOException e) {
			System.out.println("failed to get frequency map");
		}
		
	}
	
	private boolean genreMatch(List<Genre> animeGenres, Set<Genre> prefGenres) {
		for (Genre genre : prefGenres) {
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
