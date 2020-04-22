package uk.ac.leeds.comp2913.api.Util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class S3Client {

    final AmazonS3 amazonS3Client;

    @Value("${s3.bucket.name}")
    String defaultBucketName;

    @Value("${s3.default.folder}")
    String defaultBaseFolder;

    public S3Client(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public List<Bucket> getAllBuckets() {
        return amazonS3Client.listBuckets();
    }


    public PutObjectResult uploadFile(File uploadFile) {
        return amazonS3Client.putObject(defaultBucketName, uploadFile.getName(), uploadFile);
    }

    public byte[] getFile(String file) {
        S3Object obj = amazonS3Client.getObject(defaultBucketName, file);
        S3ObjectInputStream stream = obj.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

