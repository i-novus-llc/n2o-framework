package net.n2oapp.criteria.filters;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterReducer {

    private static Map<Pair, Rule> rulesMap;
    private static List<? extends Rule> ruleList = Arrays.asList(
            new EqAndEq(), new EqAndIn(), new EqAndLess(), new EqAndMore(), new EqAndNotEq(), new EqAndIsNull(), new EqAndIsNotNull(), new EqAndNotIn(),
            new NotEqAndNotEq(), new NotEqAndNotIn(),
            new InAndIn(), new InAndLess(), new InAndMore(), new InAndIsNull(), new InAndIsNotNull(), new InAndNotEq(), new InAndNotIn(),
            new IsNullAndIsNotNull(), new IsNullAndNotEq(), new IsNullAndNotIn(),
            new LessAndLess(), new MoreAndMore(), new LessAndIsNull(), new MoreAndIsNull(), new MoreAndIsNotNull(), new LessAndIsNotNull(),
            new NotInAndNotIn(),
            //eqOrIsNull
            new EqOrIsNullAndEq(), new EqOrIsNullAndEqOrIsNull(), new EqOrIsNullAndIn(), new EqOrIsNullAndIsNotNull(),
            new EqOrIsNullAndIsNull(), new EqOrIsNullAndLess(), new EqOrIsNullAndMore(), new EqOrIsNullAndNotEq(), new EqOrIsNullAndNotIn(),
            new EqOrIsNullAndLike(), new EqOrIsNullAndLikeStart(), new EqOrIsNullAndContains(),
            //like, likeStart
            new EqAndLike(), new EqAndLikeStart(), new LikeAndIsNotNull(), new LikeAndIsNull(), new LikeAndLike(), new LikeAndLikeStart(),
            new LikeStartAndIsNotNull(), new LikeStartAndIsNull(), new LikeStartAndLikeStart(),
            //contains
            new EqAndContains(), new ContainsAndNotEq(), new ContainsAndContains(),
            new ContainsAndIsNull(), new ContainsAndIsNotNull()
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
     *
     * @param left  first filter
     * @param right second filter
     * @return filters union
     */
    @SuppressWarnings("unchecked")
    public static Result reduce(Filter left, Filter right) {
        Result res = new Result();
        res.setLeftFilter(left);
        res.setRightFilter(right);
        Rule rule = rulesMap.get(new Pair<>(left.getType(), right.getType()));
        if (rule == null)
            res.setType(Result.TypeEnum.notMergeable);
        else {
            Filter filter = rule.simplify(left, right);
            res.setResultFilter(filter);
            if (filter == null)
                res.setType(Result.TypeEnum.conflict);
            else
                res.setType(Result.TypeEnum.success);
        }
        return res;
    }

}
