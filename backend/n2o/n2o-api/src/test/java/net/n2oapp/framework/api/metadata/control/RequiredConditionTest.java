package net.n2oapp.framework.api.metadata.control;

import net.n2oapp.framework.api.metadata.control.properties.RequiredCondition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.junit.Test;

/**
 * @author operehod
 * @since 24.11.2015
 */
public class RequiredConditionTest {


    @Test
    public void testSimple() throws Exception {
        RequiredCondition condition = new RequiredCondition();
        condition.setCondition("name == 'Олег' && gender.id == 1");
        condition.prepareForClient(ScriptProcessor.getInstance());
        assert condition.getVars().size() == 2;
        assert condition.getVars().get("$name").equals("name");
        assert condition.getVars().get("$genderid").equals("gender.id");
        assert condition.getClientCondition().equals("$name == 'Олег' && $genderid == 1");

        condition = new RequiredCondition();
        condition.setCondition("name == 'Олег' && gender.id == 1");
        condition.setOn("name");
        condition.prepareForClient(ScriptProcessor.getInstance());
        assert condition.getVars().size() == 1;
        assert condition.getVars().get("$name").equals("name");
        assert condition.getClientCondition().equals("$name == 'Олег' && gender.id == 1");
    }
}
