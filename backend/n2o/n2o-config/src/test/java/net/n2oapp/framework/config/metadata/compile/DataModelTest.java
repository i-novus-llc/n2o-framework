package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

/**
 * Тесты для {@link DataModel}
 */
public class DataModelTest {

    @Test
    public void add() {
        DataModel model = new DataModel();
        assertThat(model.add(new ModelLink(ReduxModel.RESOLVE, "w1", "f1"), 1), nullValue());
        assertThat(model.getValue(new ModelLink(ReduxModel.RESOLVE, "w1", "f1")), is(1));
        assertThat(model.getValue(new ModelLink(ReduxModel.RESOLVE, "w1"), "f1"), is(1));
        assertThat(model.getData(new ModelLink(ReduxModel.RESOLVE, "w1")), hasEntry("f1", 1));

        ModelLink link = new ModelLink(ReduxModel.RESOLVE, "w1");
        link.setValue("`f1`");
        assertThat(model.add(link, 2), is(1));
        assertThat(model.getValue(link), is(2));
        assertThat(model.getData(link), hasEntry("f1", 2));
    }

    @Test
    public void addAll() {
        DataModel model = new DataModel();
        Map<String, ModelLink> pathMappings = new HashMap<>();
        pathMappings.put("master_id", new ModelLink(ReduxModel.RESOLVE, "master", "id"));
        pathMappings.put("detail_id", new ModelLink(ReduxModel.RESOLVE, "detail", "id"));
        DataSet params = new DataSet();
        params.put("master_id", 1);
        params.put("detail_id", 2);

        model.addAll(pathMappings, params);
        assertThat(model.getData(new ModelLink(ReduxModel.RESOLVE, "master")), hasEntry("id", 1));
        assertThat(model.getData(new ModelLink(ReduxModel.RESOLVE, "detail")), hasEntry("id", 2));
    }

}
