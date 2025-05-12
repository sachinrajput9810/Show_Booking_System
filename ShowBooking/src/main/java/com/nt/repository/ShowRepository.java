package com.nt.repository;

import com.nt.model.Show;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ShowRepository {
    private final Map<String, Show> shows = new HashMap<>();

    public void save(Show show) {
        shows.put(show.getName(), show);
    }

    public Show findByName(String name) {
        return shows.get(name);
    }

    public Map<String, Show> findAll() {
        return shows;
    }
}
