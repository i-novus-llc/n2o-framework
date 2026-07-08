package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.config.io.action.ifelse.ElseBranchActionElementIOV2;
import net.n2oapp.framework.config.io.action.ifelse.ElseIfBranchActionElementIOV2;
import net.n2oapp.framework.config.io.action.ifelse.IfBranchActionElementIOV2;
import net.n2oapp.framework.config.io.page.StandardPageElementIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтение/записи элементов оператора if-else версии 2.0
 */
class ConditionBranchElementIOV2Test {

    @Test
    void testConditionBranchElementIOV2Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv4(), new IfBranchActionElementIOV2(), new ElseIfBranchActionElementIOV2(),
                new ElseBranchActionElementIOV2(), new InvokeActionElementIOV2(), new AnchorElementIOV2(),
                new ShowModalElementIOV2(), new AlertActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/action/testIfElseActionElementIOV2Test.page.xml");
    }
}
