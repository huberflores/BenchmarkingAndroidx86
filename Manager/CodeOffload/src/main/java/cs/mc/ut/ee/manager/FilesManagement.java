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


import cs.mc.ut.ee.utilities.Commons;

/*
 * author Huber Flores
 */

public class FilesManagement {
	public static FilesManagement instance;
	
	ArrayList<String> jarFiles = null;
	
	ArrayList<String> apkFiles = null;

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
				apkFiles = new ArrayList<String>();

				File f = new File(dirPath);
				File[] files = f.listFiles();

				for(int i=0; i < files.length; i++)
				{
					File file = files[i];
					String filename = file.getName();
					String ext = filename.substring(filename.lastIndexOf('.')+1, filename.length());

					if(ext.equals("jar"))
					{
						jarFiles.add(file.getPath());
					}
					
					if (ext.equals("apk")){
						apkFiles.add(file.getPath());
					}
				}

			
	}
	 
	 
	public ArrayList<String> getApkFiles(){
		return this.apkFiles;
	}
	
	
	public ArrayList<String> getJarFiles(){
		return this.jarFiles;
	}

}
