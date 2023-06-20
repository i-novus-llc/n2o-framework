package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.io.action.v2.ifelse.ElseBranchActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.ifelse.ElseIfBranchActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.ifelse.IfBranchActionElementIOV2;
import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;

import org.junit.jupiter.api.Test;

/**
 * Тестирование чтение/записи элементов оператора if-else версии 2.0
 */
public class ConditionBranchElementIOV2Test {

    @Test
    void testConditionBranchElementIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new IfBranchActionElementIOV2(), new ElseIfBranchActionElementIOV2(),
                new ElseBranchActionElementIOV2(), new InvokeActionElementIOV2(), new AnchorElementIOV2(),
                new ShowModalElementIOV2(), new AlertActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/v2/testIfElseActionElementIOV2Test.page.xml");
    }
}
