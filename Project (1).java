import java.util.*;

public class Project {

    public static void main(String[] args) {
        int n = 4; 
        int k = 2;
        int trials = 1000000; 

        Map<String, Integer> bitStringCount = new HashMap<>();
        Map<Integer, Integer> onesCountDistribution = new HashMap<>();

        for (int i = 0; i < trials; i++) {
            String bitString = generateUniformBitString(n, k);

            bitStringCount.put(bitString, bitStringCount.getOrDefault(bitString, 0) + 1);

            int onesCount = countOnes(bitString);
            onesCountDistribution.put(onesCount, onesCountDistribution.getOrDefault(onesCount, 0) + 1);
        }

        System.out.println("Each bit string count:");
        for (Map.Entry<String, Integer> entry : bitStringCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " times");
        }

        System.out.println("\nRandom post-production distribution based on the number 1:");
        for (Map.Entry<Integer, Integer> entry : onesCountDistribution.entrySet()) {
            System.out.println(entry.getKey() + " 1s: " + entry.getValue() + " times");
        }
    }


    // Binomiyal katsayısı, n öğeden k öğe seçmenin kaç farklı yolu olduğunu gösterir , klasik kombinasyon C(n,k)
    public static long binomialCoefficient(int n, int k) {
        if (k > n) 
        return 0;
        if (k == 0 || k == n) 
        return 1;
        
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result *= (n - (k - i));
            result /= i;
        }
        return result;
    }

    // Ağırlıklı olarak rastgele seçim yapan fonksiyon
    public static int getWeightedRandomChoice(double[] probabilities, Random random) {
        //n= 4 k = 2 icin  1 4 6 icin 1/11 0 gelme agirligi 4/11 1 gelme agirligi 6/11 2 gelme agirligi
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand < cumulativeProbability) {
                return i;
            }
        }
        return probabilities.length - 1;
    }

    // Her bir olasılığı hesaplayarak uygun bit dizisini döndürür
    public static String generateUniformBitString(int n, int k) {
        Random random = new Random();
        
        
        double[] probabilities = new double[k + 1];
        double totalCombinations = 0;
        
        for (int i = 0; i <= k; i++) {
            long combinations = binomialCoefficient(n, i);
            probabilities[i] = combinations;
            totalCombinations += combinations;
        }

        for (int i = 0; i <= k; i++) {
            probabilities[i] /= totalCombinations;
        }
        
        int selectedOnes = getWeightedRandomChoice(probabilities, random);

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            positions.add(i);
        }

            Collections.shuffle(positions);// pozisyonlari karistirdik
            Set<Integer> onesPositions = new HashSet<>(positions.subList(0, selectedOnes)); // 1 lerin sayisi kadar 1 yerlestirir ilk k elemanina

        StringBuilder bitString = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (onesPositions.contains(i)) {
                bitString.append("1");
            } else {
                bitString.append("0");
            }
        }

        return bitString.toString();
    }


    public static int countOnes(String bitString) {
        int count = 0;
        for (int i = 0; i<bitString.length(); i++) {
            if (bitString.charAt(i) == '1')
             count++;
        }
        return count;
    }
}
