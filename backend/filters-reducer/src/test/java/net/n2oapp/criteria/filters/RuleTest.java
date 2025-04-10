package net.n2oapp.criteria.filters;

import net.n2oapp.criteria.filters.rule.Eq_Eq;
import net.n2oapp.criteria.filters.rule.or.EqOrIsNull_Eq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
public class RuleTest {

    @Test
    void testEq_Eq(CapturedOutput output) {
        Eq_Eq rule = new Eq_Eq();
        Filter left = new Filter(1, FilterType.eq);
        Filter right = new Filter(1L, FilterType.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eq> и <eq> со значениями 1, имеющие разные типы Integer и Long");

        right = new Filter(2, FilterType.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eq> и <eq> со значениями 1 и 2");
    }

    @Test
    void testEqOrIsNull_Eq(CapturedOutput output) {
        EqOrIsNull_Eq rule = new EqOrIsNull_Eq();
        Filter left = new Filter(1, FilterType.eqOrIsNull);
        Filter right = new Filter(1L, FilterType.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями 1, имеющие разные типы Integer и Long");

        right = new Filter(2, FilterType.eq);
        rule.simplify(left, right);
        assertThat(output.getOut()).contains("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями 1 и 2");
    }
}