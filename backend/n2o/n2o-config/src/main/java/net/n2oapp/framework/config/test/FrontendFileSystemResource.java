package net.n2oapp.framework.config.test;

import org.springframework.core.io.FileSystemResource;

/**
 * Ресурс в директории frontend модуля.
 */
public class FrontendFileSystemResource extends FileSystemResource {

    private static String FRONTEND_RELATIVE_PATH = "../../../frontend/n2o/src/";

    public FrontendFileSystemResource(String path) {
        super(FRONTEND_RELATIVE_PATH + path);
    }
}
