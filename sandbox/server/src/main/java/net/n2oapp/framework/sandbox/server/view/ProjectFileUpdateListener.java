package net.n2oapp.framework.sandbox.server.view;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.storage.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static net.n2oapp.framework.sandbox.server.view.operations.SandboxSourceCacheOperation.CACHE_REGION;

public class ProjectFileUpdateListener implements N2oEventListener<ProjectFileUpdateEvent> {

    @Value("${n2o.config.path}")
    private String basePath;

    private final SourceTypeRegister sourceTypeRegister;
    private CacheTemplate cacheTemplate;

    public ProjectFileUpdateListener(SourceTypeRegister sourceTypeRegister, CacheManager cacheManager) {
        this.sourceTypeRegister = sourceTypeRegister;
        this.cacheTemplate = new CacheTemplate(cacheManager);
    }

    @Override
    public void handleEvent(ProjectFileUpdateEvent projectFileUpdateEvent) {
        Cache cache = cacheTemplate.getCacheManager().getCache(CACHE_REGION);
        if (cache != null) {
            SourceInfo sourceInfo = RegisterUtil.createFolderInfo(Node.byAbsolutePath(projectFileUpdateEvent.getFileName(), basePath),
                    sourceTypeRegister);
            cacheTemplate.getCacheManager().getCache(CACHE_REGION)
                    .evict(getKey(projectFileUpdateEvent.getProjectId(), sourceInfo.getId(), sourceInfo.getBaseSourceClass()));
        }
    }

    private String getKey(String projectId, String id, Class<? extends SourceMetadata> sourceClass) {
        return projectId + "." + id + "." + sourceClass.getSimpleName();
    }
}
