import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Random;
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

public class Main extends JFrame {
	
	// List of users inputted into program
	static ArrayList<Profile> users;
	
	// Graph of all the users and anime
	static Graph graph;
	
	// Adjacency List of anime nodes to user nodes
	static HashMap<Node, List<String>> animeAdjList;
	
	// Counter for delay
	static int count = 0;
	
	/**
	 * Constructor for Main - Prints out recommendations
	 * @param userRec the recommendation class of the user
	 */
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
	
	/**
	 * Constructor for Main - Prints out shortest path
	 * @param path the path list
	 */
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
			} else if (name.equals("map")) {
				createMapOfUsers();
				while (true) {
					Scanner sc = new Scanner(System.in);
					System.out.println("To find shortest path between two users " +
					"\n(Please enter 'Path')" +
							"\nTo find all triadic closures " +
					"\n(Please enter 'Closure')" + 
							"\nPlease enter 'Return' to return home");
					
					String input = sc.nextLine();
					
					if (input.equals("Path")) {
						//Code for Dijkstra
						
						String nodeA;
						String nodeB;
						
						nodeA = JOptionPane.showInputDialog("Starting node?");
						nodeB = JOptionPane.showInputDialog("Ending node?");
						
						if (graph.getNode(nodeA) == null) {
							sc.close();
							throw new ElementNotFoundException("Person A doesn't exist :(");
						}
						
						if (graph.getNode(nodeB) == null) {
							sc.close();
							throw new ElementNotFoundException("Person B doesn't exist :(");
						}
						
						List<String> path = findShortestContacts(nodeA, nodeB);
						sc.close();
						new Main(path);
					} else if (input.equals("Closure")) {
						// Code for Triadic Closure
						animeAdjList = new HashMap<>();
						
						for (Node node : graph) {
							if (node.getAttribute("ui.color").equals(Color.RED)) {
								animeAdjList.put(node, new ArrayList<>());
							}
						}
						
						for (Node n : graph) {
							if (n.getAttribute("ui.color").equals(Color.RED)) {
								for (Node n2 : graph) {
									if (!n2.getAttribute("ui.color").equals(Color.RED)) {
										if (n.hasEdgeBetween(n2)) {
											List<String> animes = animeAdjList.get(n);
											animes.add(n2.getId());
											animeAdjList.replace(n, animes);
										}
									}
								}
								
							}
						}
						
						for (Entry<Node, List<String>> entry : animeAdjList.entrySet()) {
							List<String> listOfUsers = entry.getValue();
							
							for (Iterator<String> iter = listOfUsers.iterator(); iter.hasNext();) {
								String curr = iter.next();
								for (Iterator<String> iter2 = listOfUsers.iterator(); iter2.hasNext();) {
									String curr2 = iter2.next();
									if (!graph.getNode(curr).hasEdgeBetween(curr2) && 
											!graph.getNode(curr2).hasEdgeBetween(curr) &&
											!curr.equals(curr2)) {
										graph.addEdge(getRandomString(), curr, curr2)
										.setAttribute("ui.style", "fill-color: blue; ");;
									}
								}
							}
							sc.close();
						}
					} else if (input.equals("Return")) {
						sc.close();
						break;
					}
				}
			} else {
				Profile profile = null;
				boolean alreadyVisited = false;
				
				for (Iterator<Profile> iter = users.iterator(); iter.hasNext();) {
					Profile user = iter.next();
					if (name.equals(user.getUsername())) {
						alreadyVisited = true;
						profile = user;
						break;
					}
				}
				if (!alreadyVisited) {
					profile = new Profile(name);
				}
				
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
							+ "\nThriller, Vampire, Yaoi, Yuri)");
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
							+ " Note: the name should be between 3 and 100 characters long");
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
				if (count != 0) {
					try {
						TimeUnit.MILLISECONDS.sleep(15000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					count++;
				}
				new Main(userRec);
			}
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
	
	/**
	 * Dijkstra's algorithm from GraphStream
	 * @param x start node
	 * @param y end node
	 * @return list of nodes representing the shortest path
	 */
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
     * @return random generated string
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
