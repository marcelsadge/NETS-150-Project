import java.util.HashSet;
import java.util.Set;

public class Profile {
	
	private String username;
	private HashSet<String> animes;
	private HashSet<Genre> genrePrefs;
	private double minScore;
	private int maxEpisodes;
	private int oldestYear;
	
	
	public Profile(String username) {
		this.username = username;
		animes = new HashSet<String>();
		genrePrefs = new HashSet<Genre>();
		maxEpisodes = Integer.MAX_VALUE;
		minScore = Integer.MIN_VALUE;
	}
	
	/**
	 * Stores the anime and corresponding score to the hashMap
	 * @param animeName The Name of the anime that the User watched
	 */
	public void addAnime (String animeName, int score) {
		animes.add(animeName);
	}
	
	/**
	 * Stores the anime and corresponding score to the hashMap
	 * @param animeName The Name of the anime that the User watched
	 * @param score The rating that the user gives to the anime 1-10
	 */
	public void addAnime (String animeName) {
		animes.add(animeName);
	}
	

	/**
	 * remove an anime from the Users list of watched animes
	 * @param animeName The anime to be removed
	 */
	public void removeAnime(String animeName) {
		animeName = animeName.replaceAll(" ", "_").toLowerCase();
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
	 * Set the minimum score for an anime the user would watch
	 * @param score The minimum score for an anime that the user would watch
	 */
	public void setMinScore(double score) {
		if (score < 0) {
			score = 0;
		} else if (score > 10) {
			score = 10;
		} else {
			this.minScore = score;
		}
		
	}
	
	
	/**
	 * Set the maximum anime length that the user would watch
	 * @param max The maximum number of episodes user is willing to watch
	 */
	public void setMaxEpisodes(int max) {
		if (max <= 0) {
			this.maxEpisodes = 1;
		} else {
			this.maxEpisodes = max;
		}
	}
	
	/**
	 * Set the oldest release year the user would watch
	 * @param year The oldest release year for an anime
	 */
	public void setOldestAnime(int year) {
		if (year > 2021) {
			this.oldestYear = 2021;
		} else {
			this.oldestYear = year;
		}
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
		return new HashSet<String>(animes);
	}
	
	
	/**
	 * gets the username of the user
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Gets the minimum score that the user prefers for animes
	 * @return The minimum score for the animes to recommend
	 */
	public double getMinScore() {
		return minScore;
	}
	
	/**
	 * Gets the maximum number of episodes the user is willing to watch for an anime
	 * @return The maximum number of episodes for a recommended anime
	 */
	public int getMaxEpisodes() {
		return this.maxEpisodes;
	}
	
	/**
	 * Gets the oldest year of an anime that the user is willing to watch
	 * @return The oldest release year for the anime
	 */
	public int getOldestAnimeYear() {
		return this.oldestYear;
	}
	
}
