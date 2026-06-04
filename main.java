public final class MatrixProcessor {

  private MatrixProcessor() {}

  public static void main(String[] args) {
    try {
      char[][] matrixA = {
        {'A', 'C', 'E'},
        {'G', 'I', 'K'},
      };

      char[][] matrixB = {
        {1, 2, 3},
        {4, 5, 6},
      };

      System.out.println("Matrix A:");
      printMatrix(matrixA);
      System.out.println("Matrix B:");
      printMatrix(matrixB);

      char[][] matrixC = add(matrixA, matrixB);
      System.out.println("Result of action 1 (C = A + B):");
      printMatrix(matrixC);

      long columnMaximaSum = sumOfColumnMaxima(matrixC);
      System.out.println("Result of action 2 "
          + "(sum of the largest elements of each column of C): "
          + columnMaximaSum);
    } catch (IllegalArgumentException | NullPointerException e) {
      System.err.println("Помилка обробки: " + e.getMessage());
    }
  }

  private static char[][] add(char[][] a, char[][] b) {
    validate(a, "A");
    validate(b, "B");

    int rows = a.length;
    int cols = a[0].length;
    if (b.length != rows || b[0].length != cols) {
      throw new IllegalArgumentException(
          "Матриці A і B повинні мати однакові розміри.");
    }

    char[][] result = new char[rows][cols];
    for (int i = 0; i < rows; i++) {
      if (a[i] == null || b[i] == null) {
        throw new NullPointerException("Рядок " + i + " дорівнює null.");
      }
      if (a[i].length != cols || b[i].length != cols) {
        throw new IllegalArgumentException(
            "Кожна матриця повинна бути прямокутною (рядок " + i
                + " має неправильну довжину).");
      }
      for (int j = 0; j < cols; j++) {
        int sum = a[i][j] + b[i][j];
        if (sum > Character.MAX_VALUE) {
          throw new IllegalArgumentException(
              "Сума в позиції [" + i + "][" + j
                  + "] виходить за межі діапазону char.");
        }
        result[i][j] = (char) sum;
      }
    }
    return result;
  }

  private static long sumOfColumnMaxima(char[][] matrix) {
    validate(matrix, "C");

    int cols = matrix[0].length;
    long total = 0L;
    for (int j = 0; j < cols; j++) {
      char columnMax = matrix[0][j];
      for (int i = 1; i < matrix.length; i++) {
        if (matrix[i] == null || matrix[i].length != cols) {
          throw new IllegalArgumentException(
              "Матриця C повинна бути прямокутною (рядок " + i + ").");
        }
        if (matrix[i][j] > columnMax) {
          columnMax = matrix[i][j];
        }
      }
      total += columnMax;
    }
    return total;
  }

  private static void validate(char[][] matrix, String name) {
    if (matrix == null) {
      throw new NullPointerException("Матриця " + name + " дорівнює null.");
    }
    if (matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
      throw new IllegalArgumentException("Матриця " + name + " порожня.");
    }
  }

  private static void printMatrix(char[][] matrix) {
    for (char[] row : matrix) {
      StringBuilder line = new StringBuilder();
      for (char value : row) {
        line.append(String.format("'%c'(%3d) ", value, (int) value));
      }
      System.out.println(line.toString().trim());
    }
    System.out.println();
  }
}