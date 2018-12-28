package net.n2oapp.register.register;

import net.n2oapp.register.scanner.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * User: IRyabov
 * Date: 25.01.12
 * Time: 11:14
 */
public class BaseRegister<S, V> extends BiDirectionalMapRegister<S, V> implements InitializingBean {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected Scanner<S> scanner;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(extractor);
        if (scanner != null) {
            addAll(extractor.extractMap(scanner.scan()));
        }
    }

    public void setScanner(Scanner<S> scanner) {
        this.scanner = scanner;
    }
}
