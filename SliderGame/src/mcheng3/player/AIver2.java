package mcheng3.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;

public class AIver2 implements SliderPlayer {
	
	Board AIBoard;
	char player;
	
	public AIver2() {
		
	}
	
	
	@Override
	public void init(int dimension, String board, char player) {
		AIBoard = new Board(dimension, board);
		this.player = player;
	}

	@Override
	public void update(Move move) {
		if(move != null) {
			if(player == 'H') 
				AIBoard.update(move, 'V');
			if(player == 'V') 
				AIBoard.update(move, 'H');
		}

	}

	@Override
	public Move move() {
		
		if (AIBoard.getHorizontalMoves() == 0 && this.player == 'H')
			return null;
		if (AIBoard.getVerticalMoves() == 0 && this.player == 'V')
			return null;
		
		Move move = minimax(6, this.player, Integer.MIN_VALUE, Integer.MAX_VALUE).move;

		
		//System.out.println("Minimax made a move");
		AIBoard.update(move, this.player);
		return move;
	}
	/*
	 * Comparator class to sort a move ArrayList based on player
	 */
	static class moveComparator implements Comparator<Move>{
		
		char player;
		
		public moveComparator(char player){
			this.player = player;
		}
		@Override
		public int compare(Move m1, Move m2) {
			// TODO Auto-generated method stub
			switch(player){
			case 'H':
				if (m1.d == m2.d)
					return 0;
				if (m1.d == Move.Direction.RIGHT)
					return 1;
				if (m2.d == Move.Direction.RIGHT)
					return -1;
				if (m1.d == Move.Direction.DOWN)
					return 1;
				if (m2.d == Move.Direction.DOWN)
					return -1;
				return 0;
			case 'V':
				if (m1.d == m2.d)
					return 0;
				if (m1.d == Move.Direction.UP)
					return 1;
				if (m2.d == Move.Direction.UP)
					return -1;
				if (m1.d == Move.Direction.LEFT)
					return 1;
				if (m2.d == Move.Direction.LEFT)
					return -1;
				return 0;
			}
			return 0;
		}
		
	}
	/**
	 * Fail soft minimax function for decision making
	 * @param depth
	 * @param player
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private Result minimax(int depth, char player, int alpha, int beta) {	
		
		ArrayList<Move> moves = AIBoard.getPossibleMoves(player);
		moves.sort(new moveComparator(player));
		
		Move bestOpt = null;
		if(depth == 0 || moves.isEmpty()) {
			return new Result(evaluate(), bestOpt);
		}
		
		if(player == this.player){
			int bound = Integer.MIN_VALUE;
			for(Move move: moves) {
				//Try a move for current player
				AIBoard.update(move, player);
				//System.out.println("Maximising: " + player);
				//System.out.println("Alpha: " + alpha);
				Result res = minimax(depth - 1, ((player == 'H') ? 'V' : 'H'), alpha, beta);
				AIBoard.undoMove(move, player);
				if(res.score >= bound){
					bound = res.score;
					if (bound > alpha){
						alpha = bound;
						bestOpt = move;
					}
				}

				if(beta <= alpha)
					break;
			}
			return new Result(bound, bestOpt);
		}else{
			int bound = Integer.MAX_VALUE;
			for(Move move: moves) {
				//Try a move for current player
				AIBoard.update(move, player);
				//System.out.println("Minimising: " + player);
				//System.out.println("Beta: " + beta);
				Result res = minimax(depth - 1, ((player == 'H') ? 'V' : 'H'), alpha, beta);
				AIBoard.undoMove(move, player);
				if(res.score < bound){
					bound = res.score;
					if (bound < beta){
						beta = bound;
						bestOpt = move;
					}
				}

				if(beta <= alpha)
					break;
			}
			
			return new Result(bound, bestOpt);
		}
		
	}

	/**
	 * Evaluate current board state by compiling all other factors
	 * @return
	 */
	private int evaluate() {
		int score = 0;
		/*
		switch(this.player) {
		case 'H':
			//Gameover conditions for Horizontal player
			if (AIBoard.getPieces('H').size() == 0)
				return Integer.MAX_VALUE-1;
			if (AIBoard.getPieces('V').size() == 0)
				return Integer.MIN_VALUE+1;
			break;
		case 'V':
			//Gameover conditions for Vertical player
			if (AIBoard.getPieces('V').size() == 0)
				return Integer.MAX_VALUE-1;
			if (AIBoard.getPieces('H').size() == 0)
				return Integer.MIN_VALUE+1;
			break;
		}*/
		int progress = AIBoard.getPieces(player).size()+1;
		//Final weighting adjustments
		score += evaluateBlock()*progress*AIBoard.size*6;
		score += evaluateVictoryCond()*AIBoard.size*progress*100;
		score += evaluateLoseCond()*AIBoard.size*progress*10;
		score += evaluateRows()*AIBoard.size*progress*3;
		score += evaluateDistance()*progress;
		//score += evaluateDistanceOpp()*progress*2;
		//score += evaluatePossibleMoves()*AIBoard.size*progress*2;
		return score;
	}
	/**
	 * Evaluates how likely agent is to lose based on opponents pieces
	 * @return
	 */
	private int evaluateLoseCond(){
		int score = 0;
		switch (this.player) {
		case 'H':
			ArrayList<Cells> horPlayer = AIBoard.getPieces('V'); 
			score += horPlayer.size();
			break;
		case 'V':
			ArrayList<Cells> verPlayer = AIBoard.getPieces('H'); 
			verPlayer = AIBoard.getPieces('H');
			score += verPlayer.size();
			break;
		}
		return score;
	}
	/**
	 * Evaluates progress to victory by counting number of pieces left
	 */
	private int evaluateVictoryCond() {
		int score = 0;
		switch (this.player) {
		case 'H':
			ArrayList<Cells> horPlayer = AIBoard.getPieces('H'); 
			score -= horPlayer.size();
			break;
		case 'V':
			ArrayList<Cells> verPlayer = AIBoard.getPieces('V'); 
			score -= verPlayer.size();
			break;
		}
		return score;
	}
	/**
	 * Evaluates board state based on how many blockages there are for each player
	 * @return
	 */
	private int evaluateBlock() {
		int score = 0;
		switch (this.player) {
		case 'H':
			
			for(Cells cell : AIBoard.getPieces('H')) {
				for(Cells pieces : AIBoard.getPieces('V')) {
					if (cell.x == pieces.x && cell.y > pieces.y) {
						score += 1;
					}
				}
				if(AIBoard.getRight(cell) != null && (AIBoard.getRight(cell).equals("V") || AIBoard.getRight(cell).equals("B"))) {
					score -= 1;
				}
			}
			break;
		case 'V':
			
			for(Cells cell : AIBoard.getPieces('V')) {
				for(Cells pieces : AIBoard.getPieces('H')) {
					if (cell.y == pieces.y && cell.x > pieces.x) {
						score += 1;
					}
				}
				
				if (AIBoard.getUp(cell) != null && (AIBoard.getUp(cell).equals("H") || AIBoard.getUp(cell).equals("B"))) {
					score -= 1;
				}
			}
			break;
		}
		return score;
	}
	/**
	 * Evaluate a score that represents how close opponents pieces are to edge
	 * @return
	 */
	private int evaluateDistanceOpp(){
		int score = 0;
		switch(this.player) {
		case 'H':
			for(Cells cell: AIBoard.getPieces('V')) {
				
				score -= cell.y - AIBoard.size;
			}
			break;
		case 'V':
			for(Cells cell: AIBoard.getPieces('H')) {
				
				score -= cell.x - AIBoard.size;
			}
			break;
		}
		
		return score;
	}
	/**
	 * Evaluate a score that represents how close our pieces are to edge
	 * @return
	 */
	private int evaluateDistance() {
		int score = 0;
		switch(this.player) {
		case 'H':
			for(Cells cell: AIBoard.getPieces('H')) {
				
				score += cell.x - AIBoard.size;
			}
			break;
		case 'V':
			for(Cells cell: AIBoard.getPieces('V')) {
				
				score += cell.y - AIBoard.size;
			}
			break;
		}
		
		return score;
	}
	/**
	 * Evaluate so that row/column stacking is not desirable
	 * @return
	 */
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
				score -= same;
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
				score -= same;
			}
			
		}
		return score;
	}
	/**
	 * Evaluate possible moves from a board state 
	 * @return
	 */
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