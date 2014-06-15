package cs.mc.ut.ee.simulator;


/*
 * author Huber Flores
 */

public class LoadGenerator {
	
	
	public void generateLoad(int users){
		
		for (int i = 0; i<users; i++){
			new Thread(new CodeOffloadRequest()).start();
		}
		
	}


}
