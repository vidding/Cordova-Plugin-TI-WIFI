package sm.app.plugins;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;

public class finddevice implements ServiceListener, ServiceTypeListener {

	private WifiManager wm;
	private MulticastLock multicastLock;
	private boolean isDiscovering;
	private JmDNS jmdns;
	private FindDeviceCallbackInterface callback;
	
	public static final String LOGTAG = "TIWifi";
	public static final String SERVICE_TYPE = "_http._tcp.";
    public static final String SMARTCONFIG_IDENTIFIER = "srcvers=1D90645";
    

	public finddevice(WifiManager wm, FindDeviceCallbackInterface callback){
		this.wm = wm;
		this.callback = callback;
		
		multicastLock = wm.createMulticastLock(getClass().getName());
    	multicastLock.setReferenceCounted(true);
	}

	public void setCallback(FindDeviceCallbackInterface callback) {
	    this.callback = callback;
	}

	public void startDiscovery() {
    	if (isDiscovering )
    		return;
    	InetAddress deviceIpAddress = getDeviceIpAddress(wm);
    	if (!multicastLock.isHeld()){
    		multicastLock.acquire();
    	} else {
    		System.out.println(" Muticast lock already held...");
    	}
    	try {    		
    		jmdns = JmDNS.create(deviceIpAddress, "SmartConfig");
    		jmdns.addServiceTypeListener(this);
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (jmdns != null) {
    			isDiscovering = true;
    			System.out.println("discovering services");
    		}
    	}
    }    
    
    public void stopDiscovery() {
    	try {
    		if (!isDiscovering || jmdns == null)
    			return;
    		if (multicastLock.isHeld()){
    			multicastLock.release();
    		} else {
    			System.out.println("Multicast lock already released");
    		}
    		jmdns.unregisterAllServices();
    		jmdns.close();
    		jmdns = null;
    		isDiscovering = false;
    		System.out.println("MDNS discovery stopped");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    private InetAddress getDeviceIpAddress(WifiManager wifi) {
		InetAddress result = null;
		try {
			// default to Android localhost
			//result = InetAddress.getByName("10.0.0.2");

			// figure out our wifi address, otherwise bail
			WifiInfo wifiinfo = wifi.getConnectionInfo();
			int intaddr = wifiinfo.getIpAddress();
			byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff), (byte) (intaddr >> 8 & 0xff), (byte) (intaddr >> 16 & 0xff), (byte) (intaddr >> 24 & 0xff) };
			result = InetAddress.getByAddress(byteaddr);
		} catch (UnknownHostException ex) {
			Log.w(LOGTAG, String.format("getDeviceIpAddress Error: %s", ex.getMessage()));
		}

		return result;
	}
    
    @Override
	public void serviceTypeAdded(ServiceEvent event) {
		if (event.getType().contains(SERVICE_TYPE)) {
			jmdns.addServiceListener(event.getType(), this);
		}
	}

	@Override
	public void subTypeForServiceTypeAdded(ServiceEvent event) {}

	@Override
	public void serviceAdded(ServiceEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serviceRemoved(ServiceEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serviceResolved(ServiceEvent service) {
		// TODO Auto-generated method stub
		if (service.getInfo().getNiceTextString().contains(SMARTCONFIG_IDENTIFIER)){
			System.out.println("resolved: " + service.toString());
			JSONObject deviceJSON = new JSONObject();
			try {
				deviceJSON.put("name", service.getName());
				deviceJSON.put("host", service.getInfo().getHostAddresses()[0]);
				
				Log.d(LOGTAG, "device name: " + deviceJSON.getString("name"));
				Log.d(LOGTAG, "device host: " + deviceJSON.getString("host"));
				
				callback.onDeviceResolved(deviceJSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}