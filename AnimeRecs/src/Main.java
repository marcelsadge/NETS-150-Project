import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Profile user = new Profile("Bob");
		user.addAnime("bleach", 10);
		
		Recommendation userRec = new Recommendation(user);
		
//		userRec.updateRecommendations();
		System.out.println(userRec.getRecs());
		System.out.println(userRec.getAnimeScore());

	}

}
