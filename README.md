# Anime Recomendations

## Running
Run from the Main.java.
This opens up a GUI where each user can get recommendations based on their preferences.
When finished, exit out of the user prompt to see a visualization comparing the results of users.
Please enter valid numbers when asked otherwise the program will exit.

Note: It is highly recommended the number of genres entered be less 
than 3 because the more genres inputted, the more websites it will scrape,
and the more likely MyAnimeList will start the anti-bot program. This goes for 
the number of anime watched as well. 

# Classes
AnimePage.java
Representation of the MyAnimeList.com anime page. Stores information such as
name, url, year, etc. Assumption: It connects to the website through JSOUP 
through a search query and selects the top result. It then gets the frequency 
of the user recommendations.

AnimePageMap.java
Maps name (string) to AnimePage to avoid repeating the same page multiple
times.

AnimeVisualization.java
A testing class for Main. It has the same structure but it inputs users and animes
manually and outputs the resulting graph. 

Genre.java
Holds all types of genres.

Main.java
The class to run the main program. It asks the user for several inputs
and gives them a recommendation. It also gives the user the option to 
open a map to see the social network and see the shortest path
between users and the triadic closures. 

Profile.java
Class representation of the user profile which holds their personal information, 
preferences, and animes watched. 

Recommendation.java
Takes the animes and preferences of the users and outputs the top 3 anime
recommendations. 
