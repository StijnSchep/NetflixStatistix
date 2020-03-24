package Presentation.Commons.MoviePanel;

import Data.Repositories.MovieRepository;
import Domain.Movie;

import javax.swing.*;
import java.util.List;

class MovieList extends JPanel {
    //Is a container for MovieInformation objects
    //Container changes based on which constructor is called

    //Constructor for calling movies based on filter
    MovieList(String filter) {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        List<Movie> movies = MovieRepository.getMoviesByFilter(filter);
        for(Movie m : movies) {
            add(new MovieInformation(m));
        }
    }

    //Constructor for calling the movie that is the longest for people younger than 16
    MovieList(boolean isLongestFilm) {
        if(isLongestFilm) {
            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(layout);

            List<Movie> movies = MovieRepository.getLongestMovie();

            for(Movie m : movies) {
                add(new MovieInformation(m));
            }
        }
    }

    //Default constructor for calling all movies from the database
    MovieList() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        List<Movie> movies = MovieRepository.getAllMovies();
        for(Movie m : movies) {
            add(new MovieInformation(m));
        }
    }
}
