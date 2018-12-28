package net.n2oapp.register.scanner;

import java.util.Collection;

/**
 * User: IRyabov
 * Date: 25.01.12
 * Time: 11:48
 */
public interface Scanner<S> {

    Collection<S> scan();

}
