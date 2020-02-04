package net.n2oapp.framework.boot;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SPAResolver implements ResourceResolver {
    private Resource index = new ClassPathResource("META-INF/resources/index.html");
    private List<String> ignoredPaths = Arrays.asList("n2o", "api", "rest", "docs");

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return resolve(requestPath, locations);
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        Resource resolvedResource = resolve(resourcePath, locations);
        if (resolvedResource == null) {
            return null;
        }
        try {
            return resolvedResource.getURL().toString();
        } catch (IOException e) {
            return resolvedResource.getFilename();
        }
    }

    private Resource resolve(String requestPath, List<? extends Resource> locations) {
        if (isIgnored(requestPath)) {
            return null;
        }
        if (isHandled(requestPath)) {
            return new ClassPathResource("META-INF/resources/" + getStaticPath(requestPath));
        }
        return index;
    }

    private String getStaticPath(String requestPath) {
        int index = requestPath.indexOf("static/");
        return requestPath.substring(index);
    }

    private boolean isIgnored(String path) {
        for (String ignoredPath : ignoredPaths) {
            if (path.startsWith(ignoredPath))
                return true;
        }
        return false;
    }

    private boolean isHandled(String path) {
        String extension = StringUtils.getFilenameExtension(path);
        return path.contains("static/") && extension != null;
    }
}