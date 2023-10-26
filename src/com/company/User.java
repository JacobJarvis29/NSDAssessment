package com.company;

// Solution to Week 8 Exercise 2 and 3

// compile: javac -cp json-simple-1.1.1.jar;. Message.java

import org.json.simple.JSONObject;

import java.util.List;

public class User {
    // class name to be used as tag in JSON representation
    private static final String _class =
            User.class.getSimpleName();

    private final String username;
    private final String password;
    private List<String> subscribedChannels;


    // Constructor; throws NullPointerException if arguments are null
    public User(String username,String password, List<String> subscribedChannels) {
        if (username == null||password ==null || subscribedChannels == null||subscribedChannels.contains(null))
            throw new NullPointerException();

        this.username     = username;
        this.password=password;
        this.subscribedChannels    = subscribedChannels;
    }

    public String getUsername()      { return username; }
    public String getPassword() {return  password;}
    public List<String> getSubscribed()    { return subscribedChannels; }
    public void setSub(List<String> subs){
        this.subscribedChannels = subs;

    }
    public String toString() {
        return (username + "," + password + "," +subscribedChannels.toString());
    }


    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class",    _class);
        obj.put("username",      username);
        obj.put("password",password);
        obj.put("channels",    subscribedChannels);
        return obj;
    }

    // Tries to deserialize a Message instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static User fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class"))) {
                return null;
            }
            // deserialize message fields (checking timestamp for null)
            String username      = (String)obj.get("username");
            String password = (String)obj.get("password");
            List<String> subscribedChannels    = (List<String>) obj.get("channels");

            // construct the object to return (checking for nulls)
            return new User(username,password,subscribedChannels);
        } catch (ClassCastException | NullPointerException e) {

            return null;
        }
    }
}
