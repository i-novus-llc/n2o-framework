package net.n2oapp.framework.api.metadata.meta;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ModelsTest {
    
    @Test
    void test() {
        Models models = new Models();
        models.add(new ModelLink(ReduxModelEnum.resolve, "w1", "f1.id"), 1);
        models.add(new ModelLink(ReduxModelEnum.resolve, "w1", "f2"), new DefaultValues().add("id", 2));
        assertThat(models.get(new ModelLink(ReduxModelEnum.resolve, "w1", "f1.id")), Matchers.is(new ModelLink(1)));
        assertThat(models.get(new ModelLink(ReduxModelEnum.resolve, "w1", "f2")), Matchers.is(new ModelLink(new DefaultValues().add("id", 2))));
    }
}