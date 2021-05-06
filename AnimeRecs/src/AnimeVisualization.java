import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.PageRank;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

/* This class is for testing purposes. The class manually inputs examples 
 * and outputs a graph with all the users.
 */
public class AnimeVisualization extends JFrame{
	
	static Graph graph;
	static HashMap<Node, List<String>> animeAdjMatrix;
	
	AnimeVisualization(List<String> path) {
		JList<String> displayList = new JList<>(path.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(displayList);
		
		getContentPane().add(scrollPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
	}
	
	public static void main(String[] args) {
		System.setProperty("org.graphstream.ui", "swing");
		graph = new MultiGraph("test");
		graph.setAttribute("ui.antialias", true);
		graph.setAttribute("ui.stylesheet", 
				"node {fill-mode: dyn-plain; size-mode: dyn-size; text-size: 20;} "
				+ "edge {fill-color:black;}");
		graph.display();
		
		SpriteManager sman = new SpriteManager(graph);
		
		String name1 = "bob1";
		
		Profile bobProf = new Profile(name1);
		
		bobProf.addAnime("bleach", 10);
		bobProf.addAnime("one piece", 7);
		bobProf.addAnime("dragon ball", 3);
		
		String name2 = "bob2";
		Profile bobProf2 = new Profile(name2);
		
		bobProf2.addAnime("bleach", 6);
		bobProf2.addAnime("one piece", 7);
		bobProf2.addAnime("my hero academia", 8);
		bobProf2.addAnime("dragon ball", 7);
		
		String name3 = "bob3";
		
		Profile bobProf3 = new Profile(name3);
		
		bobProf3.addAnime("Oregairu", 1);
		bobProf3.addAnime("Code Geass", 2);
		bobProf3.addAnime("Shield Hero", 3);
		bobProf3.addAnime("bleach", 4);
		
		String name4 = "bob4";
		
		Profile bobProf4 = new Profile(name4);
		
		bobProf4.addAnime("Code Geass", 2);
		bobProf4.addAnime("my hero academia", 2);
		
		String name5 = "bob5";
		
		Profile bobProf5 = new Profile(name5);
		
		bobProf5.addAnime("my hero academia", 2);
		
		ArrayList<Profile> users = new ArrayList<>();
		users.add(bobProf);
		users.add(bobProf2);
		users.add(bobProf3);
		users.add(bobProf4);
		users.add(bobProf5);
		
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
		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("To find shortest path between two users " +
			"\n(Please Enter 'Path')" +
					"\nTo find all triadic closures " +
			"\n(Please Enter any string)");
			
			String input = sc.nextLine();
			
			if (input.equals("Path")) {
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
			} else {
				animeAdjMatrix = new HashMap<>();
				
				for (Node node : graph) {
					if (node.getAttribute("ui.color").equals(Color.RED)) {
						animeAdjMatrix.put(node, new ArrayList<>());
					}
				}
				
				for (Node n : graph) {
					if (n.getAttribute("ui.color").equals(Color.RED)) {
						for (Node n2 : graph) {
							if (!n2.getAttribute("ui.color").equals(Color.RED)) {
								if (n.hasEdgeBetween(n2)) {
									List<String> animes = animeAdjMatrix.get(n);
									animes.add(n2.getId());
									animeAdjMatrix.replace(n, animes);
								}
							}
						}
						
					}
				}
				
				for (Entry<Node, List<String>> entry : animeAdjMatrix.entrySet()) {
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
				}
				graph.display();
			}
		}
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
