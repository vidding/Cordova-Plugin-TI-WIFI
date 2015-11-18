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
    private static final String LOG_TAG = "WifiAdmin";
    /* cordova actions */
    private static final String ACTION_GET_SSID = "getssid";
    private static final String ACTION_START_CONFIG = "startconfig";
    private static final String ACTION_STOP_CONFIG = "stopconfig";
    private static final String ACTION_START_FIND_DEVICE = "startfinddevice";
    private static final String ACTION_STOP_FIND_DEVICE = "stopfinddevice";

    private WifiManager wifiManager;
    private wificonfig wifiConfig;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;
        if (ACTION_GET_SSID.equals(action)) {
            result = executeGetWifiInfo(args, callbackContext);
        } else if (ACTION_START_CONFIG.equals(action)) {
            result = executeStartConfig(args, callbackContext);
        } else if (ACTION_STOP_CONFIG.equals(action)) {
            result = executeStopConfig(args, callbackContext);
        } else if (ACTION_START_FIND_DEVICE.equals(action)) {
            result = executeStartFindDevice(args, callbackContext);
        } else if (ACTION_STOP_FIND_DEVICE.equals(action)) {
            result = executeStopFindDevice(args, callbackContext);
        } else {
            Log.d(LOG_TAG, String.format("Invalid action passed: %s", action));
            result = new PluginResult(Status.INVALID_ACTION);
        }

        if(result != null) callbackContext.sendPluginResult( result );

        return true;
    }

    private PluginResult executeGetWifiInfo(JSONArray args, CallbackContext callbackContext){
        Log.w(LOG_TAG, "executeGetWifiInfo");

        Context context = cordova.getActivity().getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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

    private PluginResult executeStartConfig(JSONArray args, CallbackContext callbackContext){
        Log.w(LOG_TAG, "executeStartConfig");

        Context context = cordova.getActivity().getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiConfig = new wificonfig(wifiManager);
        wifiConfig.startSmartConfig("TP-LINK_F87E", "zhanggaoyuan20090406", "test", "");

        return null;
    }

    private PluginResult executeStopConfig(JSONArray args, CallbackContext callbackContext){
        Log.w(LOG_TAG, "executeStopConfig");

        if (wifiConfig)
            wifiConfig.stopSmartConfig();
        return null;
    }

    private PluginResult executeStartFindDevice(JSONArray args, CallbackContext callbackContext){
        Log.w(LOG_TAG, "executeStartFindDevice");
        return null;
    }

    private PluginResult executeStopFindDevice(JSONArray args, CallbackContext callbackContext){
        Log.w(LOG_TAG, "executeStopFindDevice");
        return null;
    }

};