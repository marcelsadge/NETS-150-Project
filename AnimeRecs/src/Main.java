import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Profile user = new Profile("Bob");
		user.addAnime("One_Piece", 10);
		user.addAnime("Bleach", 10);
		
		Recommendation userRec = new Recommendation(user);
		
		userRec.updateRecommendations();
		System.out.println(userRec.getRecs());

	}

}
