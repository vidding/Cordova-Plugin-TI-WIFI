package sm.app.plugins;

import android.view.Gravity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class TIWifi extends CordovaPlugin {
    private static final String LOGTAG = "WifiAdmin";
    /* cordova actions */
    private static final String ACTION_GET_SSID = "getssid";
    private static final String ACTION_SET_SSID = "setssid";

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;
        if (ACTION_GET_SSID.equals(action)) {
            result = executeGetWifiInfo(args, callbackContext);
        } else if (ACTION_SET_SSID.equals(action)) {
            result = executeSetSSID(args, callbackContext);
        } else {
            Log.d(LOGTAG, String.format("Invalid action passed: %s", action));
            result = new PluginResult(Status.INVALID_ACTION);
        }

        if(result != null) callbackContext.sendPluginResult( result );

        return true;
    }

    private PluginResult executeGetWifiInfo(JSONArray args, CallbackContext callbackContext){
        Log.w(LOGTAG, "executeGetWifiInfo");

        Context context = cordova.getActivity().getApplicationContext();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        JSONObject obj = new JSONObject();
        try {
            JSONObject activity = new JSONObject();
            activity.put("BSSID", wifiInfo.getBSSID());
            activity.put("HiddenSSID", wifiInfo.getHiddenSSID());
            activity.put("SSID", wifiInfo.getSSID());
            activity.put("MacAddress", wifiInfo.getMacAddress());
            activity.put("IpAddress", wifiInfo.getIpAddress());
            activity.put("NetworkId", wifiInfo.getNetworkId());
            activity.put("RSSI", wifiInfo.getRssi());
            activity.put("LinkSpeed", wifiInfo.getLinkSpeed());
            obj.put("activity", activity);

            JSONArray available = new JSONArray();
            for (ScanResult scanResult : wifiManager.getScanResults()) {
                JSONObject ap = new JSONObject();
                ap.put("BSSID", scanResult.BSSID);
                ap.put("SSID", scanResult.SSID);
                ap.put("frequency", scanResult.frequency);
                ap.put("level", scanResult.level);
                //netwrok.put("timestamp", String.valueOf(scanResult.timestamp));
                ap.put("capabilities", scanResult.capabilities);
                available.put(ap);
            }
            obj.put("available", available);

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("JSON Exception");
        }
        callbackContext.success(obj);

        return null;
    }

    private PluginResult executeSetSSID(JSONArray args, CallbackContext callbackContext){
        Log.w(LOGTAG, "executeSetSSID");
        final JSONObject options = args.getJSONObject(0);
        final String ssid = options.getString("ssid");
        final String password = options.getString("password");
        callbackContext.success("TIWifi: set ssid:" + ssid + " password:" + password);
        return null;
    }
};