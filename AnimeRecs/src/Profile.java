import java.util.HashMap;

public class Profile {
	
	private HashMap<String, Integer> animeList;
	
	public Profile() {
		animeList = new HashMap<String, Integer>();
	}
	
	public void addAnime (String animeName, int score) {
		if (score < 0 || score > 10) {
			System.out.println("invalid score");
			return;
		}
		
		animeList.put(animeName, score);
	}
	
	

}
