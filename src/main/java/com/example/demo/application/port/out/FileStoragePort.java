package com.example.demo.application.port.out;

import java.io.InputStream;

public interface FileStoragePort {
    String saveFile(String path, String originalFilename, InputStream data);
}
