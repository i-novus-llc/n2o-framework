package net.n2oapp.framework.config.audit.service;

import net.n2oapp.criteria.api.FilteredCollectionPage;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.properties.StaticProperties;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * Получение списка удаленных веток за исключением серверной ветки(той на которой сейчас находимся)
 */
public class BranchService implements CollectionPageService<BranchCriteria, DataSet> {
    private static final String BRANCHES_PROP = "n2o.config.audit.merge.branches";

    private MessageSourceAccessor messageSourceAccessor;

    public BranchService() {
        this.messageSourceAccessor = getBean(MessageSourceAccessor.class);
    }

    @Override
    public CollectionPage<DataSet> getCollectionPage(BranchCriteria branchCriteria) {
        String branchesProperty = StaticProperties.get(BRANCHES_PROP);
        List<DataSet> res = new ArrayList<>();
        if (branchesProperty != null && !branchesProperty.isEmpty()) {
            String[] branches = branchesProperty.split(",");
            for (String br : branches) {
                DataSet branch = new DataSet();
                branch.put("id", br);
                String name = messageSourceAccessor.getMessage(BRANCHES_PROP + "." + br);
                branch.put("name", name == null ? br : name);
                res.add(branch);
            }
        }
        return new FilteredCollectionPage<>(res, branchCriteria);
    }
}
