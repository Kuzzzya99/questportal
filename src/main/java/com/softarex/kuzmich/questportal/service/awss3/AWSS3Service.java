package com.softarex.kuzmich.questportal.service.awss3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.softarex.kuzmich.questportal.repository.FileRepository;
import com.softarex.kuzmich.questportal.service.comment.CommentService;
import com.softarex.kuzmich.questportal.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AWSS3Service {

    private AmazonS3 S3Client;
    private final FileService fileService;
    private final CommentService commentService;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Autowired
    public AWSS3Service(AmazonS3 S3Client,
                        FileService fileService,
                        CommentService commentService) {
        this.fileService = fileService;
        this.S3Client = S3Client;
        this.commentService = commentService;
    }

    public void uploadFile(MultipartFile file) {
        try {
            File fileObj = convertMultiPartToFile(file);
            this.S3Client.putObject(bucket, file.getOriginalFilename(), fileObj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllFiles() {
        ObjectListing objectListing = S3Client.listObjects(bucket);
        List<String> keys = new ArrayList<>();
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            keys.add(os.getKey());
        }
        return keys;
    }

    public void download(String fileName) throws IOException {
        try {
            S3Object o = S3Client.getObject(bucket, fileName);
            S3ObjectInputStream s3is = o.getObjectContent();
            String path = "C://Users/dkuzm/Desktop/pr/";
            String finalName = path + fileName;

            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(new File(finalName));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void deleteFile(String fileName) {
        try {
            this.S3Client.deleteObject(bucket, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}

