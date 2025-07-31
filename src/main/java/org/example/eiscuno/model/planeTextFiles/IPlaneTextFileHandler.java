package org.example.eiscuno.model.planeTextFiles;

import java.io.IOException;

public interface IPlaneTextFileHandler {
    void write(String filename, String content) throws IOException;
    String[] read(String filename);
}
