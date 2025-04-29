package net.n2oapp.criteria.filters;

import net.n2oapp.criteria.filters.rule.Eq_Eq;
import net.n2oapp.criteria.filters.rule.or.EqOrIsNull_Eq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class RuleTest {

    @Test
    void testEq_Eq(CapturedOutput output) {
        Eq_Eq rule = new Eq_Eq();
        Filter left = new Filter(1, FilterTypeEnum.eq);
        Filter right = new Filter(1L, FilterTypeEnum.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eq> и <eq> со значениями 1, имеющие разные типы Integer и Long");

        right = new Filter(2, FilterTypeEnum.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eq> и <eq> со значениями 1 и 2");
    }

    @Test
    void testEqOrIsNull_Eq(CapturedOutput output) {
        EqOrIsNull_Eq rule = new EqOrIsNull_Eq();
        Filter left = new Filter(1, FilterTypeEnum.eqOrIsNull);
        Filter right = new Filter(1L, FilterTypeEnum.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями 1, имеющие разные типы Integer и Long");

        right = new Filter(2, FilterTypeEnum.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями 1 и 2");
    }
}