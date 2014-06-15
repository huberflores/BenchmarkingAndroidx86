package cs.mc.ut.ee.manager;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import edu.ut.mobile.network.NetInfo;
import edu.ut.mobile.network.Pack;
import edu.ut.mobile.network.ResultPack;

/*
 * author Huber Flores
 */

public class CodeOffloadManager implements Runnable{

    protected Socket proxyConnection = null;
    protected String serverText   = null;
    
	APKHandler dalvikProcess;
	 
    Pack request = null;
    ResultPack response = null;
    
    InputStream in = null;
	OutputStream out = null;
	DynamicObjectInputStream ois = null;
	ObjectOutputStream oos = null;

    public CodeOffloadManager(Socket proxyConnection, String serverText) {
        this.proxyConnection = proxyConnection;
        this.serverText   = serverText;
        
        
    }

    public void run() {
        try {
        	System.out.println("Handling a code offload request");
        
        	in = proxyConnection.getInputStream();
            out = proxyConnection.getOutputStream();

            oos = new ObjectOutputStream(out);
            ois = new DynamicObjectInputStream(in);
        	
        	//ois.loadClassFromJar("/home/huber/NQueens_Server.jar");
            ois.loadClassFromJar("/home/huber/isPrime_Server.jar");
        	
            request = (Pack) ois.readObject();
            
            //Here we decide which APK to send into the Dalvik
            
            
            
        	//We proceed to connect to the APK in order to execute the code
            
            
            dalvikProcess = new APKHandler(NetInfo.ipAddress, NetInfo.apkPool);
            dalvikProcess.setOffloadRequest(request);
            dalvikProcess.connect();
            dalvikProcess.execute();
            
            
            //ResultPack r = dalvikProcess.getResultPack();
            
            
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

}
