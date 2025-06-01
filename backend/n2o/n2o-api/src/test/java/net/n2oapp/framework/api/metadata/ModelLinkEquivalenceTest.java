package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelLinkEquivalenceTest {

    @Test
    void testSimple() {
        ModelLink link1 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "id");
        ModelLink link2 = new ModelLink(ReduxModelEnum.RESOLVE, "widget");
        link2.setValue("`id`");
        assertTrue(link1.equalsLink(link2));
    }

    @Test
    void testEquals() {
        ModelLink withSubModelQuery1 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "multi[index].field.id");
        SubModelQuery subModelQuery1 = new SubModelQuery("multi[index]", "field", "queryId", "id", "name", false,null);
        withSubModelQuery1.setSubModelQuery(subModelQuery1);

        ModelLink withSubModelQuery2 = new ModelLink(ReduxModelEnum.RESOLVE, "widget");
        SubModelQuery subModelQuery2 = new SubModelQuery(null, "queryId", "id", "name", false, null);
        withSubModelQuery2.setValue("`multi[index].field.id`");
        withSubModelQuery2.setSubModelQuery(subModelQuery2);

        ModelLink modelLink1 = new ModelLink(ReduxModelEnum.RESOLVE, "widget");
        modelLink1.setValue("`multi[index].field.name`");
        ModelLink modelLink2 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "multi[index].field");
        ModelLink modelLink3 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "multi[index].field.name");

        assertFalse(withSubModelQuery1.equalsLink(modelLink1));
        assertTrue(withSubModelQuery1.equalsLink(modelLink2));
        assertTrue(withSubModelQuery1.equalsLink(modelLink3));

        assertTrue(withSubModelQuery2.equalsLink(modelLink1));
        assertFalse(withSubModelQuery2.equalsLink(modelLink2));
        assertFalse(withSubModelQuery2.equalsLink(modelLink3));

        assertFalse(modelLink1.equalsLink(withSubModelQuery1));
        assertTrue(modelLink2.equalsLink(withSubModelQuery1));
        assertTrue(modelLink3.equalsLink(withSubModelQuery1));

        assertTrue(modelLink1.equalsLink(withSubModelQuery2));
        assertFalse(modelLink2.equalsLink(withSubModelQuery2));
        assertFalse(modelLink3.equalsLink(withSubModelQuery2));

        assertTrue(modelLink3.equalsLink(modelLink2));
    }

    @Test
    void testNotEquals() {
        ModelLink withSubModelQuery1 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "field.id");
        SubModelQuery subModelQuery1 = new SubModelQuery("field", "queryId", "id", "name", false, null);
        withSubModelQuery1.setSubModelQuery(subModelQuery1);

        ModelLink withSubModelQuery3 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "id");
        SubModelQuery subModelQuery3 = new SubModelQuery(null, "queryId", null, null, false, null);
        withSubModelQuery3.setSubModelQuery(subModelQuery3);

        ModelLink modelLink1 = new ModelLink(ReduxModelEnum.RESOLVE, "widget1");
        modelLink1.setValue("`field.name`");
        ModelLink modelLink2 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "field1");
        modelLink2.setValue("`name`");
        ModelLink modelLink3 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "field");

        assertFalse(withSubModelQuery1.equalsLink(modelLink1));
        assertFalse(withSubModelQuery1.equalsLink(modelLink2));
        assertTrue(withSubModelQuery3.equalsLink(modelLink3));

        assertFalse(modelLink1.equalsLink(modelLink2));

        assertFalse(withSubModelQuery1.equalsLink(null));
        assertFalse(withSubModelQuery1.equalsLink(new Object()));
    }

    @Test
    void testEqualsNormalizedLink() {
        ModelLink link1 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "id");
        ModelLink link2 = new ModelLink(ReduxModelEnum.RESOLVE, "widget");
        link2.setValue("`id`");
        assertTrue(link1.equalsLink(link2));

        link1 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "person.id");
        link2 = new ModelLink(ReduxModelEnum.RESOLVE, "widget", "person");
        link2.setValue("`id`");
        assertTrue(link1.equalsLink(link2));

        link1 = new ModelLink(":rec_id");
        link2 = new ModelLink();
        link2.setParam("superrec_id");
        assertFalse(link1.equalsLink(link2));
    }
}
