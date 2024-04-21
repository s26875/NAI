import java.util.*;

public class NaiveBayesClassifier {
    private Map<String, Double> priorProbabilities = new HashMap<>();
    private Map<Integer, Map<String, Map<String, Double>>> conditionalProbabilities = new HashMap<>();
    private Map<String, Long> labelCount = new HashMap<>();
    private Map<Integer, Set<String>> attributeValues = new HashMap<>(); // przechowywanie unikalnych wartości dla każdego atrybutu i wykorzystujesz te informacje podczas obliczania prawdopodobieństw z wygładzaniem Laplace'a.

    public void train(List<List<String>> features, List<String> labels, Map<Integer, Set<String>> attributeValues) {
        this.attributeValues = attributeValues; // Przechowujemy wartości atrybutów dla wykorzystania w predykcji
        labels.forEach(label -> labelCount.put(label, labelCount.getOrDefault(label, 0L) + 1));
        long total = labels.size();

        // Calculate prior probabilities
        labelCount.forEach((label, count) -> priorProbabilities.put(label, (double) count / total));

        // Calculate conditional probabilities with Laplace smoothing
        attributeValues.forEach((index, values) -> {
            Map<String, Map<String, Double>> valueMap = new HashMap<>();
            for (String value : values) {
                Map<String, Double> labelMap = new HashMap<>();
                for (String label : labelCount.keySet()) {
                    long count = 0;
                    for (int i = 0; i < labels.size(); i++) {
                        if (features.get(i).get(index).equals(value) && labels.get(i).equals(label)) {
                            count++;
                        }
                    }
                    labelMap.put(label, (count + 1.0) / (labelCount.get(label) + values.size()));
                }
                valueMap.put(value, labelMap);
            }
            conditionalProbabilities.put(index, valueMap);
        });
    }

    public String predict(List<String> test) {
        Map<String, Double> labelScores = new HashMap<>(priorProbabilities);

        for (int i = 0; i < test.size(); i++) {
            final int index = i; // Zmienna finalna do użycia w lambda
            String featureValue = test.get(index);
            labelScores.keySet().forEach(label -> {
                labelScores.put(label, labelScores.get(label) *
                        conditionalProbabilities.get(index).getOrDefault(featureValue, new HashMap<>())
                                .getOrDefault(label, 1.0 / (labelCount.get(label) + attributeValues.get(index).size())));
            });
        }

        return Collections.max(labelScores.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

}
