package net.n2oapp.framework.sandbox;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class WebStaticResolver implements ResourceResolver {
    private String baseLocation;

    public WebStaticResolver(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return new ClassPathResource(baseLocation + removeProjectPathVariable(requestPath));
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return removeProjectPathVariable(resourcePath);
    }

    private static String removeProjectPathVariable(String requestPath) {
        return requestPath.substring(requestPath.indexOf('/'));
    }
}
