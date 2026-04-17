package ru.practicum.moviehub.http;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.List;


public class MoviesHandler extends BaseHttpHandler {

    private final MoviesStore moviesStore;

    public MoviesHandler(MoviesStore moviesStore) {
        this.moviesStore = moviesStore;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            List<Movie> movies = moviesStore.getMovies();

            if (movies.isEmpty()) {
                sendJson(ex, 200, "[]");
            } else {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < movies.size(); i++) {
                    sb.append(movies.get(i).toJson());
                    if (i < movies.size() - 1) {
                        sb.append(",");
                    }
                }
            }

        } else {
            ex.sendResponseHeaders(405, -1);
            ex.close();
        }

    }
}
