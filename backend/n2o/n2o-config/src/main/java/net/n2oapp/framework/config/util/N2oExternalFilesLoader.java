package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.util.ExternalFilesLoader;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class N2oExternalFilesLoader implements ExternalFilesLoader {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public String getContentByUri(String uri) {
        try (InputStream io = getContentAsStream(uri)) {
            return io == null ? null : IOUtils.toString(io, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private InputStream getContentAsStream(String path) throws IOException {
        if (path == null)
            return null;
        Resource resource = resourceLoader.getResource(path);
        if (!resource.exists())
            throw new IllegalArgumentException("File '" + path + "' not found");
        return resource.getInputStream();
    }
}
