package net.n2oapp.framework.api.metadata;

import org.junit.Test;

import static net.n2oapp.framework.api.ui.FormModel.*;

/**
 * User: operehod
 * Date: 23.03.2015
 * Time: 11:06
 */
public class FormModelTest {

    @Test
    public void test() throws Exception {
        assert getByName("query") == QUERY;
        assert getByName("default") == DEFAULT;
        assert getByName("query-or-default") == QUERY_OR_DEFAULT;
        assert getByName("copy") == COPY;
        assert getByName(null) == null;
    }
}
