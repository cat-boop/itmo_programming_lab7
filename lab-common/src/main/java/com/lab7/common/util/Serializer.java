package com.lab7.common.util;

import com.lab7.common.common_exceptions.SerializeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class Serializer {
    private byte[] serializedObject;
    private final Serializable objectToSerialize;
    private boolean alreadySerialized;

    public Serializer(Serializable objectToSerialize) {
        this.objectToSerialize = objectToSerialize;
    }

    public byte[] serialize() {
        if (alreadySerialized) {
            return serializedObject;
        }
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream)) {
            outputStream.writeObject(objectToSerialize);
            outputStream.flush();
            serializedObject = arrayOutputStream.toByteArray();
            alreadySerialized = true;
            return serializedObject;
        } catch (IOException e) {
            throw new SerializeException("Невозможно сериализовать запрос");
        }
    }

    public boolean possibleToSerialize() {
        if (alreadySerialized) {
            return true;
        }
        try {
            serialize();
            return true;
        } catch (SerializeException e) {
            return false;
        }
    }
}
