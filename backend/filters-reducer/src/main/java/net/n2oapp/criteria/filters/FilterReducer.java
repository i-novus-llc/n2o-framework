package net.n2oapp.criteria.filters;

import net.n2oapp.criteria.filters.rule.*;
import net.n2oapp.criteria.filters.rule.base.Rule;
import net.n2oapp.criteria.filters.rule.like.*;
import net.n2oapp.criteria.filters.rule.or.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Util for reduce filters in pairs as AND
 */
public class FilterReducer {

    private static Map<Pair, Rule> rulesMap;
    private static List<? extends Rule> ruleList = Arrays.asList(
            new Eq_Eq(), new Eq_In(), new Eq_Less(), new Eq_More(), new Eq_NotEq(), new Eq_IsNull(), new Eq_IsNotNull(), new Eq_NotIn(),
            new NotEq_NotEq(), new NotEq_NotIn(),
            new In_In(), new In_Less(), new In_More(), new In_IsNull(), new In_IsNotNull(), new In_NotEq(), new In_NotIn(),
            new IsNull_IsNotNull(), new IsNull_NotEq(), new IsNull_NotIn(),
            new Less_Less(), new More_More(), new Less_IsNull(), new More_IsNull(), new More_IsNotNull(), new Less_IsNotNull(),
            new NotIn_NotIn(),
            //eqOrIsNull
            new EqOrIsNull_Eq(), new EqOrIsNull_EqOrIsNull(), new EqOrIsNull_In(), new EqOrIsNull_IsNotNull(),
            new EqOrIsNull_IsNull(), new EqOrIsNull_Less(), new EqOrIsNull_More(), new EqOrIsNull_NotEq(), new EqOrIsNull_NotIn(),
            new EqOrIsNull_Like(), new EqOrIsNull_LikeStart(), new EqOrIsNull_Contains(),
            //like, likeStart
            new Eq_Like(), new Eq_LikeStart(), new Like_IsNotNull(), new Like_IsNull(), new Like_Like(), new Like_LikeStart(),
            new LikeStart_IsNotNull(), new LikeStart_IsNull(), new LikeStart_LikeStart(),
            //contains
            new Eq_Contains(), new Contains_NotEq(), new Contains_Contains(),
            new Contains_IsNull(), new Contains_IsNotNull()
    );


    static {
        rulesMap = new HashMap<>();
        for (Rule rule : ruleList) {
            Rule prev = rulesMap.put(rule.getType(), rule);
            if (prev != null)
                throw new IllegalStateException(format("more then one reduce-rule for [%s]", rule.getType()));
        }
    }

    /**
     * Unite two filters, if it is possible
     * @param left first filter
     * @param right  second filter
     * @return  filters union
     */
    @SuppressWarnings("unchecked")
    public static Result reduce(Filter left, Filter right) {
        Result res = new Result();
        res.setLeftFilter(left);
        res.setRightFilter(right);
        Rule rule = rulesMap.get(new Pair<>(left.getType(), right.getType()));
        if (rule == null)
            res.setType(Result.Type.notMergeable);
        else {
            Filter filter = rule.simplify(left, right);
            res.setResultFilter(filter);
            if (filter == null)
                res.setType(Result.Type.conflict);
            else
                res.setType(Result.Type.success);
        }
        return res;
    }

}
