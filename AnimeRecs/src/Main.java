import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Profile user = new Profile("Bob");
		Scanner scanner = new Scanner(System.in);
		user.addAnime("bleach", 10);
		
		String tempStr = null;
		int tempScore = 0;
		boolean isAddAnime = true;
		boolean isAddScore = true;

		while (isAddAnime) {
			System.out.println("Please enter the name of the anime. " +
					"(Enter Q to go to the next step)");
			tempStr = scanner.nextLine();
			if (tempStr.equals("Q")) {
				isAddAnime = false;
				isAddScore = false;
			} else {
				isAddScore = true;
			}

			while (isAddScore) {

				System.out.println("Please enter the score of the anime");
				try {
					isAddScore = false;
					tempScore = scanner.nextInt();
					scanner.nextLine();
					user.addAnime(tempStr, tempScore);
				} catch (InputMismatchException e) {
					System.out.println("Invalid number. Please Try Again.");
					scanner.nextLine();
				}
				
			}

		}

		scanner.close();

		Recommendation userRec = new Recommendation(user);
		
//		userRec.updateRecommendations();
		System.out.println(userRec.getRecs());
		System.out.println(userRec.getAnimeScore());

		
	}

}
