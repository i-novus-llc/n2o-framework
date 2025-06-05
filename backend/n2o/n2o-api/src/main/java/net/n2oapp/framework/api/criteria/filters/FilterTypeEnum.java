package net.n2oapp.framework.api.criteria.filters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип фильтра
 */
@RequiredArgsConstructor
@Getter
public enum FilterTypeEnum implements N2oEnum {
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

    private final String id;
    public final ArityEnum arity;

    public enum ArityEnum {
        UNARY, N_ARY, NULLARY
    }
}