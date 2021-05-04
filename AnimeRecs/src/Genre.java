/**
 * An enum representing a MAL genre.
 */
public enum Genre {
    Action,
    Adventure,
    Cars,
    Comedy,
    Dementia,
    Demons,
    Drama,
    Ecchi, 
    Fantasy,
    Game,
    Harem,
    Hentai,
    Historical,
    Horror, 
    Josei,
    Kids,
    Magic,
    MartialArts,
    Mecha,
    Military,
    Music,
    Mystery,
    Parody,
    Police,
    Psychological,
    Romance,
    Samurai,
    School,
    SciFi, 
    Seinen,
    Shoujo,
    ShoujoAi,
    Shounen,
    ShounenAi,
    SliceOfLife,
    Space,
    Sports,
    SuperPower,
    Supernatural,
    Thriller,
    Vampire,
    Yaoi,
    Yuri;

    public static Genre fromString(String s) {
        s = s.toLowerCase();
        if (s.equals("martial arts")) return Genre.MartialArts;
        else if (s.equals("sci-fi")) return Genre.SciFi;
        else if (s.equals("shoujo ai")) return Genre.ShoujoAi;
        else if (s.equals("shounen ai")) return Genre.ShounenAi;
        else if (s.equals("slice of life")) return Genre.SliceOfLife;
        else if (s.equals("super power")) return Genre.SuperPower;
        else {
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            return Genre.valueOf(s);
        }
    }
}
