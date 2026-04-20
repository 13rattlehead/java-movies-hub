package ru.practicum.moviehub.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MoviesApiTest {

    private static final String BASE = "http://localhost:8080";
    private static MoviesServer server;
    private static HttpClient client;
    private static MoviesStore store;
    private static final int port = 8080;

    @BeforeAll
    static void beforeAll() throws Exception {
        store = new MoviesStore();
        server = new MoviesServer(store, port);

        System.out.println("Запускаем MoviesServer");
        server.start();


        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        System.out.println("Тестовый сервер и HTTP-клиент готовы");
    }


    @BeforeEach
    void beforeEach() {
        store.clear();
    }

    @AfterAll
    static void afterAll() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void getMovies_whenEmpty_returnsEmptyArray() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(200, resp.statusCode(), "GET /movies должен вернуть 200");

        assertEquals("application/json; charset=UTF-8",
                resp.headers().firstValue("Content-Type").orElse(""),
                "Неверный Content-Type");

        String body = resp.body().trim();
        assertTrue(body.startsWith("[") && body.endsWith("]"),
                "Ожидается JSON-массив");
        assertEquals("[]", body, "Пустой магазин должен возвращать []"); // ← точнее
    }

    @Test
    void getMovies_whenStoreHasMovies_returnsCorrectList() throws Exception {
        // given
        store.addMovie(new Movie(0, "Matrix"));
        store.addMovie(new Movie(1, "Lalaland"));
        store.addMovie(new Movie(2, "Terminator"));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(
                req,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        assertEquals(200, resp.statusCode());

        assertEquals("application/json; charset=UTF-8",
                resp.headers().firstValue("Content-Type").orElse(""));

        String body = resp.body();

        Gson gson = new Gson();

        List<Movie> result = gson.fromJson(
                body,
                new ListOfMoviesTypeToken().getType()
        );

        assertEquals(3, result.size());

        assertEquals("Matrix", result.get(0).getTitle());
        assertEquals("Lalaland", result.get(1).getTitle());
        assertEquals("Terminator", result.get(2).getTitle());
    }
}