/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * Please send inquiries to huber AT ut DOT ee
 */

package cs.mc.ut.ee.manager;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

import cs.mc.ut.ee.utilities.Commons;

import edu.ut.mobile.network.NetInfo;
import edu.ut.mobile.network.Pack;
import edu.ut.mobile.network.ResultPack;

/*
 * author Huber Flores
 */

public class CodeOffloadManager implements Runnable{

    protected Socket proxyConnection = null;
    protected String mobileApp   = null;
    protected String jar = null;
    protected String apk = null;
    
	APKHandler dalvikProcess;
	 
    Pack request = null;
    ResultPack response = null;
    
    InputStream in = null;
	OutputStream out = null;
	DynamicObjectInputStream ois = null;
	ObjectOutputStream oos = null;


    public CodeOffloadManager(Socket proxyConnection, String mobileApp, String jar, String apk) {
        this.proxyConnection = proxyConnection;
        this.mobileApp = mobileApp;
        this.jar = jar;
        this.apk = apk;
                
    }

    public void run() {
        try {
        	System.out.println("Handling a code offload request");
        
        	in = proxyConnection.getInputStream();
            out = proxyConnection.getOutputStream();

            oos = new ObjectOutputStream(out);
            ois = new DynamicObjectInputStream(in);
        	
            
            /*
             * Re-construct the object sent from the mobile
             * Classes are loaded as needed from jar files
             * Jar are created in the maven built process
             */
            
            //-TODO-(automate)
            //mobile application must be identified a priori

            ois.loadClassFromJar(jar);
            request = (Pack) ois.readObject();
    
            /*
             * apkFiles stack has the available apps to use in a specific port
             * The idea is that the file pop is utilized for invocation
             * and once is finished is push again into the stack
             * BlubbleSort_Server_6001
             * BlubbleSort_Server_6002, etc.
             */

            //-TODO-(introduce a delay)
        	//Process send = Runtime.getRuntime().exec(new String[] {"sh", "-c", "cd /home/huber/Desktop/TechnicalInformation/x86Image/android-x86/; ./rund.sh -cp " + apk +" " + "edu.ut.mobile.network.Main"});
        	
        	
        	/*
        	 * proceed to connect to the APK in order to execute the code
        	 */
  
            dalvikProcess = new APKHandler(NetInfo.ipAddress, getPort(apk), jar);
            dalvikProcess.setOffloadRequest(request);
            dalvikProcess.connect();
            dalvikProcess.execute();
                     
            
            long wait = System.currentTimeMillis();
            boolean invocation = false;
            while (!invocation){
            	
            	if (dalvikProcess.getResultPack()!=null){
            		invocation = true;
            	}
            	
            	if ((System.currentTimeMillis() - wait)>100000){
            		invocation = true;
            		System.out.println("Result was null or the execution exceed the waiting time");
            	}
            	

            }
            
            
        	//send back the result to the client
        	response = dalvikProcess.getResultPack();
        	oos.flush();
        	oos.writeObject(response);
    		oos.flush(); 
    		System.out.println("Respose was sent to the mobile: " +  response.getresult());
            
            
   
        } catch (IOException e) {
            returnnull(oos);
        } catch (ClassNotFoundException e1) {
            returnnull(oos);
        } catch (Exception e2){
        	e2.printStackTrace();
        } 
        finally {
        	
        	try {
				oos.close();
				ois.close();

                in.close();
                out.close();

                proxyConnection.close();

                oos = null;
                ois = null;

                in = null;
                out = null;
                proxyConnection = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
          
        }
    }
    
    
    public void returnnull(ObjectOutputStream oos){
        if(oos != null)
            try {
                oos.writeObject(null);
                oos.flush();
            } catch (IOException ex1) {

            }
    }
    
    
    public int getPort(String apkFile){
    	int port = -1;
    	File apk = new File(apkFile);
    	
    	port = Integer.valueOf(apk.getName().substring(apk.getName().length()-8, apk.getName().length()-4));
    	
    	return port;
    }
    
 
    
}
