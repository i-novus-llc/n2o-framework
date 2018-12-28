package net.n2oapp.framework.api.metadata.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Статисческий хранитель компилятора метаданных
 */
@Deprecated //use CompileProcessor
public class CompilerHolder {

    private static N2oCompiler compiler;

    public CompilerHolder(N2oCompiler compiler) {
        CompilerHolder.compiler = compiler;
    }

    public static N2oCompiler get() {
        return compiler;
    }
}
