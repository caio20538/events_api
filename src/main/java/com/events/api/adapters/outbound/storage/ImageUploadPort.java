package com.events.api.adapters.outbound.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadPort {
    String uploadImage(MultipartFile file);
}
