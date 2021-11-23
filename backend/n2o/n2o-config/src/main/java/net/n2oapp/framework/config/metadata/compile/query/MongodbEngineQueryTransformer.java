package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.hash;

/**
 * Трансформация query для провайдера данных mongodb.
 * Генерирует тело для фильтров, <select/>, <sorting/>.
 */
@Component
public class MongodbEngineQueryTransformer implements SourceTransformer<N2oQuery>, SourceClassAware {

    public static final String EXPRESSION = "expression";

    @Override
    public N2oQuery transform(N2oQuery source, ValidateProcessor p) {
        if (!isMongodb(source))
            return source;
        if (source.getFields() != null) {
            for (N2oQuery.Field field : source.getFields()) {
                if (Boolean.FALSE.equals(field.getNoDisplay()) && field.getSelectBody() == null) {
                    transformSelect(field);
                }
                if (Boolean.FALSE.equals(field.getNoSorting()) && field.getSortingBody() == null) {
                    transformSortings(field);
                }
                if (field.getFilterList() != null) {
                    transformFilters(field);
                }
            }
        }
        return source;
    }

    private void transformSelect(N2oQuery.Field field) {
        if (field.getId().equals("id")) {
            field.setSelectBody("_id");
            field.setSelectMapping("['_id'].toString()");
        } else {
            field.setSelectBody(colon(EXPRESSION));
        }
    }

    private void transformSortings(N2oQuery.Field field) {
        if (field.getId().equals("id")) {
            field.setSortingBody("_id :idDirection");
        } else {
            field.setSortingBody(colon(EXPRESSION) + " " + colon(field.getId() + "Direction"));
        }
    }

    private void transformFilters(N2oQuery.Field field) {
        for (N2oQuery.Filter filter : field.getFilterList()) {
            String domain = getDomain(filter);
            if (filter.getFilterField() == null)
                filter.setFilterField(RouteUtil.normalizeParam(field.getId()) + "_" + filter.getType());
            if (filter.getText() == null) {
                if (field.getId().equals("id")) {
                    if (filter.getType().equals(FilterType.eq))
                        filter.setText("{ _id: new ObjectId('#" + filter.getFilterField() + "') }");
                } else {
                    switch (filter.getType()) {
                        case eq:
                            filter.setText("{ '" + colon(EXPRESSION) + "': " + getFilterField(filter, domain) + " }");
                            break;
                        case notEq:
                            filter.setText("{ '" + colon(EXPRESSION) + "': {$ne: " + getFilterField(filter, domain) + " }}");
                            break;
                        case like:
                            filter.setText("{ '" + colon(EXPRESSION) + "': {$regex: '.*" + hash(filter.getFilterField()) + ".*'}}");
                            break;
                        case likeStart:
                            filter.setText("{ '" + colon(EXPRESSION) + "': {$regex: '" + hash(filter.getFilterField()) + ".*'}}");
                            break;
                        case more:
                            filter.setText("{ '" + colon(EXPRESSION) + "': {$gte: " + getFilterField(filter, domain) + "}}");
                            break;
                        case less:
                            filter.setText("{ '" + colon(EXPRESSION) + "': {$lte: " + getFilterField(filter, domain) + "}}");
                            break;
                        case in:
                            filter.setText("{ '" + colon(EXPRESSION) + "': {$in: " + getFilterField(filter, domain) + "}}");
                            break;
                        case notIn:
                            filter.setText("{ '" + colon(EXPRESSION) + "': {$nin: " + getFilterField(filter, domain) + "}}");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private String getFilterField(N2oQuery.Filter filter, String domain) {
        if (domain.equals("string") || domain.equals("date") || domain.equals("localdate") || domain.equals("localdatetime"))
            return "'" + hash(filter.getFilterField()) + "'";
        return hash(filter.getFilterField());
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQuery.class;
    }

    private static String getDomain(N2oQuery.Filter filter) {
        return filter.getDomain() != null ? filter.getDomain().toLowerCase().trim() : "string";
    }

    private boolean isMongodb(N2oQuery source) {
        return checkMondodb(source.getLists())
                && checkMondodb(source.getUniques())
                && checkMondodb(source.getCounts());
    }

    private boolean checkMondodb(N2oQuery.Selection[] selection) {
        if (selection == null) return true;
        return Arrays.stream(selection).noneMatch(elem -> !(elem.getInvocation() instanceof N2oMongoDbDataProvider));
    }
}
