package net.n2oapp.framework.ui.controller.request;

import net.n2oapp.criteria.api.Direction;
import net.n2oapp.criteria.dataset.DataSet;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

@Deprecated
public class GetDataRequest  extends DataRequest {
    private String queryId;
    private String contentType;
    private DataSet filters = new DataSet();
    private LinkedHashMap<String, Direction> sortings = new LinkedHashMap<String, Direction>();
    private Integer page = 1;
    private Integer size = 15;
    private Integer count;
    private String model;
    private String querySource;
    private List<String> columns;

    public GetDataRequest(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public DataSet getFilters() {
        return filters;
    }

    public void setFilters(DataSet filters) {
        this.filters = filters;
    }

    public LinkedHashMap<String, Direction> getSortings() {
        return sortings;
    }

    public void setSortings(LinkedHashMap<String, Direction> sortings) {
        this.sortings = sortings;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getQuerySource() {
        return querySource;
    }

    public void setQuerySource(String querySource) {
        this.querySource = querySource;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
