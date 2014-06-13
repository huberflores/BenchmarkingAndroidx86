package cs.mc.ut.ee.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

import edu.ut.mobile.network.NetInfo;
import edu.ut.mobile.network.Pack;
import edu.ut.mobile.network.ResultPack;

public class APKHandler {
	
	Pack proxyRequest = null;
	
	int portnum;
	Socket mysocket = null;
	InputStream in = null;
	OutputStream out = null;
	    
	DynamicObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	    
	byte []serverAddress = new byte[4];
	long startTime = 0;

	
	Object result = null;
    Object state = null; 
	
	ResultPack offloadResult = null;
    
	
	public APKHandler(byte[] serverAddress, int port){
		this.portnum = port;
		this.serverAddress = serverAddress;
		
	}
	
	public void setOffloadRequest(Pack mobileRequest){
		this.proxyRequest = mobileRequest;
	}
	
	
	private Pack getOffloadRequest(){
		return this.proxyRequest;
	}
	
	
	 public boolean connect(){  
	    mysocket = new Socket();
	    try {
	        mysocket.connect(new InetSocketAddress(Inet4Address.getByAddress(serverAddress), portnum), NetInfo.waitTime);

	        startTime = System.currentTimeMillis();
	        in = mysocket.getInputStream();
	            
	        out = mysocket.getOutputStream();  

	       
	        oos = new ObjectOutputStream(new BufferedOutputStream(out));
	        oos.flush(); 
 
	        ois =new DynamicObjectInputStream(new BufferedInputStream(in));
	                 
	            
	       return true;
	     } catch (IOException ex) {
	        setResult(null, null);
	          return false;
	     }
	    }



	 public void execute(){
	    try{
	        new Sending(getOffloadRequest()).send();
	    }catch(Exception ex){
	        setResult(null, null);
	    }
	 }

	
	
	
	
	class Sending implements Runnable{
		Pack MyPack = null;
        ResultPack result = null;

        public Sending(Pack MyPack) {
            this.MyPack = MyPack;
        }


        public void send(){
            Thread t = new Thread(this);
            t.start();
        }

        
        public void run() {
            try {

                oos.writeObject( MyPack );
                oos.flush();
                                
                ois.loadClassFromJar("/home/huber/NQueens_Server.jar");
                
                result = (ResultPack) ois.readObject();
                
                setResultPack(result);

                if((System.currentTimeMillis() - startTime) < NetInfo.waitTime){
                    if(result == null)
                        setResult(null, null);
                    else
                        setResult(result.getresult(), result.getstate());
                }

                oos.close();
                ois.close();

                in.close();
                out.close();

                mysocket.close();

                oos = null;
                ois = null;

                in = null;
                out = null;
                mysocket = null;

            } catch (IOException ex) {
                setResult(null, null);
            } catch (ClassNotFoundException ex) {
                setResult(null, null);
            }
        }

    }
	
	public void setResult(Object result, Object cloudModel){
            this.result = result;
            this.state = cloudModel;     
    }
	
	public void setResultPack(ResultPack result){
			this.offloadResult = result;
   	
	}
	
	public ResultPack getResultPack(){
		return offloadResult;
	}
	
}






