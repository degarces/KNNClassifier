import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class kNN2 {
    public static void main(String[] args) {
        // Load training data and labels
        List<double[]> trainData = kNN1.loadData("train_data.txt");
        List<Integer> trainLabels = kNN1.loadLabels("train_label.txt");

        // Load test data
        List<double[]> testData = kNN1.loadData("test_data.txt");

        // Perform kNN Classification with improvements
        List<Integer> testPatterns = betterClassify(testData, trainData, trainLabels);

        // Compute and print Classification Accuracy
        double accuracy = computeAccuracy(testPatterns, kNN1.loadLabels("test_label.txt"));
        System.out.println("Improved Classification Accuracy: " + accuracy + "%");

        // Save Predicted Labels to output2.txt
        savetestPatterns("output2.txt", testPatterns);
    }

    // Method for improved classification using kNN
    private static List<Integer> betterClassify(List<double[]> testData, List<double[]> trainData, List<Integer> trainLabels) {
        // Apply feature scaling to both training and test data
        featureScaling(trainData);
        featureScaling(testData);

        // Set the value of k for kNN classification
        int k = 1;

        // Perform kNN classification on test data
        return kNN1.classifyAll(testData, trainData, trainLabels, k);
    }

    // Method for feature scaling
    private static void featureScaling(List<double[]> data) {
        // Get the number of features in the data
        int num = data.get(0).length;

        // Arrays to store minimum and maximum values for each feature
        double[] mins = new double[num];
        double[] maxs = new double[num];

        // Find min and max values for each feature
        for (int i = 0; i < num; i++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (double[] instance : data) {
                double value = instance[i];
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            mins[i] = min;
            maxs[i] = max;
        }

        // Apply min-max scaling to the data
        data.replaceAll(instance -> {
            for (int i = 0; i < num; i++) {
                double value = instance[i];
                double min = mins[i];
                double max = maxs[i];
                double scaledValue = (value - min) / (max - min);
                instance[i] = scaledValue;
            }
            return instance;
        });
    }

    // Method to compute classification accuracy
    private static double computeAccuracy(List<Integer> testPatterns, List<Integer> actualLabels) {
        int correctPredictions = 0;
        for (int i = 0; i < testPatterns.size(); i++) {
            if (testPatterns.get(i).equals(actualLabels.get(i))) {
                correctPredictions++;
            }
        }
        return (double) correctPredictions / testPatterns.size() * 100;
    }

    // Method to save predicted labels to a file
    private static void savetestPatterns(String fileName, List<Integer> testPatterns) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write predicted labels to the file
            for (int label : testPatterns) {
                writer.write(label + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
