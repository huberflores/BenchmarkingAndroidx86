package cs.mc.ut.ee.simulator;

import cs.mc.ut.ee.logic.MiniMaxRemote;
//import cs.ut.ee.algorithm.BubbleSort;


/*
 * author Huber Flores
 */

public class CodeOffloadRequest implements Runnable {
	
	
	//BubbleSort client;
	MiniMaxRemote client;

	public void run() {
		// Method invocation of the algorithm to execute remotely
		
		
		//BubbleSort
		
		//client = new BubbleSort(0);
		//client.sortFunction();
		
		
		client = new MiniMaxRemote();
		
		
		int[][] board = {{-3, -5, -4, -2, -1, -4, -5, -3},
				{-6, -6, -6, -6, -6, -6, -6, -6},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{6, 6, 6, 6, 6, 6, 6, 6},
				{3, 5, 4, 2, 1, 4, 5, 3}
				};
		
		int [] chess = new int[64];
		for (int i=0;i<8;i++)
			for (int j=0;j<8;j++)
				chess[j*8+i]=board[i][j];
		
		
		float [] steps = client.minimax(chess, 3, false);
		
		
	}
	

	

}
