package net.n2oapp.framework.sandbox.file_storage;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MinioFileStorage implements FileStorage {

    private String bucketName;

    private MinioClient minioClient;

    public MinioFileStorage(String minioUrl, String accessKey, String secretKey, String bucketName) {
        this.bucketName = bucketName;
        try {
            this.minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, secretKey)
                    .build();

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveFile(String projectId, String file, String source) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(source.getBytes());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(getObjectId(projectId, file))
                            .stream(byteArrayInputStream, source.length(), -1)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileContent(String projectId, String file) {
        try {
            if (!isFileExists(projectId, file))
                return null;
            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(getObjectId(projectId, file))
                            .build())) {
                return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getObjectId(String projectId, String file) {
        return projectId + "/" + file;
    }

    @Override
    public List<FileModel> getProjectFiles(String projectId) {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(projectId + "/").build();
        List<FileModel> files = new ArrayList<>();
        try {
            for (Result<Item> itemResult : minioClient.listObjects(listObjectsArgs)) {
                FileModel fileModel = new FileModel();
                String fileName = itemResult.get().objectName().substring(itemResult.get().objectName().lastIndexOf("/") + 1);
                fileModel.setFile(fileName);
                fileModel.setSource(getFileContent(projectId, fileName));
                files.add(fileModel);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    @Override
    public boolean isProjectExists(String projectId) {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(projectId + "/").build();
        return minioClient.listObjects(listObjectsArgs).iterator().hasNext();
    }

    private boolean isFileExists(String projectId, String file) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(getObjectId(projectId, file)).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
