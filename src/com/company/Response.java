package com.company;

// Solution to Week 8 Exercises

// compile: javac -cp json-simple-1.1.1.jar;. Response.java

import org.json.simple.*;  // required for JSON encoding and decoding

// Abstract super class for all responses
public abstract class Response implements JSONAware {
    // Serializes this object into a JSONObject
    public abstract Object toJSON();

    // Serializes this object and returns the JSON as a string
    public String toJSONString() { return toJSON().toString(); }


    // Prints this object in JSON representation
    public String toString() { return toJSONString(); }
}
