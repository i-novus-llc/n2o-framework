package net.n2oapp.framework.api.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum PrintTypeEnum implements N2oEnum {
    TEXT("text"),
    PDF("pdf"),
    IMAGE("image");

    private final String id;
}