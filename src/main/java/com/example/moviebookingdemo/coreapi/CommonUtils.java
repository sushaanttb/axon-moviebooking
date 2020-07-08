package com.example.moviebookingdemo.coreapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonUtils {

    public static <T> T selectRandomKey(List<T> keys){
        int randomNum = (int) Math.floor(Math.random() * Math.floor(keys.size()));
        return keys.get(randomNum);
    }

    public static String selectRandomMovie(Map<String,MovieSlot> movies){
        List<String> keysList = new ArrayList<>(movies.keySet());
        return selectRandomKey(keysList);

    }

    public static String selectFirstMovie(Map<String,MovieSlot> movies){
        List<String> keysList = new ArrayList<>(movies.keySet());
        return keysList.get(0);
    }

}
