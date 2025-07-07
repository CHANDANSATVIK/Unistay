package com.example.unistay;

import java.util.ArrayList;
import java.util.List;

public class WishlistManager {
    private static final List<Property> favorites = new ArrayList<>();

    public static void add(Property property) {
        if (!favorites.contains(property)) {
            favorites.add(property);
        }
    }

    public static void remove(Property property) {
        favorites.remove(property);
    }

    public static boolean isFavorite(Property property) {
        return favorites.contains(property);
    }

    public static List<Property> getFavorites() {
        return new ArrayList<>(favorites); // avoid modifying original list
    }
}
