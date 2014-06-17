package cs.ut.ee.nqueens;

import java.util.Vector;

import cs.ut.ee.algorithm.BubbleSort;
import cs.ut.ee.algorithm.Parameters;



import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class CloudService extends Service {
  
	private static Vector<Double> execTimes= new Vector<Double>();
	
	Thread Clientthread = null;

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    // Retrieve the shared preferences
//    Context context = getApplicationContext();
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
      
	  Clientthread = new Thread(new ClientThread());
	  Clientthread.start();
      return Service.START_STICKY;
  };

  private class ClientThread implements Runnable {
	
	@Override
	public void run() {
		RunFuncs();
	}
  };

  @Override
  public void onCreate() {
	  
  }
	
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
  
  private void RunFuncs() {
	  	  

	   execTimes.removeAllElements();
	
	   double avgBubbleTime = 0;
	   //for (int i = 0; i < 4; i++){
	       long internalstTime = System.nanoTime();
	       
	       //IsPrime nq = new IsPrime(Parameters.N);
	       //nq.determinePrime(Parameters.list);
	       
	       //PrimeFactors nq = new PrimeFactors(Parameters.N);
	       //nq.determinePrimeFactors();
	       
	       BubbleSort bubble = new BubbleSort(Parameters.N);
	       bubble.sortFunction();
	       
	       
	
	       execTimes.add((System.nanoTime() - internalstTime)*1.0e-6);
	   //}
	   avgBubbleTime = avg(execTimes);


      notifytochangelable("Average Bubble Time: " + avgBubbleTime);
  }
    
  static double avg(Vector<Double> nums){
      double avg = 0;
      for(int i = 0; i < nums.size(); i++){
          avg += (double)nums.get(i);
      }

      avg = avg / nums.size();
      return avg;
  }

  public void notifytochangelable(String BubbleTime){
		Intent intent = new Intent(IsPrimeOffloading.changelabel);
	    intent.putExtra("iscloud", true);
	    intent.putExtra("Bubbleintent", BubbleTime);
	    
	    sendBroadcast(intent);
	  }

}
