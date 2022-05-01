package com.lab7.common.util;

import com.lab7.common.common_exceptions.DeserializeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Deserializer {
    private final byte[] serializedObject;
    private Serializable deserializedObject;
    private boolean alreadyDeserialized;

    public Deserializer(byte[] serializedObject) {
        this.serializedObject = serializedObject;
        alreadyDeserialized = false;
    }

    public Serializable deserialize() {
        if (alreadyDeserialized) {
            return deserializedObject;
        }
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(serializedObject);
             ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream)) {
                deserializedObject = (Serializable) objectInputStream.readObject();
                alreadyDeserialized = true;
                return deserializedObject;
        } catch (IOException | ClassNotFoundException e) {
            throw new DeserializeException("Невозможно десериализовать объект");
        }
    }

    public boolean possibleToDeserialize() {
        if (alreadyDeserialized) {
            return true;
        }
        try {
            deserialize();
            return true;
        } catch (DeserializeException e) {
            return false;
        }
    }
}
