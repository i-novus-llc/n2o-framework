package net.n2oapp.criteria.api.constructor;

/**
 * User: boris.fanyuk
 * Date: 19.10.12
 * Time: 14:27
 */
public abstract class CriteriaConstructorImpl<T, Init> implements CriteriaConstructor<T, Init> {
    @Override
    public CriteriaConstructorResult construct(T source, Init init) {
        if (isForSkip(source, init))
            return null;
        return new CriteriaConstructorResult(getQueryStringFormat()).addParameter(convertParameterValue(source, init));
    }

    public abstract String getQueryStringFormat();

    public abstract T convertParameterValue(T source, Init init);

    public abstract boolean isForSkip(T source, Init init);
}
