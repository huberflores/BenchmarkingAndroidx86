package cs.mc.ut.ee.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarFile;


/*
 * author Huber Flores
 */

public class DynamicObjectInputStream extends ObjectInputStream {

	private ClassLoader baseCurrent = ClassLoader.getSystemClassLoader();
	private URLClassLoader baseJarLoader = null;

	public DynamicObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		try {
			try {
				return baseCurrent.loadClass(desc.getName());
			} catch (ClassNotFoundException e) {
				return baseJarLoader.loadClass(desc.getName());
			}
		} catch (ClassNotFoundException e) {
			return super.resolveClass(desc);
		} catch (NullPointerException e) { 
			return super.resolveClass(desc);
		}

	}
	


	public void loadClassFromJar(String pathToJar) {
			
		try{
			JarFile jarFile = new JarFile(pathToJar);
			Enumeration e = jarFile.entries();
			
			URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            baseJarLoader = cl;
		}catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	

}




















