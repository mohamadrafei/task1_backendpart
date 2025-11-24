/*
 * package com.example.demo.infrastructure.storage;
 * 
 * import java.io.IOException;
 * import java.io.InputStream;
 * import java.nio.file.Files;
 * import java.nio.file.Path;
 * import java.nio.file.Paths;
 * import java.nio.file.StandardCopyOption;
 * 
 * import org.springframework.stereotype.Component;
 * 
 * import com.example.demo.application.port.out.FileStoragePort;
 * 
 * import lombok.RequiredArgsConstructor;
 * import lombok.Value;
 * 
 * // infrastructure/storage/LocalFileStorageAdapter.java
 * 
 * @Component
 * 
 * @RequiredArgsConstructor
 * public class LocalFileStorageAdapter implements FileStoragePort {
 * 
 * @Value("${storage.base-path}")
 * private String basePath;
 * 
 * @Override
 * public String saveFile(String path, String originalFilename, InputStream
 * data) {
 * try {
 * Path dir = Paths.get(basePath, path);
 * Files.createDirectories(dir);
 * String filename = System.currentTimeMillis() + "_" + originalFilename;
 * Path target = dir.resolve(filename);
 * Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
 * // URL could be something like /files/{path}/{filename} served by a static
 * // handler
 * return "/files/" + path + "/" + filename;
 * } catch (IOException e) {
 * throw new RuntimeException("Failed to save file", e);
 * }
 * }
 * }
 */