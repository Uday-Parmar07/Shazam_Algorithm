package com.uday.shazam.fingerprint;

import com.uday.shazam.dsp.PeakFinder;
import java.util.ArrayList;
import java.util.List;

public class Fingerprinter {

    public static class HashPoint {
        public final int f1;
        public final int f2;
        public final int dt;
        public final int t1;   // anchor time

        public HashPoint(int f1, int f2, int dt, int t1) {
            this.f1 = f1;
            this.f2 = f2;
            this.dt = dt;
            this.t1 = t1;
        }
    }

    public static List<HashPoint> generateHashes(
            List<PeakFinder.Peak> peaks,
            int fanOut,
            int maxTimeDelta
    ) {
        List<HashPoint> hashes = new ArrayList<>();

        for (int i = 0; i < peaks.size(); i++) {
            PeakFinder.Peak anchor = peaks.get(i);

            for (int j = 1; j <= fanOut && i + j < peaks.size(); j++) {
                PeakFinder.Peak target = peaks.get(i + j);

                int dt = target.t - anchor.t;
                if (dt <= 0 || dt > maxTimeDelta) continue;

                hashes.add(new HashPoint(
                        anchor.f,
                        target.f,
                        dt,
                        anchor.t
                ));
            }
        }

        return hashes;
    }
}
