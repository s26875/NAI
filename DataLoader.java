import java.io.*;
import java.util.*;

public class DataLoader {
    private String filepath;
    private List<List<String>> features = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private Map<Integer, Set<String>> attributeValues = new HashMap<>();
    private Set<String> labelValues = new HashSet<>();
    private int expectedFeaturesCount = -1;  // pomaga weryfikować spójność liczby atrybutów w różnych wierszach, co jest kluczowe dla zachowania integralności danych.

    public DataLoader(String filepath) {
        this.filepath = filepath;
    }

    public void loadData() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) { // zapewnia efektywne wczytywanie danych z pliku
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim(); // Usuwanie białych znaków
                if (line.isEmpty()) continue; // Pomijanie pustych linii

                String[] parts = line.split(",");
                if (parts.length < 2) {
                    System.out.println("Warning: Skipping line " + lineNumber + " due to insufficient data.");
                    continue; // Zakłada przynajmniej jedną cechę i etykietę
                }

                List<String> featureValues = new ArrayList<>();
                for (int i = 0; i < parts.length - 1; i++) {
                    featureValues.add(parts[i].trim().toLowerCase()); // Convert feature to lower case
                }
                String label = parts[parts.length - 1].trim().toLowerCase(); // Convert label to lower case

                features.add(featureValues);
                labels.add(label);

                for (int i = 0; i < featureValues.size(); i++) {
                    attributeValues.putIfAbsent(i, new HashSet<>());
                    attributeValues.get(i).add(featureValues.get(i));
                }
                labelValues.add(label);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + filepath);
            throw e;
        } catch (IOException e) {
            System.out.println("Error: Problem reading file - " + filepath);
            throw e;
        }
    }


    public List<List<String>> getFeatures() {
        return features;
    }

    public List<String> getLabels() {
        return labels;
    }

    public Map<Integer, Set<String>> getAttributeValues() {
        return attributeValues;
    }

    public Set<String> getLabelValues() {
        return labelValues;
    }
}
