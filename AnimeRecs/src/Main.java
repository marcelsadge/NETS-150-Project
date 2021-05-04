import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

public class Main extends JFrame {
	
	static ArrayList<Profile> users;
	static Graph graph;
	
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
	
	Main(List<String> path) {
		JList<String> displayList = new JList<>(path.toArray(new String[0]));
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
			
			name = JOptionPane.showInputDialog("What is your name? (Type 'end' to exit the "
					+ "program. Type 'map' to see map of users)");
			
			if (name.equals("end")) {
				System.exit(0);
			}
			
			if (name.equals("map")) {
				createMapOfUsers();
				break;
			}
			
			Profile profile = new Profile(name);
			
			String animeInput, numInput, minScore, oldInput, genreNumInput, genrePref;
			int numberOfGenres, numberOfAnimes, numOfEpisodes, oldAnime = 0;
			double minRating;
			
			numInput = JOptionPane.showInputDialog("Maximum number of episodes that you"
					+ " are willing to watch? \n(Please Enter an Integer)");
			try {
				numOfEpisodes = Integer.parseInt(numInput);
			} catch (Exception e) {
				throw new IllegalArgumentException("Not a valid integer");
			}
			profile.setMaxEpisodes(numOfEpisodes);
			
			minScore = JOptionPane.showInputDialog("Lowest rating for anime you want to watch?"
					+ " \n(Please Enter an Integer)");
			try {
				minRating = Double.parseDouble(minScore);
			} catch (Exception e) {
				throw new IllegalArgumentException("Not a valid integer");
			}
			profile.setMinScore(minRating);
			
			oldInput = JOptionPane.showInputDialog("Oldest year for anime you want to watch?"
					+ " \n(Please Enter an Integer)");
			try {
				oldAnime = Integer.parseInt(oldInput);
			} catch (Exception e) {
				throw new IllegalArgumentException("Not a valid integer");
			}
			profile.setOldestAnime(oldAnime);
			
			genreNumInput = JOptionPane.showInputDialog("Number of genres you want to watch?"
					+ " \n(Please Enter an Integer)");
			try {
				numberOfGenres = Integer.parseInt(genreNumInput);
			} catch (Exception e) {
				throw new IllegalArgumentException("Not a valid integer");
			}

			for (int i = 0; i < numberOfGenres; i++) {
				genrePref = JOptionPane.showInputDialog("Genre preference "
						+ (i+1) + " for anime you want to watch?"
						+ "\n\n(Valid genres are:"
						+ "\nAction, Adventure, Cars, Comedy,"
						+ "\nDementia, Demons, Drama, Ecchi,"
						+ "\nFantasy, Game, Harem, Hentai,"
						+ "\nHistorical, Horror, Josei, Kids,"
						+ "\nMagic, Martial Arts, Mecha, Military,"
						+ "\nMusic, Mystery, Parody, Police,"
						+ "\nPsychological, Romance, Samurai, School,"
						+ "\nSciFi, Seinen, Shoujo, ShoujoAi,"
						+ "\nShounen, ShounenAi, SliceOfLife,"
						+ "\nSpace, Sports, SuperPower, Supernatural,"
						+ "\nThriller, Vampire, Yaoi, Yuri");
				try {
					profile.addGenrePref(genrePref);
				} catch (Exception e) {
					System.out.println("Invalid genre");
				}
			}
			
			animeInput = JOptionPane.showInputDialog("How many animes have you watched?"
					+ "\n(Please Enter an Integer)");
			try {
				numberOfAnimes = Integer.parseInt(animeInput);
			} catch (Exception e) {
				throw new IllegalArgumentException("Not a valid integer");
			}
			
			for (int i = 0; i < numberOfAnimes; i++) {
				String anime, animeScore;
				int score;
				
				anime = JOptionPane.showInputDialog("Name of anime number " + (i + 1)
						+ "Note: the name should be between 3 and 100 characters long");
				animeScore = JOptionPane.showInputDialog("Your rating for anime number " + 
				(i + 1) + "\n(Please Enter an Integer)");
				
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
		while (true) {
			String nodeA;
			String nodeB;
			
			nodeA = JOptionPane.showInputDialog("Starting node?");
			nodeB = JOptionPane.showInputDialog("Ending node?");
			
			if (graph.getNode(nodeA) == null) {
				throw new ElementNotFoundException("Person A doesn't exist :(");
			}
			
			if (graph.getNode(nodeB) == null) {
				throw new ElementNotFoundException("Person B doesn't exist :(");
			}
			
			List<String> path = findShortestContacts(nodeA, nodeB);
			
			new Main(path);
		}
	}
	
	/**
     * Constructor a social network map between users and anime
     */
	public static void createMapOfUsers() {
		System.setProperty("org.graphstream.ui", "swing");
		graph = new MultiGraph("Anime User Map");
		graph.setAttribute("ui.antialias", true);
		graph.setAttribute("ui.stylesheet", 
				"node {fill-mode: dyn-plain; size-mode: dyn-size; text-size: 20;} "
				+ "edge {fill-color:black;}");
		graph.display();
		
		SpriteManager sman = new SpriteManager(graph);
		
		for (Profile user : users) {
			Node a = graph.addNode(user.getUsername());
			a.setAttribute("ui.text-size", 100);
			a.setAttribute("ui.size", 20);
			a.setAttribute("ui.color", Color.LIGHT_GRAY);
			a.setAttribute(user.getUsername(), user);
			
			Set<String> animes = user.getAnimesWatched();
			
			for (String anime : animes) {
				if (graph.getNode(anime) == null) {
					Node b = graph.addNode(anime);
					b.setAttribute("ui.color", Color.RED);
				}
			}
			
			for (String anime : animes) {
				graph.addEdge(getRandomString(), user.getUsername(), 
						anime).setAttribute("length", 1);
			}
			
		}
		
		graph.nodes().forEach(n -> n.setAttribute("label", n.getId()));
	}
	
	public static List<String> findShortestContacts(String x, String y) {
		List<String> list = new ArrayList<>();
		
		Dijkstra dijk = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		
		dijk.init(graph);
		dijk.setSource(graph.getNode(x));
		dijk.compute();
		
		Path path = dijk.getPath(graph.getNode(y));
		List<Node> nodePath = path.getNodePath();
		
		for (Node a : nodePath) {
			String b = a.getId();
			if (!a.getAttribute("ui.color").equals(Color.RED)) {
				list.add(b);
			}
		}
		
		dijk.clear();
		
		return list;
	}
	
	/**
     * Creates a random string of length 18, used to create node Ids
     */
	protected static String getRandomString() {
        String random = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder builder = new StringBuilder();
        Random rnd = new Random();
        while (builder.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * random.length());
            builder.append(random.charAt(index));
        }
        String saltStr = builder.toString();
        return saltStr;

    }
	
}
