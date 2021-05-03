import java.util.ArrayList;
import java.util.List;

/**
 * Maps name (string) to AnimePage to avoid repeating the same page multiple
 * times.
 */
public class AnimePageMap {
    
    private static AnimePageMap instance;
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
        animePageList = new ArrayList<AnimePage>();
        nameList = new ArrayList<String>();
    }
    
    /**
     * Maps an name to an AnimePage
     * @param name name to be put
     * @param a AnimePage to be put
     */
    public void put(AnimePage a) {
        if (!(nameList.contains(a.getName()))) {
            nameList.add(a.getName());
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