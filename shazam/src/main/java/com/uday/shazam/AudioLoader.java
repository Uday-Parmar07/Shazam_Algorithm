package com.uday.shazam.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioLoader {

    // Reads WAV and returns samples as doubles in [-1,1]
    public static double[] loadMonoWav(String path) throws Exception {
        File file = new File(path);
        AudioInputStream stream = AudioSystem.getAudioInputStream(file);

        AudioFormat baseFormat = stream.getFormat();

        AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                1,                       // force mono
                2,
                baseFormat.getSampleRate(),
                false);

        AudioInputStream monoStream = AudioSystem.getAudioInputStream(decodedFormat, stream);

        List<Double> samples = new ArrayList<>();
        byte[] buffer = new byte[4096];

        int bytesRead;
        while ((bytesRead = monoStream.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i += 2) {
                int sample = (buffer[i+1] << 8) | (buffer[i] & 0xff);
                samples.add(sample / 32768.0);
            }
        }

        double[] arr = new double[samples.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = samples.get(i);
        return arr;
    }
}
