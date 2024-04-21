import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        DataLoader loader = new DataLoader("/Users/vasylwecek/Downloads/trainingset.csv");
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();

        try {
            loader.loadData();
            classifier.train(loader.getFeatures(), loader.getLabels(), loader.getAttributeValues());
            System.out.println("Model trained successfully.");

            //Mechanizm ręcznego wprowadzania danych
            Scanner scanner = new Scanner(System.in); // odczyt danych wejściowych z konsoli.
            while (true) {
                System.out.println("\nEnter test data (comma-separated), or type 'exit' to quit:");
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }

                List<String> testData = Arrays.asList(input.split(","));
                //Lista stringów (testData) jest przekazywana do metody predict klasyfikatora Bayesa (NaiveBayesClassifier),
                //która dokonuje klasyfikacji na podstawie wcześniej wyuczonego modelu i zwraca przewidywane etykiety.
                System.out.println("Prediction: " + classifier.predict(testData));
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Failed to load data: " + e.getMessage());
        }
    }
}
