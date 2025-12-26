package com.uday.shazam.dsp;

import java.util.ArrayList;
import java.util.List;

public class PeakFinder {

    public static class Peak {
        public final int t;   // frame index
        public final int f;   // frequency bin
        public final double mag;

        public Peak(int t, int f, double mag) {
            this.t = t;
            this.f = f;
            this.mag = mag;
        }
    }

    public static List<Peak> findPeaks(
            List<double[]> spec,
            int freqNeighborhood,
            int timeNeighborhood,
            double magnitudeThreshold
    ) {
        List<Peak> peaks = new ArrayList<>();

        for (int t = 0; t < spec.size(); t++) {
            double[] frame = spec.get(t);

            for (int f = 1; f < frame.length - 1; f++) {

                double mag = frame[f];
                if (mag < magnitudeThreshold) continue;

                boolean isPeak = true;

                for (int dt = -timeNeighborhood; dt <= timeNeighborhood; dt++) {
                    int tt = t + dt;
                    if (tt < 0 || tt >= spec.size()) continue;

                    double[] nbFrame = spec.get(tt);

                    for (int df = -freqNeighborhood; df <= freqNeighborhood; df++) {
                        int ff = f + df;
                        if (ff < 0 || ff >= nbFrame.length) continue;

                        if (nbFrame[ff] > mag) {
                            isPeak = false;
                            break;
                        }
                    }
                    if (!isPeak) break;
                }

                if (isPeak)
                    peaks.add(new Peak(t, f, mag));
            }
        }

        return peaks;
    }
}
