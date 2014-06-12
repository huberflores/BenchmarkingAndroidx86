package cs.mc.ut.ee.manager;

import edu.ut.mobile.network.NetInfo;

public class Activator {
	
	public static void main(String[] args) {
        CodeOffloadManager manager = new CodeOffloadManager(NetInfo.port);
        manager.startListener();
    }

}
