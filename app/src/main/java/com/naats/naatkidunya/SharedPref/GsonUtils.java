package com.naats.naatkidunya.SharedPref;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
public class GsonUtils {

    public static String convertToJSON(Object object) {
        GsonBuilder gsonBuilder = getGsonBuilder();
        final Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(object);
        return jsonString;
    }

    public static <T> T convertToObject(String responseString, Class<T> classOfT) throws Exception {
        GsonBuilder gsonBuilder = getGsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        try {
            return gson.fromJson(responseString, classOfT);
        } catch (Exception e){
            throw e;
        }
    }

    private static GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField( FieldAttributes fieldAttributes) {
                final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                return expose != null && !expose.serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        });
        gsonBuilder
                .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField( FieldAttributes fieldAttributes) {
                        final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                        return expose != null && !expose.deserialize();
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> aClass) {
                        return false;
                    }
                });
        return gsonBuilder;
    }
}
