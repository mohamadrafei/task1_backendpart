package com.example.demo.application.port.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoUploadService {

    // root folder for uploads (relative to project folder by default)
    @Value("${app.upload-dir:uploads}")
    private String uploadRoot;

    public String savePhoto(Long jobId, MultipartFile file) {
        try {
            // clean original filename (example: mask_crop.png)
            String cleanFilename = StringUtils.cleanPath(file.getOriginalFilename());

            // <project>/uploads/job-1
            Path jobDir = Paths.get(uploadRoot, "job-" + jobId);

            // make sure the folder exists
            Files.createDirectories(jobDir);

            // final path: <project>/uploads/job-1/mask_crop.png
            Path target = jobDir.resolve(cleanFilename);

            // save file
            file.transferTo(target.toFile());

            return target.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo", e);
        }
    }
}
