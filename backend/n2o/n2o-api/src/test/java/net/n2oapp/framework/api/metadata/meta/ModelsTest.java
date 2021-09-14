package net.n2oapp.framework.api.metadata.meta;

import junit.framework.TestCase;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class ModelsTest extends TestCase {
    public void test() {
        Models models = new Models();
        models.add(new ModelLink(ReduxModel.RESOLVE, "w1", "f1.id"), 1);
        models.add(new ModelLink(ReduxModel.RESOLVE, "w1", "f2"), new DefaultValues().add("id", 2));
        assertThat(models.get(new ModelLink(ReduxModel.RESOLVE, "w1", "f1.id")), Matchers.is(new ModelLink(1)));
        assertThat(models.get(new ModelLink(ReduxModel.RESOLVE, "w1", "f2")), Matchers.is(new ModelLink(new DefaultValues().add("id", 2))));
    }
}