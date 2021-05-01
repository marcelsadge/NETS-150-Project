import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import org.graphstream.algorithm.PageRank;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

public class AnimeVisualization {
	public static void main(String[] args) {
		System.setProperty("org.graphstream.ui", "swing");
		Graph graph = new MultiGraph("test");
		graph.setAttribute("ui.antialias", true);
		graph.setAttribute("ui.stylesheet", 
				"node {fill-mode: dyn-plain; size-mode: dyn-size; text-size: 20;} edge {fill-color:black;}");
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
		bobProf2.addAnime("MHA", 8);
		bobProf2.addAnime("dragon ball", 7);
		
		String name3 = "bob3";
		
		Profile bobProf3 = new Profile(name3);
		
		bobProf3.addAnime("Oregairu", 1);
		bobProf3.addAnime("Code Geass", 2);
		bobProf3.addAnime("Shield Hero", 3);
		bobProf3.addAnime("bleach", 4);
		
		ArrayList<Profile> users = new ArrayList<>();
		users.add(bobProf);
		users.add(bobProf2);
		users.add(bobProf3);
		
		PageRank pageRank = new PageRank();
		pageRank.setVerbose(true);
		pageRank.init(graph);
		
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
					
					AnimePage page = new AnimePage(anime);
					page.getImage();
					
					Sprite sprite = sman.addSprite(getRandomString());
					sprite.attachToNode(anime); 
					sprite.setAttribute("ui.shape", "box");
					sprite.setAttribute("ui.size", 50);
					sprite.setAttribute("ui.fill-mode", "image-scaled");
					sprite.setAttribute("ui.fill-image", "url('images/" + page.getName().toLowerCase() + ".jpg");
				}
			}
			
			for (String anime : animes) {
				graph.addEdge(getRandomString(), user.getUsername(), anime);
			}
			
		}
		
		graph.nodes().forEach(n -> n.setAttribute("label", n.getId()));
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
