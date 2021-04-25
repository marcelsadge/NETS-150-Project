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
        if (s.equals("Martial Arts")) return Genre.MartialArts;
        else if (s.equals("Sci-Fi")) return Genre.SciFi;
        else if (s.equals("Shoujo Ai")) return Genre.ShoujoAi;
        else if (s.equals("Shounen Ai")) return Genre.ShounenAi;
        else if (s.equals("Slice of Life")) return Genre.SliceOfLife;
        else if (s.equals("Super Power")) return Genre.SuperPower;
        else return Genre.valueOf(s);
    }
}
