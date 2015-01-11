/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * Please send inquiries to huber AT ut DOT ee
 */

package cs.mc.ut.ee.manager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.io.IOException;

import cs.mc.ut.ee.utilities.Commons;


/*
 * author Huber Flores
 */

public class EMCOServer implements Runnable{

    protected int          serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    
    public static ArrayList<AppResources> resources = new ArrayList<AppResources>();;
    
    FilesManagement files = FilesManagement.getInstance();
       

    public EMCOServer(int port){
        this.serverPort = port;
        addResources(Commons.app3);
        
        //Automatic activation of APK files
        startApksFromApp("g_chess");
        
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            
        	if (!getResource("g_chess").getApkPool().isEmpty()){
        	    new Thread(
                        new CodeOffloadManager(
                            clientSocket, "g_chess", getResource("g_chess").getJarFile(), 
                            files.getApkFiles().get(getResource("g_chess").getApkPool().pop()))
                    ).start();
    		}else{
    			System.out.println("No slots available for code offloading!");
    		}
    	
        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 6000", e);
        }
    }
    
    
    
    private AppResources getResource(String appName){
    	
    	for (int i= 0; i<resources.size(); i++){
    		if (resources.get(i).getAppName().equals(appName)){
    			return resources.get(i);
    		}
    	}
    	return null;
    }
    
    
    private void addResources(String appName){
    	
    	if (getResource(appName)==null){
    		Set<Integer> ports = files.getApkFiles().keySet();
    		Iterator<Integer> iterator = ports.iterator();
    
    		Stack<Integer> availableApks = new Stack<Integer>();
    		while (iterator.hasNext()){
    			int apkPort = iterator.next();
    			if (files.getApkFiles().get(apkPort).contains(appName)){
    				availableApks.push(apkPort);
    			}
    			
    		}
    		
    		
    		String jarFile = null;
    		for (int j = 0; j< files.getJarFiles().size(); j++){
    			if (files.getJarFiles().get(j).contains(appName)){
    				jarFile = files.getJarFiles().get(j);
    			}
    		}
    		
    		resources.add(new AppResources(appName, jarFile, availableApks));

    	}
    	
    }
    
    
    
    /*
     * This method pushes the APKs from a particular application into the Dalvik as a process named 'dalvikvm'
     * Usually, APKs are pushed by the "Code Offload Manager"
     * However, the java "Process" utility is too slow to activate the process, and thus
     * It is advisable to put the APKs to listen before receiving a code offload request
     */
    public void startApksFromApp(String appName){
    	  	
    	Set<Integer> ports = files.getApkFiles().keySet();
    	Iterator<Integer> iterator = ports.iterator();
    	
    	while(iterator.hasNext()){
    		int apkPort = iterator.next();
    		if (files.getApkFiles().get(apkPort).contains(appName)){
    			try {
    				//double starTime = System.currentTimeMillis();
    				Process send = Runtime.getRuntime().exec(new String[] {"sh", "-c", "cd " + Commons.apkFilesPath +";" + "./rund.sh -cp " + files.getApkFiles().get(apkPort) +" " + "edu.ut.mobile.network.Main"});
    				
    				//System.out.println("Pushing time : " + (System.currentTimeMillis() - starTime));
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		
    	}
    	
    	new Thread(
                new CheckPortAvailability()
            ).start();
    	
    }
    
 
    class CheckPortAvailability implements Runnable {
    	
		public void run() {
	
			while(!isStopped()){			
				System.out.println("Available ports : " + getResource("g_chess").getApkPool().toString());
				
				Set<Integer> ports = files.getApkFiles().keySet();
				Iterator<Integer> iterator = ports.iterator();
				
				while(iterator.hasNext()){
					int port = iterator.next();
					//System.out.println("The port " +  port + " is associated with file " +  files.getApkFiles().get(port));
				}
				
		    	
		    	try {
					Thread.sleep(12000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	
			}
			
		}
    	
    }
    
    

}