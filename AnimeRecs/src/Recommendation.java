import java.io.IOException;
import java.util.*;

public class Recommendation {
	
	private Profile user;
	private HashMap<String, Integer> animeScores;
	private List<String> recsInOrder;
	
	public Recommendation(Profile user) {
		this.user = user;
	}
	
	public void updateRecommendations() {
		Set<String> animes = user.getAnimesWatched();
		Set<Genre> genres = user.getGenrePrefs();
		
		for (String anime : animes) {
			AnimePage animePage = new AnimePage(anime);
			List<Genre> animeGenres = animePage.getGenreList();
			if (genreMatch(animeGenres, genres)) {
				updateScores(animePage, anime);
			}
			
		}
		
	}
	
	private void updateScores(AnimePage animePage, String anime) {
		try {
			animePage.setRecommendedAnimeToFrequencyMap();
			Map<AnimePage, Integer> scores = animePage.getRecommendedAnimeToFrequencyMap();
			int scoreToAdd = scores.get(animePage);
			if (animeScores.containsKey(anime)) {
				int currScore = animeScores.get(anime);
				animeScores.put(anime, currScore + scoreToAdd);
			} else {
				animeScores.put(anime, scoreToAdd);
			}
		} catch (IOException e) {
			System.out.println("failed to get recommendations for: " + anime);
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
