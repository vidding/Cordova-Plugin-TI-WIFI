package sm.app.plugins;

import android.view.Gravity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TIWifi extends CordovaPlugin {

    private static final String ACTION_GET_SSID = "getssid";
    private static final String ACTION_SET_SSID = "setssid";

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (ACTION_GET_SSID.equals(action)) {
            callbackContext.success("TIWifi: get ssid");
            return true;
        } else if (ACTION_SET_SSID.equals(action)) {
            final JSONObject options = args.getJSONObject(0);
            final String ssid = options.getString("ssid");
            final String password = options.getString("password");
            callbackContext.success("TIWifi: set ssid:" + ssid + " password:" + password);
            return true;
        } else {
            callbackContext.error("TIWifi." + action + " is not a supported function.");
            return false
        }
    }
};