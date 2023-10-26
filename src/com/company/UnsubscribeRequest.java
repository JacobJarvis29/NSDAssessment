package com.company;
// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. LoginRequest.java

import org.json.simple.JSONObject;

public class UnsubscribeRequest extends Request {
    // class name to be used as tag in JSON representation
    private static final String _class =
            UnsubscribeRequest.class.getSimpleName();

    private String channel;

    // Constructor; throws NullPointerException if name is null.
    public UnsubscribeRequest(String channel) {
        // check for null
        if (channel == null)
            throw new NullPointerException();
        this.channel = channel;
    }

    String getChannel() { return channel; }

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channel", channel);
        return obj;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static UnsubscribeRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            // deserialize login name
            String channel = (String)obj.get("channel");
            // construct the object to return (checking for nulls)
            return new UnsubscribeRequest(channel);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
