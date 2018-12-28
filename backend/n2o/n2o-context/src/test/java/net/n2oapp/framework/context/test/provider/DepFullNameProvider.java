package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.context.smart.impl.api.OneValueContextProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author iryabov
 * @since 15.11.2016
 */
public class DepFullNameProvider implements OneValueContextProvider {

    @Override
    public Object getValue(Context context) {
        return context.get("org.name").toString() + context.get("dep.name");
    }

    @Override
    public String getParam() {
        return "dep.fullName";
    }

    @Override
    public Set<String> getDependsOnParams() {
        return new HashSet<>(Arrays.asList("org.name", "dep.name"));
    }
}
