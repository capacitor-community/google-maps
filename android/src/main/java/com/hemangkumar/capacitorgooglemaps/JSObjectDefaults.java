package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import org.json.JSONException;

import java.util.HashMap;

public abstract class JSObjectDefaults {
    private final HashMap<String, Object> defaults;

    private final HashMap<String, Object> actualValues = new HashMap<String, Object>(){};

    public JSObjectDefaults(HashMap<String, Object> defaults) {
        this.defaults = defaults;
    }

    private Object getObject(String key) {
        // get default value
        Object defaultObject = this.defaults.get(key);

        // then get actual value
        if (this.actualValues.containsKey(key)) {
            Object object = this.actualValues.get(key);
            if (object != null) {
                return object;
            }
        }

        if (defaultObject != null) {
            return defaultObject;
        }

        return null;
    }

    public boolean getBoolean(String key) {
        Object object = getObject(key);

        if (object != null) {
            return (boolean) object;
        }

        return false;
    }

    public void updateFromJSObject(@Nullable JSObject jsObject) {
        if (jsObject != null) {
            for (HashMap.Entry<String, Object> entry : defaults.entrySet()) {
                String key = entry.getKey();
                Object defaultValue = entry.getValue();

                if (defaultValue instanceof Boolean) {
                    if (jsObject.has(key)) {
                        Boolean newValue = jsObject.getBoolean(key, (Boolean) defaultValue);
                        if (newValue == null) {
                            newValue = (Boolean) defaultValue;
                        }
                        actualValues.put(key, (boolean) newValue);
                    }
                }
            }
        }
    }

    @NonNull
    public static JSObject getJSObjectSafe(JSObject jsObject, @NonNull String name, @NonNull JSObject defaultValue) {
        JSObject returnedJsObject = jsObject.getJSObject(name);
        if (returnedJsObject != null) {
            return returnedJsObject;
        }
        return defaultValue;
    }

    @NonNull
    public static JSObject getJSObjectSafe(PluginCall call, @NonNull String name, @NonNull JSObject defaultValue) {
        if (call != null) {
            JSObject jsObject = call.getObject(name, defaultValue);
            if (jsObject != null) {
                return jsObject;
            }
        }
        return new JSObject();
    }

    @NonNull
    public static Double getDoubleSafe(JSObject jsObject, @NonNull String name, @NonNull Double defaultValue) {
        try {
            return jsObject.getDouble(name);
        } catch (JSONException ignored) {}
        return defaultValue;
    }

    @NonNull
    public static Float getFloatSafe(JSObject jsObject, @NonNull String name, @NonNull Float defaultValue) {
        Double returnedDouble = JSObjectDefaults.getDoubleSafe(jsObject, name, (double) defaultValue);
        return returnedDouble.floatValue();
    }

    @NonNull
    public static Integer getIntegerSafe(JSObject jsObject, @NonNull String name, @NonNull Integer defaultValue) {
        Integer returnedInteger = jsObject.getInteger(name);
        if (returnedInteger != null) {
            return returnedInteger;
        }
        return defaultValue;
    }

    @NonNull
    public static Boolean getBooleanSafe(JSObject jsObject, @NonNull String name, @NonNull Boolean defaultValue) {
        Boolean returnedBoolean = jsObject.getBoolean(name, false);
        if (returnedBoolean != null) {
            return returnedBoolean;
        }
        return defaultValue;
    }
}
