import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.URL;

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

    private List<Genre> genreList; 
    private Map<AnimePage, Integer> recommendedAnimeToFrequencyMap;

    /**
     * Constructor for AnimePage
     * @param query A query to search for. Must be <= 100 characters
     * @throws IllegalArgumentException If query > 100 characters
     */
    public AnimePage(String query) throws IllegalArgumentException{

        if (query.length() > 100) {
            throw new IllegalArgumentException();
        }

        try {
            doc = Jsoup.connect("https://myanimelist.net/anime.php?cat=anime&q="
                    + query).get();
            url =  doc.selectFirst("a.hoverinfo_trigger")
                    .attr("abs:href");
            
            if (url == null) {
                throw new IllegalArgumentException();
            }
            doc = Jsoup.connect(url).get();
            name = doc.title().replace(" - MyAnimeList.net", "").replace(" ", "_");
            
            genreList = new LinkedList<Genre>();
            setGenreList();
            
            recommendedAnimeToFrequencyMap = new HashMap<AnimePage, Integer>();

        } catch (IOException e) {}

    }
    /**
     * Helper function to set the genreList of the AnimePage
     */
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
    public void setRecommendedAnimeToFrequencyMap() throws IOException{

        doc = Jsoup.connect(url + "/userrecs").get();
        Elements targets = doc.selectFirst("div.border_solid")
                .nextElementSiblings();
        Elements targetPages = targets.select("div.picSurround").select("a");
        Elements targetNum = targets.select("a.js-similar-recommendations-button")
                .select("strong");

        AnimePage tempAnimePage = null;
        AnimePageMap animePageMap = AnimePageMap.getInstance();
        String url = null;

        int frequency;

        for (int i = 0; i < targetPages.size(); i++) {
            
            url = targetPages.get(i).attr("abs:href");

            try {
                frequency = Integer.parseInt(targetNum.get(i).text()) + 1;
            } catch (IndexOutOfBoundsException e) {
                frequency = 1;
            }

            System.out.println(url);
            System.out.println(frequency);

            if (animePageMap.containsUrl(url)) {
                tempAnimePage = animePageMap.get(url);
            } else {
                tempAnimePage = new AnimePage(url);
                animePageMap.put(url, tempAnimePage);
            }
            
            recommendedAnimeToFrequencyMap.put(tempAnimePage, frequency);
        }
    }

    public Map<AnimePage, Integer> getRecommendedAnimeToFrequencyMap() {
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
     * @return A list of Genres
     */
    public List<Genre> getGenreList() {
        return genreList;
    }

    /**
     * Downloads the image locally and gets the image's filepath
     * @return filepath to image, null if error
     */
    public String getImage() {
        
        System.out.println(doc.title());
        Element img = doc.selectFirst("img.lazyload");
        String imgUrl = img.attr("data-src");

        String fpath = "images/" + name + ".jpg";
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
    
    public static void main(String[] args) {
        AnimePage a = new AnimePage("fullmetalasdf");
        System.out.println(a.getName());
            
        for (Genre g : a.getGenreList()) {
            System.out.println(g);
        }
        //a.setRecommendedAnimeToFrequencyMap();
        a.getImage();
    }
}