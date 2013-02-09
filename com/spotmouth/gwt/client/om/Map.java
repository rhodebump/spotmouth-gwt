package com.spotmouth.gwt.client.om;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

/**
 * Code adapted from http://code.google.com/p/gwt-in-the-air/source/browse/trunk/src/net/ltgt/gwt/jscollections/client/JsMap.java.
 * .
 *
 * @param <T> The value type.
 */
public class Map<T extends JavaScriptObject> extends JavaScriptObject {
    /**
     * Constructs a Map.
     */
    public static <T extends JavaScriptObject> Map<T> newInstance() {
        return JavaScriptObject.createObject().cast();
    }

    protected Map() {
    }

    /**
     * Determines whether the specified key is contained in the map.
     *
     * @param key The key to search for.
     */
    public final native boolean contains(String key) /*-{
        return (key in this);
    }-*/;

    /**
     * Retrieves a value by the corresponding key.
     *
     * @param key The key corresponding to the value which should be retrieved.
     * @return The corresponding value or null if the key is not present.
     */
    public final native T get(String key) /*-{
        return this[key];
    }-*/;

    /**
     * Retrieves a value by the corresponding key.
     *
     * @param key          The key corresponding to the value which should be retrieved.
     * @param defaultValue The value to return if the key is not present.
     * @return The corresponding value or the default value if the key is not
     *         present.
     */
    public final native T get(String key, T defaultValue) /*-{
        return this[key] || defaultValue;
    }-*/;

    /**
     * Retrieves the collection of keys for this map.
     *
     * @return The map keys.
     */
    public final String[] keys() {
        return ArrayHelper.toArray(getKeys());
    }

    /**
     * Removes the selected key/value pair from the map.
     *
     * @param key The key which should be removed
     */
    public final native void remove(String key) /*-{
        delete this[key];
    }-*/;

    /**
     * Adds a key/value pair to the map.
     *
     * @param key   The key.
     * @param value The value.
     */
    public final native void set(String key, T value) /*-{
        this[key] = value;
    }-*/;

    /**
     * Retrieves the collection of values for this map.
     *
     * @return The map values.
     */
    public final T[] values() {
        return ArrayHelper.toArray(getValues());
    }

    private native JsArrayString getKeys() /*-{
        var ls = [];
        for (var l in this) ls.push(l);
        return ls;
    }-*/;

    private native JsArray<T> getValues() /*-{
        var ls = [];
        for (var l in this) ls.push(l);
        return ls;
    }-*/;
}
