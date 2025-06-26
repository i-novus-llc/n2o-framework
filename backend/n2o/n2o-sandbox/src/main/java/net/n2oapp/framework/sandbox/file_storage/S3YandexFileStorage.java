package net.n2oapp.framework.sandbox.file_storage;

import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import org.apache.commons.io.IOUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс хранения файлов в Yandex S3 хранилище
 */
public class S3YandexFileStorage implements FileStorage {
    private final String bucketName;
    private final S3Client s3Client;

    public S3YandexFileStorage(S3Client s3yandexClient, String bucketName) {
        this.bucketName = bucketName;
        this.s3Client = s3yandexClient;
        createBucketIfNotExists();
    }

    private static String getObjectId(String projectId, String file) {
        return projectId + "/" + file;
    }

    @Override
    public void saveFile(String projectId, String file, String source) {
        String objectKey = getObjectId(projectId, file);
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build(),
                RequestBody.fromString(source)
        );
    }

    @Override
    public String getFileContent(String projectId, String file) {
        if (!isFileExists(projectId, file)) {
            return null;
        }
        try (InputStream inputStream = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(getObjectId(projectId, file))
                        .build(),
                ResponseTransformer.toInputStream())) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<FileModel> getProjectFiles(String projectId) {
        List<FileModel> files = new ArrayList<>();
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(projectId + "/")
                .build();

        for (S3Object s3Object : s3Client.listObjectsV2(listRequest).contents()) {
            if (s3Object.key().endsWith("/")) continue;

            FileModel fileModel = new FileModel();
            String fileName = s3Object.key().substring(s3Object.key().lastIndexOf("/") + 1);
            fileModel.setFile(fileName);
            fileModel.setSource(getFileContent(projectId, fileName));
            files.add(fileModel);
        }
        return files;
    }

    @Override
    public boolean isProjectExists(String projectId) {
        return isFileExists(projectId, "index.page.xml");
    }

    private boolean isFileExists(String projectId, String file) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(getObjectId(projectId, file))
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            throw new IllegalStateException(e);
        }
    }

    private void createBucketIfNotExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        }
    }
}