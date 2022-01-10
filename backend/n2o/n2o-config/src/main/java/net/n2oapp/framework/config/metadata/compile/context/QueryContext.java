package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class QueryContext extends BaseCompileContext<CompiledQuery, N2oQuery> {
    private List<Filter> filters;
    private UploadType upload;
    private List<Validation> validations;
    @Deprecated
    private String failAlertWidgetId;
    @Deprecated
    private String successAlertWidgetId;
    @Deprecated
    private String messagesForm;
    private Integer querySize;
    private Map<String, String> sortingMap;
    private List<SubModelQuery> subModelQueries;
    private Set<String> copiedFields;
    private String urlPattern;

    public QueryContext(String queryId) {
        super(queryId, N2oQuery.class, CompiledQuery.class);
    }

    public QueryContext(String queryId, String route) {
        super(route, queryId, N2oQuery.class, CompiledQuery.class);
    }

    public QueryContext(String queryId, String route, String urlPattern) {
        super(route, queryId, N2oQuery.class, CompiledQuery.class);
        this.urlPattern = urlPattern;
    }

    @Override
    protected DataSet getResultData(String url, String urlPattern) {
        return super.getResultData(url, this.urlPattern == null ? urlPattern : this.urlPattern);
    }
}
