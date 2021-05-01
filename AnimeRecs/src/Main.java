import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.*;

public class Main extends JFrame {
	
	static ArrayList<Profile> users;
	
	Main(Recommendation userRec) {
		userRec.updateRecommendations();
		List<String> list = userRec.getRecs();
		
		JList<String> displayList = new JList<>(list.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(displayList);
		
		getContentPane().add(scrollPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
	}
	
	public static void main(String[] args) {
		users = new ArrayList<>();
		while (true) {
			String name;
			
			name = JOptionPane.showInputDialog("What is your name?");
			
			if (name.equals("end")) {
				break;
			}
			
			if (name.equals("map")) {
				createMapOfUsers();
				continue;
			}
			
			Profile profile = new Profile(name);
			
			String animeInput;
			int numberOfAnimes = 0;
		
			animeInput = JOptionPane.showInputDialog("How many animes have you watched?");
			try {
				numberOfAnimes = Integer.parseInt(animeInput);
			} catch (Exception e) {
				throw new IllegalArgumentException("Not a valid integer");
			}
			
			for (int i = 0; i < numberOfAnimes; i++) {
				String anime, animeScore;
				int score;
				
				anime = JOptionPane.showInputDialog("Name of anime number: " + i);
				animeScore = JOptionPane.showInputDialog("Your rating for anime number: " + i);
				
				try {
					score = Integer.parseInt(animeScore);
				} catch (Exception e) {
					throw new IllegalArgumentException("Not a valid integer");
				}
				profile.addAnime(anime, score);
			}
			users.add(profile);
			Recommendation userRec = new Recommendation(profile);
			new Main(userRec);
		}
	}
	
	/*
	public static void createMapOfUsers() {
		System.setProperty("org.graphstream.ui", "swing");
		Graph graph = new MultiGraph("Anime User Map");
		graph.setAttribute("ui.antialias", true);
		graph.setAttribute("ui.stylesheet", "node {fill-color: red; size-mode: dyn-size;} edge {fill-color:black;}");
		
		
		
		
		graph.display();
		Profile user = new Profile("Bob");
		Scanner scanner = new Scanner(System.in);
		user.addAnime("bleach", 10);
	}
	*/
}
