package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oPassword;
import net.n2oapp.framework.api.metadata.meta.control.Password;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция поля для ввода текста
 */
@Component
public class PasswordCompiler extends StandardFieldCompiler<Password, N2oPassword> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPassword.class;
    }

    @Override
    public StandardField<Password> compile(N2oPassword source, CompileContext<?,?> context, CompileProcessor p) {
        Password password = new Password();
        password.setControlSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.input.password.src"), String.class)));
        password.setPlaceholder(p.resolveJS(source.getPlaceholder()));
        password.setLength(source.getLength());
        return compileStandardField(password, source, context, p);
    }

}

