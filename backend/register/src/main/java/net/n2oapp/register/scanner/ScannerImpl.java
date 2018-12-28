package net.n2oapp.register.scanner;

import net.n2oapp.register.scanner.definitions.ClassDefinitionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User: boris.fanyuk
 * Date: 10.08.12
 * Time: 15:32
 */
public abstract class ScannerImpl<S> implements Scanner<S> {
    @Autowired
    private ApplicationContext context;

    @Override
    public Collection<S> scan() {
        Collection<S> result = new LinkedHashSet<S>();
        Collection<ClassDefinitionHolder> beans = context.getBeansOfType(ClassDefinitionHolder.class).values();
        for (ClassDefinitionHolder bean : beans) {
            scan0(result, bean.getClasses());
        }
        return result;
    }

    private void scan0(Collection<S> result, Set<Class> classes) {
        for (Class i : classes)
            if (isClassSuitable(i))
                for (S s : getItemsFromClass(i))
                    if (isItemSuitable(s))
                        result.add(s);
    }


    protected abstract boolean isClassSuitable(Class clazz);

    protected abstract S[] getItemsFromClass(Class clazz);

    protected abstract boolean isItemSuitable(S item);


}
