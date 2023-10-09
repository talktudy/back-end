package com.example.talktudy.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    public static final String S3_DIR_MEMBER = "member";
    public static final String S3_DIR_PORTFOLIO = "portfolio";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    // 다중 파일 업로드시
    public List<String> upload(List<MultipartFile> files, String dirName) {
        List<String> fileUrls = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            log.info("파일이 비어있습니다.");
            return fileUrls;
        }

        for (MultipartFile file : files) {
            try {
                String fileUrl = uploadFile(file, dirName);
                fileUrls.add(fileUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return fileUrls;
    }

    private String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
        UUID uuid = UUID.randomUUID();
        String originName = multipartFile.getOriginalFilename();
        String fileName = dirName + "/" + uuid + "_" + originName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,
                multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    // 단일 파일 업로드시
    public String uploadFile(MultipartFile file, String s3UploadDir) {
        File convertedFile = convert(file).orElseThrow(() -> new RuntimeException("MultipartFile => File 변환에 실패하였습니다."));
        String uploadedFileUrl = putS3(convertedFile, s3UploadDir, file.getOriginalFilename());
        return uploadedFileUrl;
    }

    public String putS3(File uploadFile, String dirName, String originName) {
        UUID uuid = UUID.randomUUID();
        String fileName = dirName + "/" + uuid + "_"; //  + originName; originName을 넣으면 너무 길 경우 db에 부하
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    public Optional<File> convert(MultipartFile file) {
        try {
            File convertedFile = File.createTempFile("temp", null);
            file.transferTo(convertedFile);
            return Optional.of(convertedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
} // end class
