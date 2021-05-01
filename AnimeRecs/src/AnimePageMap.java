import java.util.ArrayList;
import java.util.List;

/**
 * Maps url (string) to AnimePage to avoid repeating the same page multiple
 * times.
 */
public class AnimePageMap {
    
    private static AnimePageMap instance;
    private List<String> urlList;
    private List<AnimePage> animePageList;
    private List<String> nameList;
    
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
        urlList = new ArrayList<String>();
        animePageList = new ArrayList<AnimePage>();
        nameList = new ArrayList<String>();
    }

    public void addAnime(String query) {
        AnimePage a = new AnimePage(query);
        put(a.getUrl(), a);
    }

    
    public void put(String url, AnimePage a) {
        if (!(urlList.contains(url))) {
            nameList.add(a.getName());
            urlList.add(url);
            animePageList.add(a);
        }
    }

    public AnimePage getByUrl(String url) {
        return animePageList.get(urlList.indexOf(url));
    }

    public AnimePage getByName(String name) {
        return animePageList.get(nameList.indexOf(name));
    }

    public boolean containsUrl(String url) {
        return urlList.contains(url);
    }
    
    public boolean containsName(String name) {
    	return nameList.contains(name);
    }

    public List<String> getUrls() {
        return urlList;
    }
    
    public List<AnimePage> getAnimePages() {
        return animePageList;
    }
}