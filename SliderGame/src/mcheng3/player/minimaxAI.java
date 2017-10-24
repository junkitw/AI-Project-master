package mcheng3.player;

import java.util.ArrayList;
import java.util.Random;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;

public class minimaxAI implements SliderPlayer {
	
	Board AIBoard;
	char player;
	Random r = new Random();
	int max_outcome = 0;
	
	public minimaxAI() {
		
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

	}

	@Override
	public Move move() {
		// TODO Auto-generated method stub
		
		if (AIBoard.getHorizontalMoves() == 0 && this.player == 'H')
			return null;
		if (AIBoard.getVerticalMoves() == 0 && this.player == 'V')
			return null;
		
		

		Move move = minimax(5, this.player, Integer.MIN_VALUE, Integer.MAX_VALUE).move;
		//System.out.println("Minimax made a move");
		AIBoard.update(move, this.player);
		return move;
	}
	
	private int evaluate() {
		int score = 0;
		score += evaluateBlock();
		score += evaluateVictoryCond();
		score += evaluateRows();
		score += evaluateDistance();
		score += evaluatePossibleMoves();
		score += evaluateDirection();
	
		return score;
	}
	
	private int evaluateVictoryCond() {
		int score = 0;
		switch (this.player) {
		case 'H':
			ArrayList<Cells> horPlayer = AIBoard.getPieces('H'); 
			//Win for vertical player
			if(horPlayer.isEmpty()) {
				return Integer.MAX_VALUE;
			}
			
			score -= horPlayer.size()*AIBoard.size;
			
			horPlayer = AIBoard.getPieces('V');
			//Loss for Horizontal player
			if(horPlayer.isEmpty()) {
				return Integer.MIN_VALUE;
			}
			
			score += horPlayer.size()*AIBoard.size;
			break;
		case 'V':
			ArrayList<Cells> verPlayer = AIBoard.getPieces('V'); 
			//Win for Vertical player
			if(verPlayer.isEmpty()) {
				return Integer.MAX_VALUE - 1;
			}
			
			score -= verPlayer.size()*AIBoard.size;

			verPlayer = AIBoard.getPieces('H');
			//Loss for Vertical player
			if(verPlayer.isEmpty()) {
				return Integer.MIN_VALUE + 1;
			}
			
			score += verPlayer.size()*AIBoard.size;
			break;
		}
		return score;
	}
	private int evaluateBlock() {
		int score = 0;
		switch (this.player) {
		case 'H':
			for(Cells cell : AIBoard.getPieces('H')) {
				if (AIBoard.getDown(cell) != null && AIBoard.getDown(cell).equals("V")) {
					score += 1;
				}
				if(AIBoard.getRight(cell) != null && (AIBoard.getRight(cell).equals("V") || AIBoard.getRight(cell).equals("B"))) {
					score -= 2;
				}
			}
			break;
		case 'V':
			for(Cells cell : AIBoard.getPieces('V')) {
				if (AIBoard.getLeft(cell) != null && AIBoard.getLeft(cell).equals("H")) {
					score += 1;
				}
				if (AIBoard.getUp(cell) != null && (AIBoard.getUp(cell).equals("H") || AIBoard.getUp(cell).equals("B"))) {
					score -= 2;
				}
			}
			break;
		}
		return score;
	}
	
	private Result minimax(int depth, char player, int alpha, int beta) {	
		
		Move bestOpt = null;
		int score;
	
		ArrayList<Move> moves = AIBoard.getPossibleMoves(player);
	
		if(depth == 0 || moves.isEmpty()) {
			score = evaluate();
			return new Result(score, bestOpt);
		}else{
			for(Move move: moves) {
				//Try a move for current player
				AIBoard.update(move, player);
				
				//Maximizing for this player
				if(player == this.player){
					score = minimax(depth - 1, ((player == 'H') ? 'V' : 'H'), alpha, beta).score;
					if (score > alpha){
						alpha = score;
						bestOpt = move;
					}
				}
				
				//Minimizing for opponent
				else{
					score = minimax(depth - 1, ((player == 'H') ? 'V' : 'H'), alpha, beta).score;
					if (score < beta){
						beta = score;
						bestOpt = move;
					}
				}
				//Undo tested move
				AIBoard.undoMove(move, player);
				
				//Pruning
				if (alpha >= beta)
					break;
			}
		}
	if(player == this.player){
		return new Result(alpha, bestOpt);
	}else{
		return new Result(beta, bestOpt);
	}
		
	}

	
	private int evaluateDistance() {
		int score = 0;
		switch(this.player) {
		case 'H':
			for(Cells cell: AIBoard.getPieces('H')) {
				
				score += cell.x - AIBoard.size;
			}
			
			for(Cells cell: AIBoard.getPieces('V')) {
				
				score -= cell.y - AIBoard.size;
			}
			break;
		case 'V':
			for(Cells cell: AIBoard.getPieces('V')) {
				
				score += cell.y - AIBoard.size;
			}
			
			for(Cells cell: AIBoard.getPieces('H')) {
				
				score -= cell.x - AIBoard.size;
			}
			break;
		}
		
		return score;
	}
	
	private int evaluateRows() {
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
				score += same;
			}
		case 'V':
			for(int i = 0; i < AIBoard.size; i++) {
				for(int j = 0; j < AIBoard.size; j++) {
					if(AIBoard.board[i][j].equals("V")) {
						same++;
					}
				}
				score -= same;
			}
			
			for(int i = 0; i < AIBoard.size; i++) {
				for(int j = 0; j < AIBoard.size; j++) {
					if(AIBoard.board[j][i].equals("H")) {
						same--;
					}
				}
				score += same;
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
	
	private int evaluateDirection() {
		int score = 0;	
		switch(this.player) {
		case 'H':
			ArrayList<Move> possMoveH = AIBoard.getPossibleMoves('H');
			for(Move moving: possMoveH) {
				if(moving.d.equals("RIGHT")) {
					score += 5;
				}
				else {
					score += 1;
				}
			}
		case 'V':
			ArrayList<Move> possMoveV = AIBoard.getPossibleMoves('V');
			for(Move moving: possMoveV) {
				if(moving.d.equals("UP")) {
					score += 5;
				}
				else {
					score += 1;
				}
			}
		}
		return score;
	}

}