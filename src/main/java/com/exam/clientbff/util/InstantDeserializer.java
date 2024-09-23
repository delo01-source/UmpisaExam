package com.exam.clientbff.util;

import java.lang.reflect.Type;
import java.time.Instant;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class InstantDeserializer implements JsonDeserializer<Instant>,JsonSerializer<Instant> {
	  @Override
	  public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	    return Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
	  }
	  
	  @Override
	    public JsonElement serialize(Instant instant, Type type, JsonSerializationContext JsonDeserializationContext){
	        return new JsonPrimitive(instant.toString());
	    }

	}