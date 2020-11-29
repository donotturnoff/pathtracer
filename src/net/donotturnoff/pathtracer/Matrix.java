package net.donotturnoff.pathtracer;

public class Matrix {
	
	protected double[][] array;
	
	public Matrix() {
		setArray(new double[0][0]);
	}
	
	public Matrix(double[][] array) {
		setArray(array);
	}
	
	public Matrix(Vector vector) {
		setArray(vector);
	}
	
	public Matrix(Rotation rotation) {
		setArray(rotation);
	}
	
	public final void setArray(double[][] array) throws IllegalArgumentException {
		if (array.length > 0) {
			double[] firstRow = array[0];
			for (int i = 1; i < array.length; i++) {
				if (array[i].length != firstRow.length) {
					throw new IllegalArgumentException("Matrix must be rectangular.");
				} 
			}
		}
		this.array = array;
	}
	
	public final void setArray(Vector vector) {
		array = new double[vector.components()][1];
		for (int i = 0; i < vector.components(); i++) {
			array[i] = new double[]{vector.getComponent(i)};
		}
	}
	
	public final void setArray(Rotation rotation) {
		double x = rotation.getX();
		double y = rotation.getY();
		double z = rotation.getZ();
		
		/* Calculate sin and cos of x, y and z once to improve
		 * efficiency and readability. */
		
		final double cx = Math.cos(x);
		final double sx = Math.sin(x);
		final double cy = Math.cos(y);
		final double sy = Math.sin(y);
		final double cz = Math.cos(z);
		final double sz = Math.sin(z);
		
		/* Matrix formed by applying component rotation matrices in the
		 * order x -> z -> y. */
		
		this.array = new double[][]{ //xzy
			{cy*cz, -sz, cz*sy},
			{cx*cy*sz+sx*sy, cx*cz, cx*sy*sz-cy*sx},
			{cy*sx*sz-cx*sy, cz*sx, cx*cy+sy*sx*sz},
		};
	}
	
	public final double[][] getArray() {
		return array;
	}
	
	private boolean validIndex(int row, int column) {
		return (validRow(row) && validColumn(column));
	}
	
	private boolean validRow(int row) {
		return ((row < rows()) && (row >= 0));
	}
	
	private boolean validColumn(int column) {
		return ((column < columns()) && (column >= 0));
	}
	
	public final void setElement(int row, int column, double value) throws IllegalArgumentException {
		if (!validRow(row)) {
			throw new IllegalArgumentException("Illegal row index: " + row);
		} else if (!validColumn(column)) {
			throw new IllegalArgumentException("Illegal column index: " + column);
		} else {
			array[row][column] = value;
		}
	}
	
	public final double getElement(int row, int column) throws IllegalArgumentException {
		if (!validRow(row)) {
			throw new IllegalArgumentException("Illegal row index: " + row);
		} else if (!validColumn(column)) {
			throw new IllegalArgumentException("Illegal column index: " + column);
		} else {
			return array[row][column];
		}
	}
	
	public final int rows() {
		return array.length;
	}
	
	public final int columns() {
		
		if (rows() > 0) {
			return array[0].length;
		} else {
			return 0;
		}
	}
	
	public final int[] size() {
		return new int[]{rows(), columns()};
	}
	
	public final boolean equals(Matrix comparator) {
		final int rows = rows();
		final int columns = columns();
		
		double[][] comparatorArray = comparator.getArray();
		
		/* First check if matrices are conformable for addition 
		 * (this means they are the same size). */
		if (conformableForAddition(comparator)) {
			
			/* Iterate over every element and compare for equality.
			 * As soon as any differ, return false. */
			for (int row = 0; row < rows; row++) { 
				for (int column = 0; column < columns; column++) { 
					if (array[row][column] != comparatorArray[row][column]) {
						return false;
					} 
				}
			}
			
			/* At this point, all the conditions for equality have been met. */
			return true;
			
		} else {
			return false;
		}
	}
	
