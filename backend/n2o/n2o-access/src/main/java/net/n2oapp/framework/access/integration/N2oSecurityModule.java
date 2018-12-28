package net.n2oapp.framework.access.integration;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.processing.N2oModule;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

/**
 * requestInfo.getUser(): operhod
 * Date: 25.11.13
 * Time: 12:53
 */
public class N2oSecurityModule extends N2oModule {

    private SecurityProvider securityProvider;

    public N2oSecurityModule(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    public void setSecurityProvider(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @Override
    public void processAction(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        securityProvider.checkAccess(requestInfo);

    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        if (requestInfo.getUpload().equals(UploadType.query)) {
            securityProvider.checkAccess(requestInfo);
        }
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        if (requestInfo.getUpload().equals(UploadType.query)) {
            securityProvider.checkAccess(requestInfo);
        }
    }
}
