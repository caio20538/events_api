package com.events.api.adapters.outbound.storage;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class ImageUploadAdapter {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public ImageUploadAdapter(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }


    public String uploadImage(MultipartFile multipartFile){
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartFile(multipartFile);
            s3Client.putObject(bucketName,fileName, file);
            file.delete();
            return s3Client.getUrl(bucketName, fileName).toString();
        }catch (Exception e){
            System.out.println("Error ao subir arquivo");
            return "";
        }
    }

    private File convertMultipartFile(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(multipartFile.getBytes());
        fos.close();

        return convertFile;
    }
}
