import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class Matrix {
    private double[][] array;
    private int rowsCount;
    private int colsCount;
    Scanner scanner = new Scanner(System.in);

    public Matrix(){}

    public Matrix(int rowsCount, int colsCount, double[][] array){
        this.rowsCount = rowsCount;
        this.colsCount = colsCount;
        this.array = array;
    }
    public void inputMatrix() {

        System.out.println("Введите количество строк в матрице: ");
        rowsCount = scanner.nextInt();
        System.out.println("Введите количество столбцов в матрице: ");
        colsCount = scanner.nextInt();
        array = new double[rowsCount][colsCount];

        System.out.println("Как вы хотите ввести матрицу: по элементу или по строке? Введите \"элемент\" или \"строка\" ");
        String inputMethod = scanner.next();

        if (inputMethod.equalsIgnoreCase("element")) {
            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < colsCount; j++) {
                    System.out.println("Введите элемент в позиции [" + i + "][" + j + "]:");
                    array[i][j] = scanner.nextDouble();
                }
            }
        } else if (inputMethod.equalsIgnoreCase("row")) {
            for (int i = 0; i < rowsCount; i++) {
                System.out.println("Введите значения для строки " + (i+1) + ", разделенные пробелами:");
                for (int j = 0; j < colsCount; j++) {
                    array[i][j] = scanner.nextDouble();
                }
            }
        }
    }

    public void printMatrix() {
        System.out.println("Matrix (" + rowsCount + " * " + colsCount + "): ");
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Matrix multiplyMatrix(Matrix otherMatrix) {
        if (this.colsCount != otherMatrix.rowsCount) {
            System.out.println("Умножение матриц A*B невозможно, но возможно B*A. Хотите умножить матрицы в обратном порядке? (да/нет)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("да") || answer.equals("yes") || answer.equals("д") || answer.equals("y")) {
                if (this.rowsCount == otherMatrix.colsCount) {
                    // умножение матриц в обратном порядке
                    double[][] result = new double[otherMatrix.rowsCount][this.colsCount];
                    for (int i = 0; i < otherMatrix.rowsCount; i++) {
                        for (int j = 0; j < this.colsCount; j++) {
                            for (int k = 0; k < this.rowsCount; k++) {
                                result[i][j] += otherMatrix.array[i][k] * this.array[k][j];
                            }
                        }
                    }
                    return new Matrix(otherMatrix.rowsCount, this.colsCount, result);
                } else {
                    System.out.println("Умножение матриц в обратном порядке также невозможно");
                    return null;
                }
            } else {
                return null;
            }
        }

        // умножение матриц A*B
        double[][] result = new double[this.rowsCount][otherMatrix.colsCount];
        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < otherMatrix.colsCount; j++) {
                for (int k = 0; k < this.colsCount; k++) {
                    result[i][j] += this.array[i][k] * otherMatrix.array[k][j];
                }
            }
        }
        return new Matrix(this.rowsCount, otherMatrix.colsCount, result);
    }
    //метод генерирующий порядок минора для последующего нахождения определителя матрицы
    public Matrix generateSubMatrix(int delrow, int delcol){
        double[][] m = new double[rowsCount - 1][colsCount -1];
        int i1 = 0;
        for(int i = 0; i<rowsCount; i++){
            if (i == delrow){
                continue;
            }
            int j1 = 0;
            for(int j = 0; j< colsCount; j++){
                if (j == delcol){
                    continue;
                }
                m[i1][j1] = array[i][j];
                j1++;
            }
            i1++;
        }
        return new Matrix(rowsCount -1, colsCount -1, m);
    }
    //метод находящий определители, которые понадобятся для нахождения ранга матрицы
    public double Det(){
        double res = 0;
        if (colsCount != rowsCount){
            return res;
        }
        if (colsCount == 1){
            return array[0][0];
        }
        else if(colsCount == 2){
            return (array[0][0]*array[1][1] - array[0][1] * array[1][0]);
        }
        else {
            res = 0;
            for (int i = 0; i < rowsCount; i++) {
                res += Math.pow(-1, i) * array[0][i] * this.generateSubMatrix(0, i).Det();
            }
        }
        return res;
    }

    //метод находящий ранг матрицы

    public int FindRank(){
        int minDimension = Math.min(rowsCount, colsCount);
        int rank = 0;
        for(int i = 1; i <= minDimension; i++){
            ArrayList<Matrix> submatrices = returnAllSubMatrix(i);
            for(int j = 0; j < submatrices.size(); j++){
                if (Math.abs(submatrices.get(j).Det()) != 0){ // Проверка определителя на "очень маленькое" значение
                    rank++;
                    break;
                }
            }
        }
        return rank;
    }


    public ArrayList<Matrix> returnAllSubMatrix(int n) {
        ArrayList<Matrix> submatrices = new ArrayList<>();

        for (int i = 0; i <= rowsCount - n; i++) {
            for (int j = 0; j <= colsCount - n; j++) {
                double[][] subarray = new double[n][n];
                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < n; l++) {
                        subarray[k][l] = array[i + k][j + l];
                    }
                }
                Matrix submatrix = new Matrix(n,n,subarray);
                submatrices.add(submatrix);
            }
        }

        return submatrices;
    }
}



