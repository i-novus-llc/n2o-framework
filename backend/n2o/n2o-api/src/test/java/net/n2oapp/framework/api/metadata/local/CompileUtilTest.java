package net.n2oapp.framework.api.metadata.local;

import net.n2oapp.framework.api.metadata.control.N2oFieldCondition;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;
import org.junit.Test;

/**
 * @author operehod
 * @since 19.06.2015
 */
public class CompileUtilTest {


    @Test
    public void testCreateConditionOnBase() {
        //нету базового
        N2oFieldCondition condition = CompileUtil.createConditionOnBase(null, "gender.id==id");
        assert condition.getCondition().equals("gender.id==id");
        assert condition.getOn().equals("id,gender.id");

        //есть базовый без on
        N2oFieldCondition base = new N2oFieldCondition("1==2");
        condition = CompileUtil.createConditionOnBase(base, "gender.id==id");
        assert condition.getCondition().equals("(gender.id==id) && (1==2)");
        assert condition.getOn().equals("id,gender.id");

        //есть базовый с on
        base = new N2oFieldCondition("name==2", "name");
        condition = CompileUtil.createConditionOnBase(base, "gender.id==id");
        assert condition.getCondition().equals("(gender.id==id) && (name==2)");
        assert condition.getOn().equals("name,id,gender.id");

    }
}
