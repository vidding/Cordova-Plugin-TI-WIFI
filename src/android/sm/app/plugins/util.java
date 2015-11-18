package sm.app.plugins;

import android.net.wifi.WifiManager;

public class util {

	public static final String ZERO_PADDING_16="0000000000000000";
	
	public static String getGateway (WifiManager wm) {
		return util.intToIp(wm.getDhcpInfo().gateway);
	}
	
	public static String intToIp(int i) {
		return ((i >> 24 ) & 0xFF ) + "." +
				((i >> 16 ) & 0xFF) + "." +
				((i >> 8 ) & 0xFF) + "." +
				( i & 0xFF) ;
	}
}
