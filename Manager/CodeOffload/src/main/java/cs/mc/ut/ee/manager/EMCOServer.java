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
    
    ArrayList<AppResources> resources = new ArrayList<AppResources>();;
    
    FilesManagement files = FilesManagement.getInstance(); 
    

    public EMCOServer(int port){
        this.serverPort = port;
        addResources(Commons.app1);
        
        //
        startApksFromApp("isPrime");
        
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
            new Thread(
                new CodeOffloadManager(
                    clientSocket, "isPrime", getResource("isPrime").getJarFile(), (String) getResource("isPrime").getApkPool().pop())
            ).start();
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
    		//add the resource
    		Stack availableApks = new Stack();
    		for (int i = 0; i< files.getApkFiles().size(); i++){
    			if (files.getApkFiles().get(i).contains(appName)){
    				availableApks.push(files.getApkFiles().get(i));
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
     * This method pushes the APKs from a particular application into the Dalvik
     * Usually, APKs are push by the "Code Offload Manager"
     * However, the java "Process" utility is to slow to activate the process, and thus
     * It is advisable to put the APKs to listen before receiving a code offload request
     */
    public void startApksFromApp(String appName){
    	
    	for (int k = 0; k< files.getApkFiles().size(); k++){
			if (files.getApkFiles().get(k).contains(appName)){
				try {
					Process send = Runtime.getRuntime().exec(new String[] {"sh", "-c", "cd /home/huber/Desktop/TechnicalInformation/x86Image/android-x86/; ./rund.sh -cp " + files.getApkFiles().get(k) +" " + "edu.ut.mobile.network.Main"});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	
    }
    

}