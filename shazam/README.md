# Shazam Algorithm Prototype

## Overview
This repository contains a prototype implementation of the classic Shazam audio fingerprinting workflow. The current codebase is optimized for the landmark-based fingerprints that Shazam popularized. With an appropriate corpus of fingerprints generated using the same strategy, the matcher scales to larger databases without code changes.

> **Note**
> A previous experiment attempted to back the matcher with AcoustID fingerprints. That effort was abandoned because AcoustID relies on Chromaprint hashes, which are incompatible with the constellation-map fingerprints used here. As a result, only Shazam-style fingerprints should be ingested for reliable recognition.

## Project Layout
- `audio/`: Sample clips used for local experiments.
- `src/main/java/com/uday/shazam/`: Core implementation including fingerprint extraction, peak detection, spectrogram utilities, and database matching.
- `src/test/java/com/uday/shazam/`: Placeholder for unit and integration tests.

## Running the Application
1. Ensure you have Java 8+ and Maven installed. (Future upgrades to newer LTS JDKs require the `pom.xml` compiler settings to be updated accordingly.)
2. From the project root (`shazam/`), build the project:
   ```bash
   mvn clean package
   ```
3. Launch the CLI application with Maven:
   ```bash
   mvn -q exec:java -Dexec.mainClass="com.uday.shazam.App"
   ```
   Alternatively, run the compiled jar located at `target/shazam-1.0-SNAPSHOT.jar` once the build completes.

## Using a Larger Fingerprint Database
- Generate fingerprints from your audio catalog using the provided peak finder and fingerprinter classes (`PeakFinder`, `Fingerprinter`).
- Store the hashes with their time offsets in a backing datastore through `fingerprint/HashDatabase`.
- When the database follows the Shazam landmark technique (pairing anchor peaks with target peaks and hashing the frequency-time offsets), the matcher scales linearly with the number of hashes and supports sizeable libraries.
- Mixing other fingerprinting schemes, like Chromaprint/AcoustID, will not work because their hash format and temporal assumptions differ from the tuple-based hashes expected by `fingerprint/Matcher`.

## Shazam Fingerprinting Workflow
1. **Spectrogram Generation**: Convert audio frames to the frequency domain (`Spectrogram`) using short-time Fourier transforms.
2. **Peak Detection**: Identify robust local maxima across frequency bands (`PeakFinder`).
3. **Fingerprint Creation**: Form landmark pairs combining anchor and target peaks, hashing their frequency difference and time delta (`Fingerprinter`).
4. **Database Storage**: Persist hashes and their associated track/time references (`HashDatabase`).
5. **Query Matching**: Fingerprint the query clip, retrieve hash matches, compute offset histograms, and select the track with the highest aligned vote count (`Matcher`).

## Next Steps
- Replace the current in-memory map with a persistent store (e.g., Redis, PostgreSQL) for production-scale catalogs.
- Add automated tests that cover fingerprint generation, hash storage, and matching accuracy.
- Benchmark matching performance with progressively larger datasets to tune peak-selection thresholds and hash fan-out.
