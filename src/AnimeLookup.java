package src;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Lookups query for specific anime pages on MAL
 */
public class AnimeLookup {
    
    Document doc;

    /**
     * Searches for a specific anime query
     * @param query Query to lookup. 100 characters max
     * @return AnimePage of first search result. Is null if there are no search
     * results
     * @throws IllegalArgumentException If query has more than 100 characters
     */
    public AnimePage lookup(String query) throws IllegalArgumentException{
        
        if (query.length() > 100) {
            throw new IllegalArgumentException();
        }

        try {
            doc = Jsoup.connect("https://myanimelist.net/anime.php?cat=anime&q="
                    + query).get();
            String foundUrl = doc.selectFirst("a.hoverinfo_trigger")
                    .attr("abs:href");
            //System.out.println(foundUrl);
            if (foundUrl ==  null) {
                return null;
            }
            return new AnimePage(foundUrl);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        
        AnimeLookup al = new AnimeLookup();
        al.lookup("fullmetalasdfasdfasdf");
    }
}
