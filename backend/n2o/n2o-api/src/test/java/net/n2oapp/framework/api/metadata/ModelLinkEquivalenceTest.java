package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelLinkEquivalenceTest {

    @Test
    void testSimple() {
        ModelLink link1 = new ModelLink(ReduxModel.resolve, "widget", "id");
        ModelLink link2 = new ModelLink(ReduxModel.resolve, "widget");
        link2.setValue("`id`");
        assertTrue(link1.equalsLink(link2));
    }

    @Test
    void testEquals() {
        ModelLink withSubModelQuery1 = new ModelLink(ReduxModel.resolve, "widget", "field.id");
        SubModelQuery subModelQuery1 = new SubModelQuery("field", "queryId", "id", "name", false, null);
        withSubModelQuery1.setSubModelQuery(subModelQuery1);

        ModelLink withSubModelQuery2 = new ModelLink(ReduxModel.resolve, "widget");
        SubModelQuery subModelQuery2 = new SubModelQuery(null, "queryId", "id", "name", false, null);
        withSubModelQuery2.setValue("`field.id`");
        withSubModelQuery2.setSubModelQuery(subModelQuery2);

        ModelLink modelLink1 = new ModelLink(ReduxModel.resolve, "widget");
        modelLink1.setValue("`field.name`");
        ModelLink modelLink2 = new ModelLink(ReduxModel.resolve, "widget", "field");
        ModelLink modelLink3 = new ModelLink(ReduxModel.resolve, "widget", "field.name");
        ModelLink modelLink4 = new ModelLink(ReduxModel.resolve, "widget", "field");

        assertFalse(withSubModelQuery1.equalsLink(modelLink1));
        assertTrue(withSubModelQuery1.equalsLink(modelLink2));
        assertTrue(withSubModelQuery1.equalsLink(modelLink3));
        assertTrue(withSubModelQuery1.equalsLink(modelLink4));

        assertTrue(withSubModelQuery2.equalsLink(modelLink1));
        assertFalse(withSubModelQuery2.equalsLink(modelLink2));
        assertFalse(withSubModelQuery2.equalsLink(modelLink3));
        assertFalse(withSubModelQuery2.equalsLink(modelLink4));

        assertFalse(modelLink1.equalsLink(withSubModelQuery1));
        assertTrue(modelLink2.equalsLink(withSubModelQuery1));
        assertTrue(modelLink3.equalsLink(withSubModelQuery1));
        assertTrue(modelLink4.equalsLink(withSubModelQuery1));

        assertTrue(modelLink1.equalsLink(withSubModelQuery2));
        assertFalse(modelLink2.equalsLink(withSubModelQuery2));
        assertFalse(modelLink3.equalsLink(withSubModelQuery2));
        assertFalse(modelLink4.equalsLink(withSubModelQuery2));

        assertTrue(modelLink3.equalsLink(modelLink4));
    }

    @Test
    void testNotEquals() {
        ModelLink withSubModelQuery1 = new ModelLink(ReduxModel.resolve, "widget", "field.id");
        SubModelQuery subModelQuery1 = new SubModelQuery("field", "queryId", "id", "name", false, null);
        withSubModelQuery1.setSubModelQuery(subModelQuery1);

        ModelLink withSubModelQuery2 = new ModelLink(ReduxModel.resolve, "widget");
        SubModelQuery subModelQuery2 = new SubModelQuery(null, "queryId", "id", "name", false, null);
        withSubModelQuery2.setSubModelQuery(subModelQuery2);

        ModelLink withSubModelQuery3 = new ModelLink(ReduxModel.resolve, "widget", "id");
        SubModelQuery subModelQuery3 = new SubModelQuery(null, "queryId", null, null, false, null);
        withSubModelQuery3.setSubModelQuery(subModelQuery3);

        ModelLink modelLink1 = new ModelLink(ReduxModel.resolve, "widget1");
        modelLink1.setValue("`field.name`");
        ModelLink modelLink2 = new ModelLink(ReduxModel.resolve, "widget", "field1");
        modelLink2.setValue("`name`");
        ModelLink modelLink3 = new ModelLink(ReduxModel.resolve, "widget", "field");

        assertFalse(withSubModelQuery1.equalsLink(modelLink1));
        assertFalse(withSubModelQuery1.equalsLink(modelLink2));
        assertTrue(withSubModelQuery3.equalsLink(modelLink3));

        assertFalse(modelLink1.equalsLink(modelLink2));

        assertFalse(withSubModelQuery1.equalsLink(null));
        assertFalse(withSubModelQuery1.equalsLink(new Object()));
    }

    @Test
    void testEqualsNormalizedLink() {
        ModelLink link1 = new ModelLink(ReduxModel.resolve, "widget", "id");
        ModelLink link2 = new ModelLink(ReduxModel.resolve, "widget");
        link2.setValue("`id`");
        assertTrue(link1.equalsLink(link2));

        link1 = new ModelLink(ReduxModel.resolve, "widget", "person.id");
        link2 = new ModelLink(ReduxModel.resolve, "widget", "person");
        link2.setValue("`id`");
        assertTrue(link1.equalsLink(link2));

        link1 = new ModelLink(":rec_id");
        link2 = new ModelLink();
        link2.setParam("superrec_id");
        assertFalse(link1.equalsLink(link2));
    }
}
