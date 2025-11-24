package com.example.demo.application.port.out;

import java.util.List;

import com.example.demo.domain.Entities.Attachment;

public interface AttachmentRepositoryPort {
    Attachment save(Attachment attachment);

    List<Attachment> findByJobId(Long jobId);
}
