package net.n2oapp.framework.api.metadata.pipeline;

public enum PipelineOperationTypeEnum {
    READ,
    MERGE,
    VALIDATE,
    SOURCE_TRANSFORM,
    SOURCE_CACHE,
    COPY,
    COMPILE,
    COMPILE_TRANSFORM,
    COMPILE_CACHE,
    BIND,
    PERSIST,
    DESERIALIZE,
    SERIALIZE
}
