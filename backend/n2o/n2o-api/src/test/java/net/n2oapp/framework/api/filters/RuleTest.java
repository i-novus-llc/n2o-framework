package net.n2oapp.framework.api.filters;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.rule.EqAndEq;
import net.n2oapp.framework.api.criteria.filters.rule.or.EqOrIsNullAndEq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class RuleTest {

    @Test
    void testEq_Eq(CapturedOutput output) {
        EqAndEq rule = new EqAndEq();
        Filter left = new Filter(1, FilterTypeEnum.EQ);
        Filter right = new Filter(1L, FilterTypeEnum.EQ);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eq> и <eq> со значениями 1, имеющие разные типы Integer и Long");

        right = new Filter(2, FilterTypeEnum.EQ);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eq> и <eq> со значениями 1 и 2");
    }

    @Test
    void testEqOrIsNull_Eq(CapturedOutput output) {
        EqOrIsNullAndEq rule = new EqOrIsNullAndEq();
        Filter left = new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL);
        Filter right = new Filter(1L, FilterTypeEnum.EQ);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями 1, имеющие разные типы Integer и Long");

        right = new Filter(2, FilterTypeEnum.EQ);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями 1 и 2");
    }
}