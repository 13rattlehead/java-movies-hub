package ru.practicum.moviehub.store;

import ru.practicum.moviehub.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class MoviesStore {
    private final List<Movie> movies = new ArrayList<>();

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void removeMovie(long id) {
        movies.removeIf(movie -> movie.getId() == id);
    }

    public void clear() {
        movies.clear();
    }

    public List<Movie> getAll() {
        return movies;
    }
}
