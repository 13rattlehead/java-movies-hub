package ru.practicum.moviehub.http;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.util.List;


public class MoviesHandler extends BaseHttpHandler {

    private final MoviesStore store;

    public MoviesHandler(MoviesStore store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            List<Movie> movies = store.getAll();

            if (movies.isEmpty()) {
                sendJson(ex, 200, "[]");
                return;
            }

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < movies.size(); i++) {
                json.append(movies.get(i).toJson());
                if (i < movies.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            sendJson(ex, 200, json.toString());
        } else {
            ex.sendResponseHeaders(405, -1);
            ex.close();
        }
    }
}