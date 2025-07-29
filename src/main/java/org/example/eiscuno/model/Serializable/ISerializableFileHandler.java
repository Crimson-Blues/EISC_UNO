package org.example.eiscuno.model.Serializable;

import java.io.Serializable;

public interface ISerializableFileHandler {
    void serialize(String filename, Object element);
    Object deserialize(String filename);
}
