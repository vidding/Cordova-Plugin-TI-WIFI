package sm.app.plugins;

import org.json.JSONObject;

public interface FindDeviceCallbackInterface {
	void onDeviceResolved(JSONObject deviceJSON);
}
