import java.util.*;
public class CrystalStructure {
    public static void main(String[] args) {

        // Input of crystal dimensions and number of wind tests
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the maximum width, height, and wind count of the crystal separated by spaces (e.g., 5 4 2):");
        int[] numbers = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        int[][] winds = new int[numbers[2]][];
        for (int i = 0; i < numbers[2]; i++) {
            System.out.println("Please input wind " + (i + 1) + " as a sequence of integers separated by spaces (e.g., 1 0 6 1 1 4 1 2 2 5 2 2 3 3 4):");
            winds[i] = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }

        // int[] numbers = {5, 4, 2};
        // int[][] winds = {
        //     {1, 0, 6, 1, 1, 4, 1, 2, 2, 5, 2, 2, 3, 3, 4},
        //     {0, -1, 7, 1, 1, 4, 1, 5, 2, 2, 3, 3, 4, 4, 4, 5, 4}
        // };

        char[][] crystalMin = new char[numbers[0]][numbers[1]];
        char[][] crystalMax = new char[numbers[0]][numbers[1]];
        for (char[] row : crystalMin) Arrays.fill(row, '.');
        for (char[] row : crystalMax) Arrays.fill(row, '.');

        // Extract boundaries from each wind and place them in a list of sets
        List<Set<int[]>> sets = new ArrayList<>();
        for (int[] wind : winds) {
            Set<int[]> mySet = new HashSet<>();
            for (int i = 0; i < wind[2]; i++) {
                mySet.add(new int[]{wind[3 + 2 * i], wind[4 + 2 * i]});
            }
            sets.add(mySet);
        }

        // First, place boundaries into crystals
        for (Set<int[]> set : sets) {
            for (int[] boundary : set) {
                crystalMin[boundary[0] - 1][boundary[1] - 1] = '#';
                crystalMax[boundary[0] - 1][boundary[1] - 1] = '#';
            }
        }

        // Minimal crystal structure
        List<Integer> range = new ArrayList<>();
        for (int i = 0; i < numbers[2]; i++) {
            range.add(i);
        }

        for (int index = 0; index < sets.size(); index++) {
            Set<int[]> seti = sets.get(index);
            for (int[] boundary : seti) {
                for (int i : range) {
                    if (i != index && !contains(sets.get(i), boundary)) {
                        int b0 = boundary[0] - winds[i][0];
                        int b1 = boundary[1] - winds[i][1];
                        if (b0 > 0 && b0 <= numbers[0] && b1 > 0 && b1 <= numbers[1]) {
                            crystalMin[b0 - 1][b1 - 1] = '#';
                        }
                    }
                }
            }
        }

        crystalMax = copyCrystal(crystalMin);

        for (int index = 0; index < sets.size(); index++) {
            Set<int[]> theSet = sets.get(index);
            for (int[] boundary : theSet) {
                int b0 = boundary[0] - winds[index][0];
                int b1 = boundary[1] - winds[index][1];
                if (b0 > 0 && b0 <= numbers[0] && b1 > 0 && b1 <= numbers[1]) {
                    crystalMax[b0 - 1][b1 - 1] = 'e';
                }
            }
        }

        for (int i = 0; i < numbers[0]; i++) {
            for (int j = 0; j < numbers[1]; j++) {
                if (crystalMax[i][j] == '.') {
                    int cri = 0;
                    for (int k = 0; k < numbers[2]; k++) {
                        int c0 = i + 1 - winds[k][0];
                        int c1 = j + 1 - winds[k][1];
                        if (!(c0 > 0 && c0 <= numbers[0] && c1 > 0 && c1 <= numbers[1]) || crystalMax[c0 - 1][c1 - 1] != '#') {
                            cri++;
                        }
                    }
                    if (cri >= 3) {
                        crystalMax[i][j] = 'e';
                    }
                }
            }
        }

        for (int i = 0; i < numbers[0]; i++) {
            for (int j = 0; j < numbers[1]; j++) {
                if (crystalMax[i][j] == '.') crystalMax[i][j] = '#';
                else if (crystalMax[i][j] == 'e') crystalMax[i][j] = '.';
            }
        }

        printCrystal(transpose(crystalMin));
        System.out.println();
        printCrystal(transpose(crystalMax));
    }

    private static boolean contains(Set<int[]> set, int[] boundary) {
        for (int[] item : set) {
            if (Arrays.equals(item, boundary)) {
                return true;
            }
        }
        return false;
    }

    private static char[][] copyCrystal(char[][] crystal) {
        char[][] copy = new char[crystal.length][crystal[0].length];
        for (int i = 0; i < crystal.length; i++) {
            System.arraycopy(crystal[i], 0, copy[i], 0, crystal[i].length);
        }
        return copy;
    }

    private static char[][] transpose(char[][] matrix) {
        char[][] transposed = new char[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    private static void printCrystal(char[][] crystal) {
        for (char[] row : crystal) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
}
