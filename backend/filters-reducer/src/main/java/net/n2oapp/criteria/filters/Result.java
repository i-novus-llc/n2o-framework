package net.n2oapp.criteria.filters;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:11
 */
public class Result {


    public static enum Type {
        success, conflict, failure;


    }

    public boolean isSuccess() {
        return getType().equals(Type.success);
    }

    public boolean isError() {
        return getType().equals(Type.conflict);
    }

    public boolean isFail() {
        return getType().equals(Type.failure);
    }

    private Filter leftFilter;
    private Filter rightFilter;
    private Filter resultFilter;
    private Type type;

    public Filter getLeftFilter() {
        return leftFilter;
    }

    public void setLeftFilter(Filter leftFilter) {
        this.leftFilter = leftFilter;
    }

    public Filter getRightFilter() {
        return rightFilter;
    }

    public void setRightFilter(Filter rightFilter) {
        this.rightFilter = rightFilter;
    }

    public Filter getResultFilter() {
        return resultFilter;
    }

    public void setResultFilter(Filter resultFilter) {
        this.resultFilter = resultFilter;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isReducedToLeft() {
        return leftFilter.equals(resultFilter);
    }

    public boolean isReducedToRight() {
        return rightFilter.equals(resultFilter);
    }

}
