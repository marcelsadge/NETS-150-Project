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
		
	}
	
	public List<String> getRecs() {
		return new LinkedList<String>(recsInOrder);
	}
	
	public static void main(String[] args) {
		
	}

}
