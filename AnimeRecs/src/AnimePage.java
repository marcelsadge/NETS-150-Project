import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A class representing the page of an Anime on MAL
 */
public class AnimePage {
    
    private String name;
    private String url;
    private Document doc;
    private double score;
    private int numEpisodes;
    private int releasedYear;

    // all the genres this anime falls under
    private List<Genre> genreList; 
    
    private Map<String, Integer> recommendedAnimeToFrequencyMap;

    /**
     * Constructor for AnimePage
     * @param query A query to search for. Must be <= 100 characters
     * @throws IllegalArgumentException If query > 100 characters
     */
    public AnimePage(String query) throws IllegalArgumentException{

    	// get our global anime page map
    	AnimePageMap animePageMap = AnimePageMap.getInstance();
    	
        if (query.length() > 100) {
            throw new IllegalArgumentException();
        }

        try {
            doc = Jsoup.connect("https://myanimelist.net/anime.php?cat=anime&q="
                    + query).get();
            url =  doc.selectFirst("a.hoverinfo_trigger")
                    .attr("abs:href");
            
            // stores the anime page map in the global mapping
            if (url == null) {
                throw new IllegalArgumentException();
            } else if (!animePageMap.containsUrl(url)) {
            	animePageMap.put(url, this);
            }
            
            doc = Jsoup.connect(url).get();
            
            // gets the name of the anime
            name = doc.select("h1[class=title-name h1_bold_none]").text();
            
            // gets the score of the anime if it is available
            String scoreStr = doc.select("div[class~=score-label score-.*]").text();
            if (scoreStr.equals("N/A")) {
            	score = 0;
            } else {
            	score = Double.parseDouble(scoreStr);
            }
            
            // sets the total number of episodes in the anime, max integer value if unknown
            String episodes = doc.selectFirst("div[class=spaceit]:matches(Episodes:.*)").text();
            Pattern pattern1 = Pattern.compile("Episodes: (\\d+)");
        	Matcher m1 = pattern1.matcher(episodes);
        	if (m1.find()) {
        		numEpisodes = Integer.parseInt(m1.group(1));
        	} else {
        		numEpisodes = Integer.MAX_VALUE;
        	}
        	
        	// sets the release year of the anime
        	String aired = doc.selectFirst("div[class=spaceit]:matches(Aired:.*)").text();
        	Pattern pattern2 = Pattern.compile("Aired: (\\w+ \\d+,)? (\\d+)");
        	Matcher m2 = pattern2.matcher(aired);
        	if (m2.find()) {
        		releasedYear = Integer.parseInt(m2.group(2));
        	} else {
        		System.out.println("Release year not found");
        		releasedYear = Integer.MAX_VALUE;
        	}
            
        	// sets the Genres for this anime
            genreList = new LinkedList<Genre>();
            setGenreList();
            
            System.out.println("Opened Page: " + name);
            
            recommendedAnimeToFrequencyMap = new HashMap<String, Integer>();
            
        } catch (IOException e) {}
    }
    
    /**
     * Helper function to set the genreList of the AnimePage
     */
    void setGenreList() {
        for (Element e : doc.select("span[itemprop=genre]")) {
            if (e.text() != "") {
                genreList.add(Genre.fromString(e.text()));
            }
        }
    }
    
    /**
     * Get a map of recommended AnimePages to frequency
     * @return a map of recommended AnimePages to frequency
     * @throws IOException
     */
    public void setRecommendedAnimeToFrequencyMap() throws IOException{

        doc = Jsoup.connect(url + "/userrecs").get();
        
        // select all the borderClasses that has animes inside of them
        Elements targets = doc.selectFirst("div.border_solid")
                .nextElementSiblings().select("div[class=borderClass]");
        
        for (Element t : targets) {
        	
        	// get the anime name
        	Element animeNameTag = t.selectFirst("div[style~=margin-bottom.*]").selectFirst("a");
        	String animeName = animeNameTag.text();
        	
        	// get the recommended frequency
        	int frequency = 0;
        	try {
        		Element targetNum = t.selectFirst("a.js-similar-recommendations-button")
            			.selectFirst("strong");
        		frequency = Integer.parseInt(targetNum.text()) + 1;
        	} catch (Exception e) {
        		frequency = 1;
        	}
        	

            recommendedAnimeToFrequencyMap.put(animeName, frequency);
        	
        }
        
    }

    
    /**
     * Returns a map of the an Anime to the number of times it was recommended
     * @return Map of anime to recommended frequency
     */
    public Map<String, Integer> getRecommendedAnimeToFrequencyMap() {
        return recommendedAnimeToFrequencyMap;
    }


    
    /**
     * Gets the name of the anime
     * @return The name of the anime
    */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the name of the anime
     * @return The name of the anime
    */
    public double getScore() {
    	return score;
    }
    
    /**
     * Gets the number of episodes in the anime
     * @return The number of episodes
    */
    public int getEpisodes() {
    	return numEpisodes;
    }

    /**
     * Gets the year the anime was released
     * @return The year the anime was released
     */
    public int getReleasedYear() {
    	return releasedYear;
    }
    /**
     * Gets the list of genres of the anime
     * @return A list of Genres
     */
    public List<Genre> getGenreList() {
        return new LinkedList<Genre>(genreList);
    }

    /**
     * Downloads the image locally and gets the image's filepath
     * @return filepath to image, null if error
     */
    public String getImage() {
        
        System.out.println(doc.title());
        Element img = doc.selectFirst("img.lazyload");
        String imgUrl = img.attr("data-src");

        String fpath = "images/" + name.replace(" ", "_") + ".jpg";
        try {
            Response resultImageResponse = Jsoup.connect(imgUrl)
                    .ignoreContentType(true).execute();
            File f = new File(fpath);

            if (f.exists()) {
                return fpath;
            } else {
                FileOutputStream out = new FileOutputStream(f);
                out.write(resultImageResponse.bodyAsBytes()); 
                out.close();
                return fpath;
            }
            
        } catch (IOException e) {
        }
        return null;
    }
    
    /**
     * Getter method for the url of this AnimePage
     * @return The url for this page
     */
    public String getUrl() {
        return url;
    }

    public static void main(String[] args) {
        AnimePage a = new AnimePage("one piece");
        System.out.println("Anime Name: " + a.getName());
        System.out.println("Anime Score: " + a.getScore());
        System.out.println("Number Of Episodes: " + a.getEpisodes());
        System.out.println("Release Year: " + a.getReleasedYear());
        
        for (Genre g : a.getGenreList()) {
            System.out.println(g);
        }
        
        try {
            a.setRecommendedAnimeToFrequencyMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        a.getImage();
    }
}