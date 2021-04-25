import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Profile {
	
	private String username;
	
	private HashMap<String, Integer> animes;
	
	private HashSet<Genre> genrePrefs;
	
	public Profile(String username) {
		this.username = username;
		animes = new HashMap<String, Integer>();
		genrePrefs = new HashSet<Genre>();
	}
	
	/**
	 * Stores the anime and corresponding score to the hashMap
	 * @param animeName The Name of the anime that the User watched
	 * @param score The rating that the user gives to the anime 1-10
	 */
	public void addAnime (String animeName, int score) {
		if (score < 0 || score > 10) {
			System.out.println("invalid score");
			return;
		}
		
		animes.put(animeName, score);
	}
	

	/**
	 * remove an anime from the Users list of watched animes
	 * @param animeName The anime to be removed
	 */
	public void removeAnime(String animeName) {
		animes.remove(animeName);
	}
	
	
	/**
	 * Add a genre preference for the user
	 * @param genre The genre the User prefers to watch
	 */
	public void addGenrePref(String genre) {
		genrePrefs.add(Genre.fromString(genre));
	}
	
	/**
	 * remove a genre preference in the set
	 * @param genre The genre preference to be removed
	 */
	public void removeGenrePref(String genre) {
		if (genrePrefs.contains(Genre.fromString(genre))) {
			genrePrefs.remove(Genre.fromString(genre));
		}
		return;
	}
	
	/**
	 * Get the user's genre preferences
	 * @return Set of genre preferences
	 */
	public Set<Genre> getGenrePrefs() {
		return new HashSet<Genre> (genrePrefs);
	}
	
	/**
	 * get the user's animes watched
	 * @return Set of animes watched by the user
	 */
	public Set<String> getAnimesWatched() {
		return new HashSet<String>(animes.keySet());
	}
	
	/**
	 * get all the animes watched with their corresponding scores
	 * @return Map containing animes with their corresponding scores
	 */
	public Map<String, Integer> getAnimeScores() {
		return new HashMap<String, Integer> (animes);
	}
	
	public String getUsername() {
		return username;
	}
	
	
	

}
