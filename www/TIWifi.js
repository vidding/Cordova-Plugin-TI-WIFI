/**
 * Created by suye on 2015/11/16.
 */
function TIWifi(){

}

TIWifi.prototype.getssid = function(successCallback, errorCallback){
    cordova.exec(successCallback, errorCallback, "TIWifi", "getssid", []);
};

/*
 * 开始配置SSID...
 * ssid不能为空，其它三个参数都可以为""，不能为null
 *
 */
TIWifi.prototype.startconfig = function(ssid, password, devname, encodekey, successCallback, errorCallback){
    var options = {
        ssid: ssid,
        password: password||"",
        devname: devname||"",
        encodekey: encodekey||""
    };
    cordova.exec(successCallback, errorCallback, "TIWifi", "startconfig", [options]);
};

TIWifi.prototype.stopconfig = function(successCallback, errorCallback){
    cordova.exec(successCallback, errorCallback, "TIWifi", "stopconfig", []);
};
TIWifi.prototype.startfinddevice = function(successCallback, errorCallback){
    cordova.exec(successCallback, errorCallback, "TIWifi", "startfinddevice", []);
};
TIWifi.prototype.stopfinddevice = function(successCallback, errorCallback){
    cordova.exec(successCallback, errorCallback, "TIWifi", "stopfinddevice", []);
};


TIWifi.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.TIWifi = new TIWifi();
    return window.plugins.TIWifi;
};

cordova.addConstructor(TIWifi.install);