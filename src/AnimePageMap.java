package src;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps url (string) to AnimePage to avoid repeating the same page multiple
 * times.
 */
public class AnimePageMap {
    
    private static AnimePageMap instance;
    private Map<String, AnimePage> UrlToAnimePageMap;

    /**
     * Get pointer to AnimePageMap singleton
     * @return AnimePageMap instance
     */
    public static AnimePageMap getInstance() {
        if (instance == null) {
            instance = new AnimePageMap();
        }
        return instance;
    }

    private AnimePageMap() {
        UrlToAnimePageMap = new HashMap<String, AnimePage>();
    }

    public void put(String url, AnimePage a) {
        UrlToAnimePageMap.put(url, a);
    }

    public AnimePage get(String url) {
        return UrlToAnimePageMap.get(url);
    }

    public Boolean containsUrl(String url) {
        return UrlToAnimePageMap.containsKey(url);
    }
}
