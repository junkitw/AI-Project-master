/***
 * Name: Jun Kit Wong
 * Student Number: 731740
 * 
 * Name: Michael Cheng
 * Student Number: 758456
 */

package mcheng3.player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;

public class SmartAss implements SliderPlayer{
	
	Board AIBoard;
	char player;
	Random r = new Random();
	
	public SmartAss() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(int dimension, String board, char player) {
		// TODO Auto-generated method stub
		AIBoard = new Board(dimension, board);
		this.player = player;
	}

	@Override
	public void update(Move move) {
		// TODO Auto-generated method stub
		if(move != null) {
			if(player == 'H') 
				AIBoard.update(move, 'V');
			if(player == 'V') 
				AIBoard.update(move, 'H');
		}
		
		//System.out.println("THE PLAYER NOW IS " + this.player);
	}

	@Override
	public Move move() {
	
		//System.out.println(player);
		if (AIBoard.getHorizontalMoves() == 0 && this.player == 'H')
			return null;
		if (AIBoard.getVerticalMoves() == 0 && this.player == 'V')
			return null;
		
		ArrayList<Move> moves = AIBoard.getPossibleMoves(this.player);
		
		//for loop - each move you call minimax function
		//max value in the list - choose that move
		/*for(int i = 0; i < moves.size(); i++) {
			val = minimav(move);
			
			if(val>max)
				max = val
				max_move = this move
			
		}*/
		/*int max_outcome = 0;
		int[] map = new int[moves.size()];
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < moves.size(); i++) {
			System.out.print(moves.get(i));
			System.out.print('\t');
			if(this.player == 'H') {
				max_outcome =  ((AIBoard.size-1) - moves.get(0).i);
				map[i] = ((AIBoard.size-1) - moves.get(i).i);
				System.out.println("THE DISTANCE TO GOAL IS: " + map[i]);
				if(map[i]< max_outcome) {
					max_outcome = map[i];
					indexes.add(i);
				}
			}		
			if(this.player == 'V') {
				max_outcome =  ((AIBoard.size-1) - moves.get(0).j);
				map[i] = ((AIBoard.size-1) - moves.get(i).j);
				System.out.println("THE DISTANCE TO GOAL IS: " + map[i]);
				
				if(map[i] < max_outcome) {
					max_outcome = map[i];
					indexes.add(i);
					
				}
			}
			
		}
		
		System.out.println("\nTHE MAX OUTCOME FOR THIS PLAYER IS " + max_outcome);
		
		for(int i: indexes) {
			System.out.println(i);
		}*/
		
		Move move = moves.get(r.nextInt(100) % moves.size());
		
		// TODO Auto-generated method stub
		AIBoard.update(move, this.player);
		return move;
	}
	
	private int evaluate_distance() {
		
		int score = 0;
		switch(this.player) {
		case 'H':
			for(Cells cell: AIBoard.getPieces('H')) {
				
				score += cell.x;
			}
			
			for(Cells cell: AIBoard.getPieces('V')) {
				
				score -= cell.y;
			}

		case 'V':
			for(Cells cell: AIBoard.getPieces('V')) {
				
				score += cell.y;
			}
			
			for(Cells cell: AIBoard.getPieces('H')) {
				
				score -= cell.x;
			}

		}
		
		return score;
	}
	
	private int evaluate_rows() {
		int score = 0;
		int same = -1;
		switch(this.player) {
		case 'H':
			for(int i = 0; i < AIBoard.size; i++) {
				for(int j = 0; j < AIBoard.size; j++) {
					if(AIBoard.board[j][i].equals("H")) {
						same++;
					}
				}
				score -= same;
			}
			
			for(int i = 0; i < AIBoard.size; i++) {
				for(int j = 0; j < AIBoard.size; j++) {
					if(AIBoard.board[i][j].equals("V")) {
						same--;
					}
				}
				score -= same;
			}
		case 'V':
			for(int i = 0; i < AIBoard.size; i++) {
				for(int j = 0; j < AIBoard.size; j++) {
					if(AIBoard.board[j][i].equals("V")) {
						same++;
					}
				}
				score -= same;
			}
			
			for(int i = 0; i < AIBoard.size; i++) {
				for(int j = 0; j < AIBoard.size; j++) {
					if(AIBoard.board[i][j].equals("H")) {
						same--;
					}
				}
				score -= same;
			}
			
		}
		return score;
	}
	
	private int evaluatePossibleMoves() {
		int score = 0;
		ArrayList<Move> possMoveH = AIBoard.getPossibleMoves('H');	
		ArrayList<Move> possMoveV = AIBoard.getPossibleMoves('V');	
		switch(this.player) {
		case 'H':
			score += possMoveH.size();		
			score -= possMoveV.size();

		
		case 'V':
			score += possMoveV.size();		
			score -= possMoveH.size();
		
		}
		return score;
	}
	

}
