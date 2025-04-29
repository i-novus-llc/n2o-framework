package net.n2oapp.criteria.filters;

/**
 * Тип фильтра
 */
public enum FilterTypeEnum {
    @Deprecated simple, eq, notEq, more, less, eqOrIsNull,
    isNotNull(ArityEnum.nullary), isNull(ArityEnum.nullary),
    in(ArityEnum.n_ary), notIn(ArityEnum.n_ary), inOrIsNull(ArityEnum.n_ary),
    infinite(ArityEnum.nullary),empty(ArityEnum.nullary), like, likeStart,
    overlaps(ArityEnum.n_ary), contains(ArityEnum.n_ary);

    public ArityEnum arity;

    FilterTypeEnum(ArityEnum arity) {
        this.arity = arity;
    }

    FilterTypeEnum() {
        this.arity = ArityEnum.unary;
    }

    public enum ArityEnum {
        unary, n_ary, nullary
    }


}
