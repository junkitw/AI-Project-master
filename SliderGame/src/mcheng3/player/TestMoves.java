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

public class TestMoves {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Board board = new Board(6 ,"H + + + + +\n+ H B + + +\nH + + + + +\nH + V B V +\n+ + + + H +\n+ V + + V V ");
		//board.PrintBoard();
		
		System.out.println(board.getHorizontalMoves());
		System.out.println(board.getVerticalMoves());
		board.PrintBoard();
		ArrayList<Cells> pieces = board.getPieces('H');
		System.out.println(pieces.size());
		for(Cells cell : pieces) {
			System.out.println("(" + cell.x + "," + cell.y + ")");
		}
		
		
		
	}
}
