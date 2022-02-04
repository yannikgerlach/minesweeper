package minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** Sets up a field, i.e. creates specified number of mines and surrounding field values. **/
public class FieldCreator {
	
	private Minesweeper minesweeper;
	
	private int numberOfRows;
	private int numberOfColumns;
	
	private int numberOfMines;
	
	private Field[][] field;
	private Set<Position> mines = new HashSet<Position>();
	
	private FieldRevealer fieldRevealer;


	public FieldCreator(Minesweeper minesweeper, int rows, int columns, int mines) {
		this.minesweeper = minesweeper;
		this.numberOfRows = rows;
		this.numberOfColumns = columns;
		this.numberOfMines = mines;
		this.field = new Field[rows][columns];
		this.fieldRevealer = new FieldRevealer(field);
	}


	public Field[][] generate() {
		generateMines();
		setField();
		calculateFieldValues();
		return field;
	}


	private void generateMines() {
		while (mines.size() < numberOfMines)
			generateMine();
	}


	private void generateMine() {
		Random generator = new Random();
		int row = generator.nextInt(numberOfRows);
		int column = generator.nextInt(numberOfColumns);
		Position position = new Position(row, column);
		if (!isPositionAMine(position)) 
			mines.add(position);
	}


	private boolean isPositionAMine(Position position) {
		int row = position.row;
		int column = position.column;
		for (Position mine : mines)
			if (row == mine.row && column == mine.column)
				return true;
		return false;
	}


	/** Set field classes to be either mines or fields. **/
	private void setField() {
		for (int row = 0; row < numberOfRows; row++) {
			for (int column = 0; column < numberOfColumns; column++) {
				Field fieldValue;
				Position position = new Position(row, column);

				if (isPositionAMine(position)) {
					fieldValue = new Mine(minesweeper, fieldRevealer, position);
				} else {
					fieldValue =  new Value(minesweeper, fieldRevealer, position);
				}

				field[row][column] = fieldValue;
			}
		}
	}


	private void calculateFieldValues() {
		for (int row = 0; row < numberOfRows; row++) {
			for (int column = 0; column < numberOfColumns; column++) {
				Position position = new Position(row, column);
				if (isPositionAMine(position))
					increaseValueInSurroundingFields(position);
			}	
		}
	}


	private void increaseValueInSurroundingFields(Position centerPosition) {
		for (int row = centerPosition.row-1; row <= centerPosition.row+1; row++) {
			for (int column = centerPosition.column-1; column <= centerPosition.column+1; column++) {
				Position position = new Position(row, column);
				if (isPositionInBoundary(position))
					increaseValueOfField(field[row][column]);
			}	
		}
	}


	private boolean isPositionInBoundary(Position position) {
		int row = position.row;
		int column = position.column;
		boolean isInRowBoundary = row >= 0 && row < numberOfRows;
		boolean isInColumnBoundary = column >= 0 && column < numberOfColumns;
		return isInRowBoundary && isInColumnBoundary;
	}


	private void increaseValueOfField(Field field) {
		if (!(field instanceof Value))
			return;
		Value valueField = (Value) field;
		valueField.increaseValue();
	}
	
}
