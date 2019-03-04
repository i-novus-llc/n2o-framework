package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.object.ObjectElementIOv2;
import net.n2oapp.framework.config.io.object.ObjectElementIOv3;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.compile.validation.ConditionValidationCompiler;
import net.n2oapp.framework.config.metadata.compile.validation.ConstraintValidationCompiler;
import net.n2oapp.framework.config.metadata.compile.validation.MandatoryValidationCompiler;

/**
 * Набор для сборки стандартных объектов
 */
public class N2oObjectsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.ios(new ObjectElementIOv2(),
                new ObjectElementIOv3());
        b.compilers(new N2oObjectCompiler(),
                new ConstraintValidationCompiler(),
                new MandatoryValidationCompiler(),
                new ConditionValidationCompiler());
        b.packs(new N2oInvocationV2ReadersPack());
    }
}
