package ru.practicum.moviehub.model;

public class Movie {
    private long id;
    private String title;

    public Movie(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String toJson() {
        return "{\"id\":\"" + id + "\",\"title\":\"" + title + "\"}";
    }
}
