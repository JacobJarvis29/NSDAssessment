package com.company;

// Solution to Week 8 Exercise 2 and 3

// compile: javac -cp json-simple-1.1.1.jar;. Message.java

import org.json.simple.JSONObject;

public class Message {
    // class name to be used as tag in JSON representation
    private static final String _class =
            Message.class.getSimpleName();

    private final String body;
    private final String author;
    private final String timestamp;
    private final String channel;

    // Constructor; throws NullPointerException if arguments are null
    public Message(String body, String author, String timestamp, String channel) {
        if (body == null || author == null)
            throw new NullPointerException();

        this.body      = body;
        this.author    = author;
        this.timestamp = timestamp;
        this.channel = channel;
    }

    public String getBody()      { return body; }
    public String getAuthor()    { return author; }
    public String  getTimestamp() { return timestamp; }
    public String   getChannel() { return channel; }

    public String toString() {
        return author + ": " + body + " (" + timestamp + ")";
    }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class",    _class);
        obj.put("body",      body);
        obj.put("author",    author);
        obj.put("timestamp", timestamp);
        obj.put("channel", channel);
        return obj;
    }

    // Tries to deserialize a Message instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static Message fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class"))) {
                return null;
            }
            // deserialize message fields (checking timestamp for null)
            String body      = (String)obj.get("body");
            String author    = (String)obj.get("author");
            String timestamp = (String)obj.get("timestamp");
            String channel = (String)obj.get("channel");
            // construct the object to return (checking for nulls)
            return new Message(body, author, timestamp,channel);
        } catch (ClassCastException | NullPointerException e) {

            return null;
        }
    }
}
