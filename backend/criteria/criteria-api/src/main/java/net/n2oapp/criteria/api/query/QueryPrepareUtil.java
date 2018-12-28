package net.n2oapp.criteria.api.query;

/**
 * User: iryabov
 * Date: 10.02.13
 * Time: 12:33
 */
public class QueryPrepareUtil {
    public static String prepareSelectClause(String baseQuery, String selectClause) {
        if ((selectClause == null) || (selectClause.isEmpty())) {
            return baseQuery.replace(":select", "1");
        } else {
            return baseQuery.replace(":select", selectClause);
        }
    }

    public static String prepareWhereClause(String baseQuery, String whereClause) {
        if (whereClause == null || whereClause.length() == 0) {
            return baseQuery.replace(":where", "1=1");
        } else {
            return baseQuery.replace(":where", whereClause);
        }
    }

    public static String prepareOrderByClause(String baseQuery, String orderByClause) {
        if (orderByClause == null || orderByClause.length() == 0) {
            return baseQuery.replace(":order", "1");
        } else {
            return baseQuery.replace(":order", orderByClause);
        }
    }

    public static String prepareJoinClause(String baseQuery, String joinClause) {
        if (joinClause == null) {
            joinClause = "";
        }
        return baseQuery.replace(":join", joinClause);
    }
}
