/**
 * Created by suye on 2015/11/16.
 */
TIWifi = {};

TIWifi.prototype.getssid = function(successCallback, errorCallback){
    cordova.exec(successCallback, errorCallback, "TIWifi", "getssid", []);
};

TIWifi.prototype.setssid = function(ssid, password, successCallback, errorCallback){
    var options = {
        ssid: ssid,
        password: password
    };
    cordova.exec(successCallback, errorCallback, "TIWifi", "getssid", [options]);
};

TIWifi.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.TIWifi = new TIWifi();
    return window.plugins.TIWifi;
};

cordova.addConstructor(TIWifi.install);