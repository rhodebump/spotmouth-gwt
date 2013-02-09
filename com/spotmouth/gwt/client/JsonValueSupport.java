package com.spotmouth.gwt.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.*;

import java.util.Date;

/**
 * Utility methods for handling JSON values.
 *
 * @author Rob van Maris
 */
public class JsonValueSupport {
    /**
     * Extracts integer field from object
     *
     * @param object the object
     * @param key    the fieldname
     * @return the integer value or null
     * @throws JSONException when the field does not represent an integer value
     */
    public static String stringFieldValue(JSONObject object, String key) throws JSONException {
        JSONValue value = object.get(key);
        if (value == null) {
            return null;
        }
        return jsonValueToString(value);
    }

    public static Boolean booleanFieldValue(JSONObject object, String key) throws JSONException {
        JSONValue value = object.get(key);
        if (value == null) {
            return null;
        }
        return jsonValueToBoolean(value);
    }

    /**
     * Extracts string entry from JSON array.
     *
     * @param jsonArray the JSON array.
     * @param index     index of the entry in the array.
     * @return the string entry.
     * @throws JSONException
     */
    public static String stringArrayEntry(JSONArray jsonArray, int index) throws JSONException {
        JSONValue value = jsonArray.get(index);
        if (value == null) {
            return null;
        }
        return jsonValueToString(value);
    }

    /**
     * Extracts integer field from object
     *
     * @param object the object
     * @param key    the fieldname
     * @return the integer value or null
     * @throws JSONException when the field does not represent an integer value
     */
    public static Integer integerFieldValue(JSONObject object, String key) throws JSONException {
        JSONValue value = object.get(key);
        if (value == null) {
            return null;
        }
        return jsonValueToInt(value);
    }

    /**
     * Extracts integer field from object
     *
     * @param object the object
     * @param key    the fieldname
     * @return the integer value or null
     * @throws JSONException when the field does not represent an integer value
     */
    public static Long longFieldValue(JSONObject object, String key) throws JSONException {
        JSONValue value = object.get(key);
        if (value == null) {
            return null;
        }
        return jsonValueToLong(value);
    }

    /**
     * Extracts integer field from object, returns default when the field is empty.
     *
     * @param object the object
     * @param key    the fieldname
     * @return the integer value or null
     * @throws JSONException when the field does not represent an integer value
     */
    public static Integer integerFieldValue(JSONObject object, String key, Integer defaultValue) throws JSONException {
        Integer result = integerFieldValue(object, key);
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    public static Date dateFieldValue(JSONObject object, String key, String[] patterns) throws JSONException {
        for (String pattern : patterns) {
            try {
                return dateFieldValue(object, key, pattern);
            } catch (Exception e) {
            }
        }
        throw new JSONException("Unabled to parse date '" + String.valueOf(object) + "'. Unknown format");
    }

    public static Date dateFieldValue(JSONObject object, String key, String pattern) throws JSONException {
        String value = stringFieldValue(object, key);
        if (value == null) {
            return null;
        }
        return DateTimeFormat.getFormat(pattern).parse(value);
    }

    public static Date dateFieldValue(JSONObject object, String key, String pattern, Date defaultValue) throws JSONException {
        Date result = dateFieldValue(object, key, pattern);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    /**
     * Extracts integer entry from JSON array.
     *
     * @param jsonArray the JSON array.
     * @param index     index of the entry in the array.
     * @return the integer entry.
     * @throws JSONException
     */
    public static Integer integerArrayEntry(JSONArray jsonArray, int index) throws JSONException {
        JSONValue value = jsonArray.get(index);
        if (value == null) {
            return null;
        }
        return jsonValueToInt(value);
    }

    /**
     * Converts JSON value to integer.
     *
     * @param jsonValue the JSON value.
     * @return the integer value.
     */
    public static Integer jsonValueToInt(JSONValue jsonValue) {
        JSONNumber number = jsonValue.isNumber();
        if (number == null) {
            throw new JSONException("Not an integer: " + jsonValue);
        } else {
            return new Double(number.doubleValue()).intValue();
        }
    }

    /**
     * Converts JSON value to long.
     *
     * @param jsonValue the JSON value.
     * @return the integer value.
     */
    public static Long jsonValueToLong(JSONValue jsonValue) {
        JSONNumber number = jsonValue.isNumber();
        if (number == null) {
            throw new JSONException("Not a long: " + jsonValue);
        } else {
            return new Double(number.doubleValue()).longValue();
        }
    }

    /**
     * Converts JSON value to string.
     *
     * @param jsonValue the JSON value.
     * @return the string value.
     */
    public static String jsonValueToString(JSONValue jsonValue) {
        JSONString string = jsonValue.isString();
        if (string == null) {
            return null;
        } else {
            return string.stringValue();
        }
    }

    public static Boolean jsonValueToBoolean(JSONValue jsonValue) {
        JSONBoolean bool = jsonValue.isBoolean();
        if (bool != null) {
            return bool.booleanValue();
        }
        JSONString string = jsonValue.isString();
        if (string != null) {
            return "true".equals(string.stringValue()) || "t".equals(string.stringValue()) || "yes".equals(string.stringValue()) || "y".equals(string.stringValue());
        }
        throw new JSONException("Not a boolean: " + jsonValue);
    }
}
