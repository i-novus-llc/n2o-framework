package net.n2oapp.framework.config.audit.service.conflict;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.register.audit.model.N2oConfigConflict;

import java.util.ArrayList;

/**
 * Сервис разрешения конфликтов после мержа
 */
public class MetaDataConflictCriteriaService implements CollectionPageService<MetaDataConflictCriteria, DataSet> {
    @Override
    public CollectionPage<DataSet> getCollectionPage(MetaDataConflictCriteria criteria) {
        ArrayList<DataSet> result = new ArrayList<DataSet>();
        result.add(new DataSet() {
        });
        return new FilteredCollectionPage<>(result, criteria);
    }
}
