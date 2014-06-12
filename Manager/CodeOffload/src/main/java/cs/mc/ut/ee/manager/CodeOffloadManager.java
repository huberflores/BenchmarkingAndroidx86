package cs.mc.ut.ee.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import edu.ut.mobile.network.NetInfo;
import edu.ut.mobile.network.Pack;
import edu.ut.mobile.network.ResultPack;

/*
 * author Huber Flores
 */


public class CodeOffloadManager {
	
	final static Logger logger = Logger.getLogger(CodeOffloadManager.class.getName());
	
	Socket proxyConnection = null;
	
	int portnum;
	byte[] proxyAddress = new byte[4];
	
	ServerSocket knockKnock = null;
	
	InputStream in = null;
	OutputStream out = null;
	DynamicObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	
	
	public CodeOffloadManager(int port){
		portnum = port;
	}
	
	
	
	public boolean startListener(){
		if (knockKnock == null || knockKnock.isClosed()) {
            try {
                knockKnock = new ServerSocket(portnum);
                knockKnock.setSoTimeout(0);
            } catch (IOException ex) {
                logger.warning(ex.getMessage());
            }
        }
		
		 try {
	            System.out.println("server waiting");
	            proxyConnection = knockKnock.accept();
	            
	            in = proxyConnection.getInputStream();
	            out = proxyConnection.getOutputStream();

	            oos = new ObjectOutputStream(out);
	            ois = new DynamicObjectInputStream(in);

	            System.out.println("connection established");

	            forwarder();
	            
	            return true;
	        } catch (SocketException ex) {
	            logger.warning(ex.getMessage());
	            return false;
	        } catch (IOException ex) {
	            logger.warning(ex.getMessage());
	            return false;
	        } catch (Exception ex) {
	            logger.warning(ex.getMessage());
	            return false;
	        }
		
	}
	
	
	private void forwarder() {
        try {
            new gatewayRouter().waitForRequest();
        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }
    }
	
	
	 class gatewayRouter implements Runnable {
		 	APKHandler dalvikProcess;
		 
	        Pack request = null;

	        public gatewayRouter() {
	        }

	        public void waitForRequest() {
	            Thread t = new Thread(this);
	            System.out.println("Thread Starting ");
	            t.start();
	        }

	        //@Override
	        public void run() {
	            try {
	            	
	            	
	            	ois.loadClassFromJar("/home/huber/NQueens_Server.jar");
	            	
	                request = (Pack) ois.readObject();
	                
	                //Here we decide which APK to send into the dalvik
	                
	                
	                
	            	//We proceed to connect to the APK in order to execute the code
	                
	                dalvikProcess = new APKHandler(NetInfo.ipAddress, NetInfo.apkPool);
	                dalvikProcess.setOffloadRequest(request);
	                dalvikProcess.connect();
	                dalvikProcess.execute();
	                
	                ResultPack r = dalvikProcess.getResultPack();
	            	
	            	
	                
	                System.out.println("Function name" +  request.getfunctionName());
	                
	                /*if (request.getfunctionName() != null && request.getfunctionName().length() > 0) {
	       
	                	//aqui se invoca el apk handler
	                	
	                      
	                } else {
	                    returnnull(oos);
	                }*/
	            } catch (IOException e) {
	                returnnull(oos);
	            } catch (ClassNotFoundException e1) {
	                returnnull(oos);
	            } catch (Exception e2){
	            	e2.printStackTrace();
	            } 
	            finally {
	                startListener();
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
