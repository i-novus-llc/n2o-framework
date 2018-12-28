package net.n2oapp.framework.config.audit.service;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.persister.MetadataPersister;
import net.n2oapp.framework.config.register.ConfigId;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.audit.model.N2oConfigHistory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * Получение истории изменений одного файла
 */
public class N2oHistoryService implements CollectionPageService<N2oHistoryCriteria, N2oConfigHistory> {
    private N2oConfigAudit configAudit;
    private MetadataPersister metadataPersister;
    private SourceTypeRegister sourceTypeRegister;

    public N2oHistoryService() {
        this.configAudit = getBean(N2oConfigAudit.class);
        this.metadataPersister = getBean(MetadataPersister.class);
        this.sourceTypeRegister = getBean(SourceTypeRegister.class);
    }

    @Override
    public CollectionPage<N2oConfigHistory> getCollectionPage(N2oHistoryCriteria criteria) {
        if (criteria.getLocalPath() == null)
            throw new IllegalArgumentException("localPath is required filter!");

        List<N2oConfigHistory> res = configAudit.getHistory(criteria.getLocalPath());
        if (criteria.getId() != null) {
            res = res.stream().filter(h -> h.getId().equals(criteria.getId())).collect(Collectors.toList());
        }
        return N2oCommitService.filterAndMap(criteria, res);
    }

    /**
     * Восстановить содержимое файла из коммита
     * @param localPath локальный путь к файлу
     * @param revision ревизия комита
     */
    public void restoreTo(String localPath, String revision) {
        Optional<N2oConfigHistory> commit = configAudit.getHistory(localPath).stream().filter(c -> c.getId().equals(revision)).findFirst();
        if (!commit.isPresent())
            throw new N2oException("Revision " + revision + " not found");
        ConfigId configId = RegisterUtil.getConfigIdByLocalPath(localPath, sourceTypeRegister);
        metadataPersister.persist(configId.getId(), configId.getBaseSourceClass(), commit.get().getContent(), null);
    }

}
