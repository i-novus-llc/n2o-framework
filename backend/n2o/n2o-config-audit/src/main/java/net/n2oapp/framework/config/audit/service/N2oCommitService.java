package net.n2oapp.framework.config.audit.service;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.audit.git.N2oGitCore;
import net.n2oapp.framework.config.register.audit.model.N2oConfigCommit;
import net.n2oapp.framework.config.register.audit.model.N2oConfigHistory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Получение истории изменений
 */
public class N2oCommitService implements CollectionPageService<N2oCommitCriteria, N2oConfigCommit> {
    private static final String BRANCHES_PROP = "n2o.config.audit.merge.branches";

    @Override
    public CollectionPage<N2oConfigCommit> getCollectionPage(N2oCommitCriteria criteria) {
        N2oConfigAudit configAudit = StaticSpringContext.getBean(N2oConfigAudit.class);
        List<N2oConfigCommit> res = configAudit.getCommits();
        return filterAndMap(criteria, res);
    }

    public static <T extends N2oConfigCommit> CollectionPage<T> filterAndMap(N2oCommitCriteria criteria, List<T> res) {
        res = res.stream().filter(md ->
                        ((criteria.getDateBegin() == null) || (md.getDate() == null || md.getDate().compareTo(criteria.getDateBegin()) >= 0))
                                && (criteria.getDateEnd() == null || (md.getDate() == null || md.getDate().compareTo(criteria.getDateEnd()) <= 0))
                                && (criteria.getChangeType() == null || (criteria.getChangeType().contains(md.getType().toString())))
                                && (criteria.getMessage() == null || md.getMessage().toLowerCase().contains(criteria.getMessage().toLowerCase()))
                                && (criteria.getAuthor() == null || md.getAuthor().toLowerCase().contains(criteria.getAuthor().toLowerCase()))
        ).map(N2oCommitService::mapMessageBranch).collect(Collectors.toList());
        return new FilteredCollectionPage<>(res, criteria);
    }

    private static <T extends N2oConfigCommit> T mapMessageBranch(T commit) {
        if (N2oConfigHistory.Type.MERGE.equals(commit.getType())) {
            String branchName = commit.getMessage();
            if (branchName.equals(N2oGitCore.SYSTEM_BRANCH_NAME)) {
                commit.setMessage("n2o.audit.updateMerged");
            } else {
                String name = BRANCHES_PROP + "." + branchName;
                commit.setMessage(name == null ? branchName : name);
            }
        }
        return commit;
    }
}
