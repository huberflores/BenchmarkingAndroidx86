package cs.mc.ut.ee.simulator;

import cs.mc.ut.ee.algorithm.BubbleSort;


/*
 * author Huber Flores
 */

public class CodeOffloadRequest implements Runnable {
	
	BubbleSort client;

	public void run() {
		// TODO Auto-generated method stub
		client = new BubbleSort(0);
		client.sortFunction();
	}
	

	

}
