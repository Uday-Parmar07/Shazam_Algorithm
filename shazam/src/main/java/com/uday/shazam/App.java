package com.uday.shazam;

import com.uday.shazam.audio.AudioLoader;
import com.uday.shazam.dsp.Spectrogram;
import com.uday.shazam.dsp.PeakFinder;
import com.uday.shazam.fingerprint.Fingerprinter;
import com.uday.shazam.fingerprint.HashDatabase;
import com.uday.shazam.fingerprint.Matcher;

import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {

        // ---- Load audio ----
        double[] samples = AudioLoader.loadMonoWav("audio/test.wav");
        System.out.println("Loaded samples: " + samples.length);

        int window = 2048;
        int hop = 512;

        // ---- Spectrogram (STFT) ----
        List<double[]> spec = Spectrogram.compute(samples, window, hop);
        System.out.println("Frames computed: " + spec.size());

        // ---- Peak Detection ----
        List<PeakFinder.Peak> peaks = PeakFinder.findPeaks(
                spec,
                5,   // freq neighborhood
                3,   // time neighborhood
                5.0  // amplitude threshold
        );

        System.out.println("Total peaks detected: " + peaks.size());

        // ---- Fingerprint Hashes ----
        List<Fingerprinter.HashPoint> hashes = Fingerprinter.generateHashes(
                peaks,
                5,   // fan-out
                50   // max time delta
        );

        System.out.println("Total hashes generated: " + hashes.size());

        System.out.println("First 10 hashes (f1,f2,dt):");
        for (int i = 0; i < Math.min(10, hashes.size()); i++) {
            Fingerprinter.HashPoint h = hashes.get(i);
            System.out.printf("(%d,%d,%d) ", h.f1, h.f2, h.dt);
        }
        System.out.println();

        // ---- Indexing Mode (store hashes for song) ----
        HashDatabase db = new HashDatabase();
        String songId = "song-1";

        for (Fingerprinter.HashPoint h : hashes) {
            db.addHash(songId, h.f1, h.f2, h.dt, h.t1);
        }

        System.out.println("Hashes stored in DB: " + hashes.size());

        // ---- Query Mode (match against DB) ----
        String result = Matcher.match(hashes, db);
        System.out.println("Match result: " + result);
    }
}
