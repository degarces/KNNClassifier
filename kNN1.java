import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class kNN1 {
    public static void main(String[] args) {
        // Step 1: Load Data
        List<double[]> trainingData = loadData("train_data.txt");
        List<Integer> trainingLabels = loadLabels("train_label.txt");
        List<double[]> testData = loadData("test_data.txt");

        // Step 2: Perform kNN Classification
        List<Integer> preLabels = new ArrayList<>();
        for (double[] testInstance : testData) {
            int predictedLabel = classify(testInstance, trainingData, trainingLabels);
            preLabels.add(predictedLabel);
        }

        // Step 3: Compute Classification Accuracy
        int predictions = 0;
        for (int i = 0; i < testData.size(); i++) {
            // Compare predicted labels with actual labels
            if (preLabels.get(i).equals(loadLabels("test_label.txt").get(i))) {
                predictions++;
            }
        }
        double accuracy = (double) predictions / testData.size() * 100;

        System.out.println("Classification Accuracy: " + accuracy + "%");

        // Saves predicted labels to output1.txt
        savepreLabels("output1.txt", preLabels);
    }

    // loads data from a file and return a list of double arrays
    public static List<double[]> loadData(String fileName) {
        List<double[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into values and convert them to doubles
                String[] values = line.trim().split("\\s+");
                double[] instance = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    instance[i] = Double.parseDouble(values[i]);
                }
                data.add(instance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Method to load labels from a file and return a list of integers
    public static List<Integer> loadLabels(String fileName) {
        List<Integer> labels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into strings and convert to integers
                String[] labelStrings = line.trim().split("\\s+");
                for (String labelString : labelStrings) {
                    labels.add(Integer.parseInt(labelString));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labels;
    }

    // Method to perform kNN classification on an instance
    public static int classify(double[] testInstance, List<double[]> trainingData, List<Integer> trainingLabels) {
        double[] distances = new double[trainingData.size()];
        
        // Calculates distances between the test instance and each training instance
        for (int i = 0; i < trainingData.size(); i++) {
            distances[i] = Distance(testInstance, trainingData.get(i));
        }

        // Find index of the minimum distance
        int minIndex = 0;
        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < distances[minIndex]) {
                minIndex = i;
            }
        }
        return trainingLabels.get(minIndex);
    }

    // Method to calculate Euclidean distance between two instances
    private static double Distance(double[] instance1, double[] instance2) {
        double sum = 0.0;
        for (int i = 0; i < instance1.length; i++) {
            sum += Math.pow(instance1[i] - instance2[i], 2);
        }
        return Math.sqrt(sum);
    }

    // Method to perform kNN classification on all test instances
    public static List<Integer> classifyAll(List<double[]> testData, List<double[]> trainingData, List<Integer> trainingLabels, int k) {
        List<Integer> testPatterns = new ArrayList<>();

        for (double[] testInstance : testData) {
            int predictedLabel = classify(testInstance, trainingData, trainingLabels);
            testPatterns.add(predictedLabel);
        }

        return testPatterns;
    }

    // Method to save predicted labels to a file
    private static void savepreLabels(String fileName, List<Integer> preLabels) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write predicted labels to the file
            for (int label : preLabels) {
                writer.write(label + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