	public final Matrix transpose() {
		final int rows = rows();
		final int columns = columns();
		
		if (rows == 0) {
			return new Matrix();
		} else {
			/* Transpose has same amount of columns as original has rows,
			 * and vice versa. */		
			double[][] transposeArray = new double[columns][rows]; 
			
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
					/* Swap each element's row and column value when 
					 * populating the new array. */
					transposeArray[column][row] = array[row][column];
				}
			}
			return new Matrix(transposeArray);
		}
	}
	
	public final Matrix sum(Matrix addend) throws IllegalArgumentException {
		if (conformableForAddition(addend)) {
			final int rows = rows();
			final int columns = columns();
			final double[][] addendArray = addend.getArray();
			double[][] resultArray = new double[rows][columns]; 
			
			/* Iterate over each element in each matrix
			 * and store the sum in the result array. */
			
			for (int row = 0; row < rows; row++) { 
				for (int column = 0; column < columns; column++) {
					resultArray[row][column] = array[row][column] + addendArray[row][column];
				}
			}
			return new Matrix(resultArray);
		} else {
			throw new IllegalArgumentException("Matrices are not conformable for addition");
		}
	}
	
	public final Vector product(Vector vector) throws IllegalArgumentException {
		Matrix multiplicand = new Matrix(vector);
		
		if (conformableForMultiplication(multiplicand)) {
			Matrix product = this.product(multiplicand);
			return new Vector(product);
		} else {
			throw new IllegalArgumentException("Matrix and vector are not conformable for multiplication");
		}
	}
	
	public final Matrix product(Matrix multiplicand) {
		
		if (conformableForMultiplication(multiplicand)) {
			final int rows1 = rows();
			final int columns1 = columns();
			final int columns2 = multiplicand.columns();
			final double[][] multiplicandArray = multiplicand.getArray();
			
			/* Result matrix has the same amount of rows as the first matrix and the
			 * same amount of columns as the second matrix. */
			double[][] resultArray = new double[rows1][columns2];
			
			/* Iterate over each row in the first matrix. Add the product of each 
			 * element with the corresponding element in the second matrix to the
			 * corresponding element in the result matrix. This follows the rules
			 * of matrix multiplication, of "combining" rows and columns. */
			for (int row1 = 0; row1 < rows1; row1++) {
				for (int col2 = 0; col2 < columns2; col2++) {
					for (int col1 = 0; col1 < columns1; col1++) {
						resultArray[row1][col2] += (array[row1][col1] * multiplicandArray[col1][col2]);
					}
				}
			}
			
			return new Matrix(resultArray);
		} else {
			throw new IllegalArgumentException("Matrices are not conformable for multiplication");
		}
	}
	
	public final Matrix product(double multiplicand) {
		final int rows = rows();
		final int columns = columns();
		
		double[][] resultArray = new double[rows][columns];
		
		/* Iterate over each element and multiply it by the scalar argument. */
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				resultArray[row][column] = array[row][column] * multiplicand;
			}
		}
		
		return new Matrix(resultArray);
	}
	
	public final boolean conformableForAddition(Matrix addend) {
		
		/* Two matrices must be the same size to be conformable for addition. */
		return ((rows() == addend.rows()) && (columns() == addend.columns()));
	}
	
	public final boolean conformableForMultiplication(Matrix multiplicand) {
		
		/* The first matrix must have the same amount of columns as 
		 * the second has rows to be conformable for multiplication. */
		return (columns() == multiplicand.rows());
	}
	
	public final double determinant() throws UnsupportedOperationException {
		if (isSquare()) {
			double runningTotal = 0;
			final int rows = rows();
			final int columns = columns();

			if (rows == 0 || columns == 0) {

				/* Handle empty matrix */
				return 1.0;

			} else if (rows == 1 && columns == 1) {

				/* Base case of recursion - the determinant of a 1x1 matrix is the
				 * only element in that matrix. */
				return array[0][0];

			} else {

				/* Use the Leibniz formula to calculate the determinant by
				 * recursively determining determinants of minors. */
				for (int column = 0; column < columns; column++) {

					/* For every element in the first row, calculate the determinant
					 * of the minor and multiply by the current element. */
					final double minorDeterminant = minor(0, column).determinant();
					final double newTerm = array[0][column] * minorDeterminant;

					/* Alternately add and subtract the new term from the
					 * running total. */
					if (column % 2 == 0) {
						runningTotal += newTerm;
					} else {
						runningTotal -= newTerm;
					}
				}

				return runningTotal;
			}
		} else {
			throw new UnsupportedOperationException("Cannot find determinant of non-square matrix");
		}
	}
	
	public final Matrix minor(int omittedRow, int omittedColumn) {
		
		/* The minor matrix will have one less row and column than 
		 * the original. */
		double[][] newArray = new double[rows()-1][columns()-1];
		
		/* This keeps track of the new row index. */
		int newRow = 0;
		
		for (int row = 0; row < rows(); row++) {
			
			if (row != omittedRow) {
				
				/* This keeps track of the new column index. */
				int newColumn = 0;
				
				for (int column = 0; column < columns(); column++) {
					
					if (column != omittedColumn) {
						
						/* The new matrix is only updated if the current row and
						 * column aren't the ones which were to be omitted. */
						newArray[newRow][newColumn] = array[row][column];
						newColumn++;
					}
					
				}
				newRow++;
			}
			
		}
		return new Matrix(newArray);
	}
	
	public final boolean isSingular() {
		return determinant() == 0;
	}
	
	public final boolean isSingular(double tolerance) {
		
		/* Takes into account rounding errors when comparing the determinant 
		 * with zero. A suitable value for the tolerance might be 10E-8. */
		return Math.abs(determinant()) < tolerance;
	}
	
	public final boolean isSquare() {
		return (rows() == columns());
	}
	
	public final boolean isIdentity() {
		
		/* An identity matrix is a square matrix with a descending diagonal
		 * of ones, and with all other elements zeros. */
		if (!isSquare()) {
			return false;
		} else {
			for (int row = 0; row < rows(); row++) {
				
				for (int column = 0; column < columns(); column++) {
					
					/* Not an identity matrix if an element on the main diagonal
					 * is anything other than one, or if any other element is
					 * non-zero. Set out like this for readability. */
					if (row == column && array[row][column] != 1.0) {
						return false;
					} else if (row != column && array[row][column] != 0.0) {
						return false;
					}
					
				}
			}
			
			return true;
		}
		
	}
	
	public final boolean isZero() {
		
		/* Ensure all elements are zero. */
		for (double[] row: array) {
			for (double element: row) {
				if (element != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public final boolean isEmpty() {
		return ((rows() == 0) && (columns() == 0));
	}
}
