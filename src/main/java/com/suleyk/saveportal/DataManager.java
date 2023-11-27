package com.suleyk.saveportal;

import java.util.ArrayList;
import java.util.List;

public class DataManager {


    public static List<String> getGameList() {
        // Fetch games from the data source
        List<String> games = new ArrayList<>();
        games.add("Game 1");
        games.add("Game 2");
        games.add("Game 3");
        return games;
    }
}
