package sm.app.plugins;

import android.net.wifi.WifiManager;

import com.integrity_project.smartconfiglib.SmartConfig;
import com.integrity_project.smartconfiglib.SmartConfigListener;



public class wificonfig{
	
	private WifiManager wm;

	public wificonfig(WifiManager wm){
		this.wm = wm;
	}
	
	private boolean isStartsmartconfiging;
	private SmartConfig smartConfig;

	public void startSmartConfig(String ssid, String password, String devicename, String pEncryptionKey ) {
		if (isStartsmartconfiging)
			return;
		
		isStartsmartconfiging = true;
		String passwordKey = password;
		String SSID = ssid;
		String gateway = util.getGateway(wm);
		byte[] freeData;
		byte[] paddedEncryptionKey;	
		if (pEncryptionKey.length() > 0) {
			paddedEncryptionKey = (pEncryptionKey + util.ZERO_PADDING_16).substring(0, 16).trim().getBytes();
		} else {
			paddedEncryptionKey = null;
		}
		if (devicename.length() > 0) { // device name isn't empty
			byte[] freeDataChars = new byte[devicename.length() + 2];
			freeDataChars[0] = 0x03;
			freeDataChars[1] = (byte) devicename.length();
			for (int i=0; i<devicename.length(); i++) {
				freeDataChars[i+2] = (byte) devicename.charAt(i);
			}
			freeData = freeDataChars;
		} else {
			freeData = new byte[1];
			freeData[0] = 0x03;
		}
		
		paddedEncryptionKey = null;
		
		smartConfig = null;
		SmartConfigListener smartConfigListener = new SmartConfigListener() {
			@Override
			public void onSmartConfigEvent(SmtCfgEvent event, Exception e) {}
		};
		try {			
			smartConfig = new SmartConfig(smartConfigListener, freeData, passwordKey, paddedEncryptionKey, gateway, SSID, (byte) 0, "");
			smartConfig.transmitSettings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopSmartConfig() {
		try {
			if (isStartsmartconfiging){
				smartConfig.stopTransmitting();
			}
			isStartsmartconfiging = false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}