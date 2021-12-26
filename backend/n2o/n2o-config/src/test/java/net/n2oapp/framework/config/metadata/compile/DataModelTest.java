package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Тесты для {@link DataModel}
 */
public class DataModelTest {

    @Test
    public void add() {
        DataModel model = new DataModel();
        assertThat(model.add(new ModelLink(ReduxModel.resolve, "w1", "f1"), 1), nullValue());
        assertThat(model.getValue(new ModelLink(ReduxModel.resolve, "w1", "f1")), is(1));
        assertThat(model.getValue(new ModelLink(ReduxModel.resolve, "w1"), "f1"), is(1));
        assertThat(model.getData(new ModelLink(ReduxModel.resolve, "w1")), hasEntry("f1", 1));

        ModelLink link = new ModelLink(ReduxModel.resolve, "w1");
        link.setValue("`f1`");
        assertThat(model.add(link, 2), is(1));
        assertThat(model.getValue(link), is(2));
        assertThat(model.getData(link), hasEntry("f1", 2));
    }

    @Test
    public void addAll() {
        DataModel model = new DataModel();
        Map<String, ModelLink> pathMappings = new HashMap<>();
        pathMappings.put("master_id", new ModelLink(ReduxModel.resolve, "master", "id"));
        pathMappings.put("detail_id", new ModelLink(ReduxModel.resolve, "detail", "id"));
        DataSet params = new DataSet();
        params.put("master_id", 1);
        params.put("detail_id", 2);

        model.addAll(pathMappings, params);
        assertThat(model.getData(new ModelLink(ReduxModel.resolve, "master")), hasEntry("id", 1));
        assertThat(model.getData(new ModelLink(ReduxModel.resolve, "detail")), hasEntry("id", 2));
    }

    @Test
    public void getDataIfAbsent() {
        N2oSubModelsProcessor p = mock(N2oSubModelsProcessor.class);
        doAnswer(invocation -> {
            DataSet data = invocation.getArgument(1);
            if (data.get("id").equals(123))
                data.put("name", "Joe");
            return null;
        }).when(p).executeSubModels(anyListOf(SubModelQuery.class), anyObject());

        DataModel model = new DataModel();
        ModelLink link = new ModelLink(ReduxModel.resolve, "widget", "id");
        link.setSubModelQuery(new SubModelQuery("query"));
        model.add(link, 123);
        Function<String, Object> dataFunc = model.getDataIfAbsent(new ModelLink(ReduxModel.resolve, "widget"), p);
        assertThat(dataFunc.apply("name"), is("Joe"));
    }
}
