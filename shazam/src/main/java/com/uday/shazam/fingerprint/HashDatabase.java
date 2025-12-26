package com.uday.shazam.fingerprint;

import java.util.*;

public class HashDatabase {

    // hashKey -> list of (songId, timeInSong)
    private final Map<String, List<Entry>> map = new HashMap<>();

    public static class Entry {
        public final String songId;
        public final int t;

        public Entry(String songId, int t) {
            this.songId = songId;
            this.t = t;
        }
    }

    private static String makeKey(int f1, int f2, int dt) {
        return f1 + "|" + f2 + "|" + dt;
    }

    public void addHash(String songId, int f1, int f2, int dt, int t) {
        String key = makeKey(f1, f2, dt);
        map.computeIfAbsent(key, k -> new ArrayList<>())
           .add(new Entry(songId, t));
    }

    public List<Entry> lookup(int f1, int f2, int dt) {
        String key = makeKey(f1, f2, dt);
        return map.getOrDefault(key, Collections.emptyList());
    }
}
