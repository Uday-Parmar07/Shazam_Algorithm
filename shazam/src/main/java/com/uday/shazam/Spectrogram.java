package com.uday.shazam.dsp;

import org.jtransforms.fft.DoubleFFT_1D;
import java.util.ArrayList;
import java.util.List;

public class Spectrogram {

    public static List<double[]> compute(double[] samples, int window, int hop) {
        DoubleFFT_1D fft = new DoubleFFT_1D(window);
        List<double[]> frames = new ArrayList<>();

        for (int start = 0; start + window < samples.length; start += hop) {

            double[] frame = new double[window];
            System.arraycopy(samples, start, frame, 0, window);

            // Hann window
            for (int i = 0; i < window; i++)
                frame[i] *= 0.5 * (1 - Math.cos(2 * Math.PI * i / (window - 1)));

            double[] spectrum = new double[window * 2];
            System.arraycopy(frame, 0, spectrum, 0, window);

            fft.realForwardFull(spectrum);

            double[] mag = new double[window];
            for (int i = 0; i < window; i++) {
                double re = spectrum[2*i];
                double im = spectrum[2*i+1];
                mag[i] = Math.sqrt(re*re + im*im);
            }

            frames.add(mag);
        }

        return frames;
    }
}
