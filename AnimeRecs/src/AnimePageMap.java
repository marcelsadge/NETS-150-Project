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

    /**
     * Constructor for AnimePageMap
     */
    private AnimePageMap() {
        urlList = new ArrayList<String>();
        animePageList = new ArrayList<AnimePage>();
        nameList = new ArrayList<String>();
    }
    
    /**
     * Maps an url to an AnimePage
     * @param url Url to be put
     * @param a AnimePage to be put
     */
    public void put(String url, AnimePage a) {
        if (!(urlList.contains(url))) {
            nameList.add(a.getName());
            urlList.add(url);
            animePageList.add(a);
        }
    }

    /**
     * Returns an animePage mapped to a specific name
     * @param name Target name
     * @return AnimePage mapped to name
     */
    public AnimePage getByName(String name) {
        return animePageList.get(nameList.indexOf(name));
    }

    /**
     * Returns whether the AnimePageMap contains the target anime by name
     * @param url URL to be checked
     * @return true if name is present
     */
    public boolean containsUrl(String url) {
        return urlList.contains(url);
    }
    /**
     * Returns whether the AnimePageMap contains the target anime by name
     * @param name Name to be checked
     * @return true if name is present
     */
    public boolean containsName(String name) {
    	return nameList.contains(name);
    }
    
    /**
     * Returns the size of the AnimePageMap
     * @return size of AnimePageMap
     */
    public int size() {
    	return animePageList.size();
    }
}