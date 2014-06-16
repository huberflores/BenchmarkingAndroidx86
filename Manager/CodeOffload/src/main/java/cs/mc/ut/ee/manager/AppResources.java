/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * Please send inquiries to huber AT ut DOT ee
 */
package cs.mc.ut.ee.manager;

import java.util.Stack;

/*
 * author Huber Flores
 */

public class AppResources {
	
	String appName;
	
	String jarFile;
	
	Stack apkPool;
	
	
	public AppResources(String appName, String jarFile, Stack apkPool){
		this.appName = appName;
		this.jarFile = jarFile;
		this.apkPool = apkPool;
	}
	
	
	public String getAppName(){
		return this.appName;
	}
	
	public String getJarFile(){
		return this.jarFile;
	}
	
	public Stack getApkPool(){
		return this.apkPool;
	}
	
	public void setAppName(String appName){
		this.appName = appName;
	}
	
	public void setApkPool(Stack apkPool){
		this.apkPool = apkPool;
	}
	
	public void setJarFile(String jarFile){
		this.jarFile = jarFile;
	}

}
