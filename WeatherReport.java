import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class Temperature {
    String city;
    String state;
    int lowTemperature;
    int highTemperature;

    public Temperature(String city, String state, int lowTemperature, int highTemperature) {
        this.city = city;
        this.state = state;
        this.lowTemperature = lowTemperature;
        this.highTemperature = highTemperature;
    }

    public int differential() {
        return highTemperature - lowTemperature;
    }
    public String getCity() {
        return city;
    }

    public int getHighTemperature() {
        return highTemperature;
    }
}

public class WeatherReport {
    List<Temperature> temperatures;

    // Constructor with no parameters
    public WeatherReport() {
        temperatures = generateTestTemperatures();
    }

    // Constructor with one String parameter for file name
    public WeatherReport(String fileName) {
        temperatures = readTemperaturesFromFile(fileName);
    }

    // Helper method to generate test temperatures
    private List<Temperature> generateTestTemperatures() {
    	List<Temperature> temps = new LinkedList<>();
    	 temps.add(new Temperature("City1", "State1", 20, 30));
         temps.add(new Temperature("City2", "State2", 25, 35));
         temps.add(new Temperature("City3", "State3", 18, 28));
        // Create and return a list of test Temperature objects
        // ...
         return temps;
    }

    // Helper method to read temperatures from file
    private List<Temperature> readTemperaturesFromFile(String fileName) {
        List<Temperature> temps = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Skip header row

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String city = data[1];
                String state = data[10];
                int lowTemperature = Integer.parseInt(data[6]);
                int highTemperature = Integer.parseInt(data[5]);

                temps.add(new Temperature(city, state, lowTemperature, highTemperature));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temps;
    }
    public boolean isSortedByCity() {
        for (int i = 0; i < temperatures.size() - 1; i++) {
            String currentCity = temperatures.get(i).getCity();
            String nextCity = temperatures.get(i + 1).getCity();

            if (currentCity.compareTo(nextCity) > 0) {
                return false; // Not sorted by city
            }
        }
        return true; // Sorted by city
    }

    public boolean isSortedByHigh() {
        for (int i = 0; i < temperatures.size() - 1; i++) {
            int currentHigh = temperatures.get(i).getHighTemperature();
            int nextHigh = temperatures.get(i + 1).getHighTemperature();

            if (currentHigh > nextHigh) {
                return false; // Not sorted by high temperature
            }
        }
        return true; // Sorted by high temperature
    }


    public void sortWithCollections(String by) {
        switch (by) {
            case "City":
                Collections.sort(temperatures, Comparator.comparing(Temperature::getCity));
                break;
            case "High":
                Collections.sort(temperatures, Comparator.comparingInt(Temperature::getHighTemperature));
                break;
            default:
                System.out.println("Invalid sorting criteria. Supported values are 'City' and 'High'.");
        }
    }
    public void printTemperatures() {
        System.out.println("Temperatures:");
        for (Temperature temp : temperatures) {
            System.out.println("City: " + temp.getCity() +
                               ", State: " + temp.state +
                               ", Low: " + temp.lowTemperature +
                               ", High: " + temp.highTemperature);
        }
    }


   
    public void sortWithMerge(String by) {
        switch (by) {
            case "City":
                mergeSort(temperatures, Comparator.comparing(Temperature::getCity));
                break;
            case "High":
                mergeSort(temperatures, Comparator.comparingInt(Temperature::getHighTemperature));
                break;
            default:
                System.out.println("Invalid sorting criteria. Supported values are 'City' and 'High'.");
        }
    }

    private <T> void mergeSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return;
        }

        int middle = list.size() / 2;
        List<T> left = new LinkedList<>(list.subList(0, middle));
        List<T> right = new LinkedList<>(list.subList(middle, list.size()));

        mergeSort(left, comparator);
        mergeSort(right, comparator);

        merge(list, left, right, comparator);
    }

    private <T> void merge(List<T> list, List<T> left, List<T> right, Comparator<T> comparator) {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) < 0) {
                list.set(currentIndex, left.get(leftIndex));
                leftIndex++;
            } else {
                list.set(currentIndex, right.get(rightIndex));
                rightIndex++;
            }
            currentIndex++;
        }

        while (leftIndex < left.size()) {
            list.set(currentIndex, left.get(leftIndex));
            leftIndex++;
            currentIndex++;
        }

        while (rightIndex < right.size()) {
            list.set(currentIndex, right.get(rightIndex));
            rightIndex++;
            currentIndex++;
        }
    }
 

        public static void main(String[] args) {
            try {
                // Create a WeatherReport object with data from the file
                WeatherReport report = new WeatherReport("weather.txt");

                // Test and time sorting by city using Collections.sort()
                long startTimeCollectionsCity = System.nanoTime();
                if (!report.isSortedByCity()) {
                    report.sortWithCollections("City");
                }
                long endTimeCollectionsCity = System.nanoTime();
                System.out.println("Time taken to sort by city with Collections.sort(): "
                        + (endTimeCollectionsCity - startTimeCollectionsCity) + " ns");

                // Test and time sorting by high temperature using Collections.sort()
                long startTimeCollectionsHigh = System.nanoTime();
                if (!report.isSortedByHigh()) {
                    report.sortWithCollections("High");
                }
                long endTimeCollectionsHigh = System.nanoTime();
                System.out.println("Time taken to sort by high temperature with Collections.sort(): "
                        + (endTimeCollectionsHigh - startTimeCollectionsHigh) + " ns");

                // Create a new WeatherReport object for testing merge sort
                WeatherReport mergeSortReport = new WeatherReport("weather.txt");

                // Test and time sorting by city using merge sort
                long startTimeMergeSortCity = System.nanoTime();
                if (!mergeSortReport.isSortedByCity()) {
                    mergeSortReport.sortWithMerge("City");
                }
                long endTimeMergeSortCity = System.nanoTime();
                System.out.println("Time taken to sort by city with merge sort: "
                        + (endTimeMergeSortCity - startTimeMergeSortCity) + " ns");

                // Test and time sorting by high temperature using merge sort
                long startTimeMergeSortHigh = System.nanoTime();
                if (!mergeSortReport.isSortedByHigh()) {
                    mergeSortReport.sortWithMerge("High");
                }
                long endTimeMergeSortHigh = System.nanoTime();
                System.out.println("Time taken to sort by high temperature with merge sort: "
                        + (endTimeMergeSortHigh - startTimeMergeSortHigh) + " ns");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
