package com.rubird.muzeipinterest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by varunoberoi on 20/03/15.
 */
public class ArrayAdapterFactory implements TypeAdapterFactory {

//    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
//
//        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
//        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
//
//        return new TypeAdapter<T>() {
//
//            public void write(JsonWriter out, T value) throws IOException {
//                delegate.write(out, value);
//            }
//
//            public T read(JsonReader in) throws IOException {
//
//                JsonElement jsonElement = elementAdapter.read(in);
//                if (jsonElement.isJsonObject()) {
//                    JsonObject jsonObject = jsonElement.getAsJsonObject();
//                    if (jsonObject.has("data") && jsonObject.get("data").isJsonObject())
//                    {
//                        jsonElement = jsonObject.get("data");
//                    }
//                }
//
//                if (jsonElement.isJsonObject()) {
//                    JsonObject jsonObject = jsonElement.getAsJsonObject();
//                    if (jsonObject.has("pins") && jsonObject.get("pins").isJsonArray())
//                    {
//                        jsonElement = jsonObject.get("pins");
//                    }
//                }
//
//                return delegate.fromJsonTree(jsonElement);
//            }
//        }.nullSafe();
//    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {

        TypeAdapter<T> typeAdapter = null;

        try {
            if (type.getRawType() == List.class)
                typeAdapter = new ArrayAdapter(
                        (Class) ((ParameterizedType) type.getType())
                                .getActualTypeArguments()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return typeAdapter;
    }

}
