package com.uday.shazam.fingerprint;

import java.util.*;

public class Matcher {

    public static String match(
            List<Fingerprinter.HashPoint> query,
            HashDatabase db
    ) {
        // songId -> (offset -> votes)
        Map<String, Map<Integer, Integer>> votes = new HashMap<>();

        for (Fingerprinter.HashPoint q : query) {
            List<HashDatabase.Entry> matches = db.lookup(q.f1, q.f2, q.dt);

            for (HashDatabase.Entry e : matches) {
                int offset = e.t - q.t1;

                Map<Integer, Integer> songOffsets =
                        votes.computeIfAbsent(e.songId, k -> new HashMap<>());

                songOffsets.put(offset, songOffsets.getOrDefault(offset, 0) + 1);
            }
        }

        String bestSong = null;
        int bestVotes = 0;

        for (Map.Entry<String, Map<Integer, Integer>> song : votes.entrySet()) {
            for (Map.Entry<Integer, Integer> bucket : song.getValue().entrySet()) {

                if (bucket.getValue() > bestVotes) {
                    bestVotes = bucket.getValue();
                    bestSong = song.getKey();
                }
            }
        }

        return bestSong == null
                ? "NO MATCH"
                : bestSong + " (score=" + bestVotes + ")";
    }
}
