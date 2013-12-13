package cs.ut.ee.mobilecloud;

import java.io.*;
import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.regex.*;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.*;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import javax.servlet.http.*; 

public class UploadImage extends HttpServlet{
		
	private static final long serialVersionUID = 1L;

	private static final String idQuery="AKIAJFIEZ5567RYA4UKQ";
	private static final String secretKey ="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";
	
	String fileName;	
	String fileNameShort;
	S3Service s3Service;
	

	public int connectWalrus() {
		int success=-1;
		AWSCredentials awsCredentials = new AWSCredentials(idQuery,secretKey);
		   try{
			   s3Service = new RestS3Service(awsCredentials);
			   success=0;
 
		   }catch(S3ServiceException e){ 
			   e.printStackTrace();
		   }
      return success;
	}
	
	public ArrayList<String> listBuckets(){
		ArrayList<String> bucketsWalrus = new ArrayList<String>();
	        try {
				S3Bucket[] myBuckets = s3Service.listAllBuckets(); 
				for (S3Bucket bucket: myBuckets)
					bucketsWalrus.add(bucket.getName());
				
			} catch (S3ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
	    return bucketsWalrus;
	}
	
	public void connectionWalrusRefused(HttpServletResponse res){
		try {
    		
    		res.setStatus(200);
    		res.setContentType("text/html");
    		PrintWriter out = res.getWriter();
    		out.println("<html>");
            out.println("<head>");
            out.println("<title>List of buckets!</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Connection refused with Walrus... :( <h1>");
            out.println("</body>");
            out.println("</html>");

            out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public void uploadImageFile(String bucketName, String filepath){
		 try {
			File fileData = new File(filepath);
			S3Object fileObject = new S3Object(fileData);
			fileObject.setContentType("i");
			
			fileObject = s3Service.putObject(bucketName, fileObject);
			
		} catch (S3ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
		
	public void uploadToServer(HttpServletRequest request) {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			//System.out.println("File Not Uploaded");
		} else {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = null;

			try {
				items = upload.parseRequest(request);
				//System.out.println("items: "+items);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()){
					String name = item.getFieldName();
					//System.out.println("name: "+name);
					String value = item.getString();
					//System.out.println("value: "+value);
				} else {
					try {
						String itemName = item.getName();
						Random generator = new Random();
						int r = Math.abs(generator.nextInt());

						String reg = "[.*]";
						String replacingtext = "";
						//System.out.println("Text before replacing is:-" + itemName);
						Pattern pattern = Pattern.compile(reg);
						Matcher matcher = pattern.matcher(itemName);
						StringBuffer buffer = new StringBuffer();

						while (matcher.find()) {
							matcher.appendReplacement(buffer, replacingtext);
						}
						int IndexOf = itemName.indexOf("."); 
						String domainName = itemName.substring(IndexOf);
						//System.out.println("domainName: "+domainName);

						Calendar calendar = Calendar.getInstance();
						//String finalimage = buffer.toString()+"_"+r+domainName;
						fileName = buffer.toString() + calendar.getTimeInMillis() + domainName;
						//System.out.println("Final Image==="+finalimage);

						//Gökhan
						//Random rand = new Random();

						//int  n = rand.nextInt(5000);
						//Gökhan
						fileNameShort = fileName.replaceAll(".java", "");
						File savedFile = new File("/tmp/"+ fileNameShort + ".java");
						
						item.write(savedFile);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		int walrusConnection = connectWalrus();
				
		if (walrusConnection>=0){
			try {

				res.setStatus(200);
	    		res.setContentType("text/html");
	    		PrintWriter out = res.getWriter();
	    		out.println("<html>");
	            out.println("<head>");
	            out.println("<title>List of buckets!</title>");
	            out.println("</head>");
	            out.println("<body>");
	            
				S3Bucket[] buckets=null;
				try {
					buckets = s3Service.listAllBuckets();
				} catch (S3ServiceException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (buckets!=null){
					for (int b = 0; b < buckets.length; b++) {
						out.println("<h1> Bucket: "+buckets[b].getName() + " contains: <h1>");
			            //System.out.println("Bucket '" + buckets[b].getName() + "' contains:");

			            // List the objects in this bucket.
			            S3Object[] objects =null;
						try {
							objects = s3Service.listObjects(buckets[b].getName());
						} catch (S3ServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

			            // Print out each object's key and size.
						if (objects!=null){
							for (int o = 0; o < objects.length; o++) {
				                //System.out.println(" " + objects[o].getKey() + " (" + objects[o].getContentLength() + " bytes)");
				                out.println(" " + objects[o].getKey() + " (" + objects[o].getContentLength() + " bytes)");
				            }
						}
			            
			        }
				}
	    		
	    			            	
	            out.println("</body>");
	            out.println("</html>");

	            out.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			//Connection to Walrus was refused...
			connectionWalrusRefused(res);
		}
	}
  	
    
    
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {	
		int walrusConnection = connectWalrus();
		
				
		//if (walrusConnection>=0){
		//uploadToServer(req);

			try {
	    		
				double start1 = System.currentTimeMillis();
					uploadToServer(req);
					
					//working code for without dalvik
					//Process send = Runtime.getRuntime().exec(new String[] {"sh", "-c", "sed -i 's/Foo/" + fileNameShort + "/g' /tmp/"+ fileName + "; javac /tmp/"+ fileName +  "; java /tmp/"+ fileNameShort + ".class; " +
					//		"jar cfv /tmp/"+ fileNameShort +".jar /tmp/"+ fileNameShort + ".class ;"}); 
					//working code for without dalvik
					
					
					Process send = Runtime.getRuntime().exec(new String[] {"sh", "-c", "sed -i 's/IsPrime/" + fileNameShort + "/g' /tmp/"+ fileName + "; javac /tmp/"+ fileName +
					"; /home/ubuntu/android-sdk-linux/platform-tools/dx --dex --no-strict --output=/tmp/" + fileNameShort + ".jar /tmp/"+ fileNameShort +".class; cd /home/ubuntu/android-x86; /home/ubuntu/android-x86.rund.sh -cp /tmp/"+ fileNameShort +".jar " + fileNameShort}); 
					
					//working code
					//Process send = Runtime.getRuntime().exec(new String[] {"sh", "-c", "export PATH=${PATH}:/home/ubuntu/android-sdk-linux/platform-tools; /home/ubuntu/android-sdk-linux/platform-tools/dx --dex --no-strict --output=/tmp/IsPrime1385548413449.jar /tmp/IsPrime1385548413449.class;"});
					//working code
					double end1 = System.currentTimeMillis();
				
				writeTo(start1, end1, fileName, fileNameShort, fileName.length(), "/tmp/Client_Server.txt");
					
				res.setStatus(200);
	    		res.setContentType("text/html"); 
	    		PrintWriter out = res.getWriter();
	    		out.println("<html>");
	            out.println("<head>");
	            out.println("<title>Upload Image!</title>");
	            out.println("</head>");
	            out.println("<body>");

	            //double start2 = System.currentTimeMillis();
	            	            
	            	//uploadImageFile("clobucket", "/tmp/"+fileName);	
				
				//double end2 = System.currentTimeMillis();
	            
				//writeTo(start2, end2, fileNam.length(), "/mnt/MCM_Cloud.txt");
				
	            out.println("</body>");
	            out.println("</html>");

	            out.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//}else{
			//Connection to Walrus was refused...
	//		connectionWalrusRefused(res);
		//}
	}
	
	public void writeTo(double startTime, double endTime, String fileName1, String fileShortName1, double imageSize, String filename){
								
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true));
			writer.append(imageSize + ", " + startTime + ", " + endTime +", " + (endTime-startTime));			
			
			writer.newLine();
			//writer.append("/" + fileName1 + "/, /" + fileShortName1 + "/" );			
			
			//writer.newLine();

			writer.close();
	
		}
		catch(IOException e){
			System.out.println("Exception occurred trying to write '%s'., filename");
		}
	}
  	
	
}
