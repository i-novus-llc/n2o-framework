package net.n2oapp.criteria.filters;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Тип фильтра
 */
@Getter
public enum FilterTypeEnum {
    @Deprecated SIMPLE("simple", ArityEnum.UNARY),
    EQ("eq", ArityEnum.UNARY),
    NOT_EQ("notEq", ArityEnum.UNARY),
    MORE("more", ArityEnum.UNARY),
    LESS("less", ArityEnum.UNARY),
    EQ_OR_IS_NULL("eqOrIsNull", ArityEnum.UNARY),
    IS_NOT_NULL("isNotNull", ArityEnum.NULLARY),
    IS_NULL("isNull", ArityEnum.NULLARY),
    IN("in", ArityEnum.N_ARY),
    NOT_IN("notIn", ArityEnum.N_ARY),
    IN_OR_IS_NULL("inOrIsNull", ArityEnum.N_ARY),
    INFINITE("infinite", ArityEnum.NULLARY),
    EMPTY("empty", ArityEnum.NULLARY),
    LIKE("like", ArityEnum.UNARY),
    LIKE_START("likeStart", ArityEnum.UNARY),
    OVERLAPS("overlaps", ArityEnum.N_ARY),
    CONTAINS("contains", ArityEnum.N_ARY);

    @JsonValue
    private final String id;
    public final ArityEnum arity;

    FilterTypeEnum(String id, ArityEnum arity) {
        this.id = id;
        this.arity = arity;
    }

    public enum ArityEnum {
        UNARY, N_ARY, NULLARY
    }
}