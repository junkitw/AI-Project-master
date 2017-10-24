package mcheng3.player;
/***
 * Name: Jun Kit Wong
 * Student Number: 731740
 * 
 * Name: Michael Cheng
 * Student Number: 758456
 */

import java.util.ArrayList;
import java.util.Scanner;

import aiproj.slider.Move;

public class Board {
	int size;
	Cells[][] board;
	
	/***
	 *  constructor generates board from input file and assign absolute coordinates
	 */
	public Board(int dimension, String board) {
		Scanner scanner = new Scanner(board);
		this.size = dimension;
		this.board = new Cells[size][size];
		for(int i = size-1 ; i>= 0; i--) {
			for(int j = 0; j <= size-1; j++){
				this.board[j][i] = new Cells(j,i, scanner.next());
			}
		}
		scanner.close();
	}
	
	public Board(Board board) {
		this.size = board.size;
		this.board = board.board;
	}
	/**
	 * Checks if one player has run out of pieces
	 * @return
	 */
	boolean gameover(){
		for(Cells[] row : board){
			for(Cells cell : row){
				if(cell.val == "V" || cell.val == "H"){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Returns all cells of pieces of given player 
	 * @param player
	 * @return
	 */
	ArrayList<Cells> getPieces(char player){
		ArrayList<Cells> pieces = new ArrayList<Cells>();
		for(Cells[] row : board){
			for(Cells cell : row){
				switch(player) {
				case 'V':
					if(cell.val.equals("V")){
						pieces.add(cell);
					}
					break;
				case 'H':
					if(cell.val.equals("H")) {
						pieces.add(cell);
					}
					break;
				}
			}
		}
		return pieces;
	}
	/**
	 * Update board with a move by particular player
	 * @param move
	 * @param player
	 */
	void update(Move move, char player) {
		if (move != null) {
			if (player == 'V') {
				switch(move.d) {
				case UP:
					if(move.j+1 == size)
						break;
					board[move.i][move.j+1].val = "V";
					break;
				case LEFT:
					board[move.i-1][move.j].val = "V";
					break;
				case RIGHT:
					board[move.i+1][move.j].val = "V";
					break;
				default:
					break;
				}
			}
			if (player == 'H') {
				switch(move.d) {
				case UP:
					board[move.i][move.j+1].val = "H";
					break;
				case DOWN:
					board[move.i][move.j-1].val = "H";
					break;
				case RIGHT:
					if(move.i+1 == size)
						break;
					board[move.i+1][move.j].val = "H";
					break;
				default:
					break;
				}
			}
			
			board[move.i][move.j].val = "+";
		}
		if(move == null) {
			System.out.println("FAIL");
		}
	}
	/**
	 * Undo a move done by particular player
	 * @param done
	 * @param player
	 */
	void undoMove(Move done, char player) {
		switch (player){
		case 'H':
			board[done.i][done.j].val = "H";
			break;
		case 'V': 
			board[done.i][done.j].val = "V";
			break;
		}
		switch (done.d) {
		case UP:
			if (done.j < size-1)
				board[done.i][done.j+1].val = "+";
			break;
		case DOWN:
			board[done.i][done.j-1].val = "+";
			break;
		case LEFT:
			board[done.i-1][done.j].val = "+";
			break;
		case RIGHT:
			if (done.i < size-1)
				board[done.i+1][done.j].val = "+";
			break;
		}
	}
	/***
	 * prints out the board image
	 */
	void PrintBoard() {
		int count = 1;
		for(int i=size-1;i>=0;i--) {
			for(int j=0;j<size;j++){
				System.out.print(board[j][i].val + " ");
				if (count % size == 0){
					System.out.println();
				}
				count++;
				
			}
		}
		count++;
	}
	/**
	 * Returns all the possible moves a player can make
	 * @param player
	 * @return
	 */
	ArrayList<Move> getPossibleMoves(char player){
		ArrayList<Move> moves = new ArrayList<Move>();
		if (player == 'H') {
			for (Cells[] rows: board) {
				for (Cells cell: rows) {
					if (cell.val.equals("H")){
						//System.out.println(cell.x + "," + cell.y);
						if (this.getUp(cell) != null && this.getUp(cell).val.equals("+")) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.UP));
						}	
						if (this.getDown(cell) != null && this.getDown(cell).val.equals("+")) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.DOWN));
						}
						if (this.getRight(cell) != null && this.getRight(cell).val.equals("+")) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.RIGHT));
						}
						else if (cell.x == size-1) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.RIGHT));
						}
					}	
				}
			}
		}
		else if(player == 'V') {
			for (Cells[] rows: board) {
				for (Cells cell: rows) {
					if (cell.val.equals("V")){
						//System.out.println(cell.x + "," + cell.y);
						if (this.getUp(cell) != null && this.getUp(cell).val.equals("+")) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.UP));
						}	
						if (this.getLeft(cell) != null && this.getLeft(cell).val.equals("+")) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.LEFT));
						}
						if (this.getRight(cell) != null && this.getRight(cell).val.equals("+")) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.RIGHT));
						}
						else if (cell.y == size-1) {
							moves.add(new Move(cell.x,cell.y,Move.Direction.UP));
						}
					}	
				}
			}
		}
		return moves;
	}
	/***
	 * counts the number of valid moves for the horizontal player
	 * @return count
	 */
	int getHorizontalMoves() {
		int count = 0;
		for (Cells[] rows: board) {
			for (Cells cell: rows) {
				if (cell.val.equals("H")){
					//System.out.println(cell.x + "," + cell.y);
					if (this.getUp(cell) != null && this.getUp(cell).val.equals("+")) {
						count++;
					}	
					if (this.getDown(cell) != null && this.getDown(cell).val.equals("+")) {
						count++;
					}
					if (this.getRight(cell) != null && this.getRight(cell).val.equals("+")) {
						count++;
					}
					else if (cell.x == size-1) {
						count++;
					}
				}	
			}
		}
		return count;
	}
	
	/***
	 * counts the number of valid moves of vertical player
	 * @return count
	 */
	int getVerticalMoves() {
		int count = 0;
		for (Cells[] rows: board) {
			for (Cells cell: rows) {
				if (cell.val.equals("V")){
					//System.out.println(cell.x + "," + cell.y);
					if (this.getUp(cell) != null && this.getUp(cell).val.equals("+")) {
						count++;
					}	
					if (this.getLeft(cell) != null && this.getLeft(cell).val.equals("+")) {
						count++;
					}
					if (this.getRight(cell) != null && this.getRight(cell).val.equals("+")) {
						count++;
					}
					if (cell.y == size-1) {
						count++;
					}
				}	
			}	
		}
		return count;
	}
	
	/***
	 * finds the cell above the given cell
	 * @param wanted
	 * @return cell, @return null if no cell is found
	 */
	Cells getUp(Cells wanted) {
		if (wanted.y + 1 >= size)
			return null;
		return board[wanted.x][wanted.y + 1];
		
	}
	
	/***
	 * finds the cell below the given cell
	 * @param wanted
	 * @return cell, @return null if no cell is found
	 */
	Cells getDown(Cells wanted) {
		if (wanted.y - 1 < 0)
			return null;
		return board[wanted.x][wanted.y - 1];
	}
	
	/***
	 * finds the cell to the right of the given cell
	 * @param wanted
	 * @return cell, @return null if no cell is found
	 */
	Cells getRight(Cells wanted) {
		if (wanted.x + 1 >= size)
			return null;
		return board[wanted.x+1][wanted.y];
		
	}
	
	/***
	 * finds the cell to the left of the given cell
	 * @param wanted
	 * @return cell, @return null if no cell is found
	 */
	Cells getLeft(Cells wanted) {
		if (wanted.x - 1 < 0)
			return null;
		return board[wanted.x - 1][wanted.y];
	}
}
