package net.n2oapp.framework.api.metadata.meta.action.editlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum ListOperationTypeEnum implements N2oEnum {
    CREATE("create"),
    CREATE_MANY("createMany"),
    UPDATE("update"),
    DELETE("delete"),
    DELETE_MANY("deleteMany");

    private final String id;
}
