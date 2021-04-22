package src;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A class representing the page of an Anime
 */

public class AnimePage {
    
    private String name;
    private String url;
    private Document doc;

    private List<Genre> genreList; 
    private Map<AnimePage, Integer> recommendedAnimeToFrequencyMap;

    /**
     * Constructor for AnimePage
     * @param url A valid url
     * @throws IOException if there is an issue with the url
     */
    public AnimePage(String url) throws IOException{
        this.url = url;
        doc = Jsoup.connect(url).get();
        name = doc.title().replace(" - MyAnimeList.net", "");

        genreList = new LinkedList<Genre>();
        setGenreList();

        recommendedAnimeToFrequencyMap = new HashMap<AnimePage, Integer>();
    }

    private void setGenreList() {
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
    public Map<AnimePage, Integer> getRecommendedAnimeToFrequencyMap() throws IOException{

        doc = Jsoup.connect(url + "/userrecs").get();
        Elements targets = doc.selectFirst("div.border_solid").nextElementSiblings();
        Elements targetPages = targets.select("div.picSurround").select("a");
        Elements targetNum = targets.select("a.js-similar-recommendations-button").select("strong");
        for (Element e : targetPages) {
            System.out.println(e.attr("abs:href"));
        }
        for (Element e: targetNum) {
            System.out.println(e.text());
        }
        for (int i = 0; i<targetPages.size(); i++) {
            recommendedAnimeToFrequencyMap.put(new AnimePage(targetPages.get(i).attr("abs:href")),
                    Integer.parseInt(targetNum.get(i).text()));
        }
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
     * Gets the list of genres of the anime
     * @return
     */
    public List<Genre> getGenreList() {
        return genreList;
    }
    public static void main(String[] args) {
        try {
            AnimePage a = new AnimePage("https://myanimelist.net/anime/5114/"
            +"Fullmetal_Alchemist__Brotherhood");
            System.out.println(a.getName());
            
            for (Genre g : a.getGenreList()) {
                System.out.println(g);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
