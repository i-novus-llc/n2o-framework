package net.n2oapp.framework.access.metadata.pack;


import net.n2oapp.framework.access.metadata.accesspoint.io.ObjectAccessPointIOv2;
import net.n2oapp.framework.access.metadata.accesspoint.io.ObjectFiltersAccessPointIOv2;
import net.n2oapp.framework.access.metadata.accesspoint.io.PageAccessPointIOv2;
import net.n2oapp.framework.access.metadata.accesspoint.io.UrlAccessPointIOv2;
import net.n2oapp.framework.access.metadata.compile.SecurityExtensionAttributeMapper;
import net.n2oapp.framework.access.metadata.compile.SimpleAccessSchemaCompiler;
import net.n2oapp.framework.access.metadata.schema.N2oAccessSchema;
import net.n2oapp.framework.access.metadata.schema.io.SimpleAccessIOv2;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaValidator;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.config.N2oApplicationBuilder;

public class AccessSchemaPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.types(new MetaType("access", N2oAccessSchema.class))
                .ios(new SimpleAccessIOv2())
                .ios(new PageAccessPointIOv2())
                .ios(new ObjectAccessPointIOv2())
                .ios(new UrlAccessPointIOv2())
                .ios(new ObjectFiltersAccessPointIOv2())
                .compilers(new SimpleAccessSchemaCompiler())
                .packs(new AccessPointsV1Pack())
                .packs(new AccessPointsIOV2Pack())
                .packs(new AccessTransformersPack())
                .extensions(new SecurityExtensionAttributeMapper())
                .validators(new SimpleAccessSchemaValidator());
    }
}