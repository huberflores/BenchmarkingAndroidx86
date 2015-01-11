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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import cs.mc.ut.ee.utilities.Commons;

/*
 * author Huber Flores
 */

public class FilesManagement {
	public static FilesManagement instance;
	
	ArrayList<String> jarFiles = null;
	
	Map<Integer, String> apkFiles = null;

	private FilesManagement(){ 
		initializeFiles(Commons.jarDirectory);
	}

	public static synchronized FilesManagement getInstance(){
		if (instance==null){
			instance = new FilesManagement();
			return instance;
		}

		return instance;
	}
	
	
	 private void initializeFiles(String dirPath){

				jarFiles = new ArrayList<String>();
				apkFiles = new HashMap<Integer, String>();

				File f = new File(dirPath);
				File[] files = f.listFiles();

				for(int i=0; i < files.length; i++)
				{
					File file = files[i];
					String fileName = file.getName();
					String ext = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());

					if(ext.equals("jar"))
					{
						jarFiles.add(file.getPath());
					}
					
					if (ext.equals("apk")){
						int port = getPort(fileName);
						apkFiles.put(port,file.getPath());
					}
				}

			
	}
	 
	 
	private int getPort(String fileName){
	
		int pos1 = fileName.lastIndexOf("__")+2;
		int pos2 = fileName.lastIndexOf('.');
		String apkPort = fileName.substring(pos1, pos2);
		
		int port = Integer.valueOf(apkPort);
		
		
		return port;
	} 
	 
	 
	public Map<Integer, String> getApkFiles(){
		return this.apkFiles;
	}
	
	
	public ArrayList<String> getJarFiles(){
		return this.jarFiles;
	}

}
