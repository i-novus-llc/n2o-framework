package net.n2oapp.criteria.filters;

/**
 * Тип фильтра
 */
public enum FilterType {
    @Deprecated simple, eq, notEq, more, less, eqOrIsNull,
    isNotNull(Arity.nullary), isNull(Arity.nullary),
    in(Arity.n_ary), notIn(Arity.n_ary), inOrIsNull(Arity.n_ary),
    infinite(Arity.nullary),empty(Arity.nullary), like, likeStart,
    overlaps(Arity.n_ary), contains(Arity.n_ary);

    public Arity arity;

    FilterType(Arity arity) {
        this.arity = arity;
    }

    FilterType() {
        this.arity = Arity.unary;
    }

    public enum Arity {
        unary, n_ary, nullary
    }


}
