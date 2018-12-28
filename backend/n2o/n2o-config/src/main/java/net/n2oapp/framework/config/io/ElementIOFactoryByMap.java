package net.n2oapp.framework.config.io;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.metadata.io.ElementIOFactory;
import net.n2oapp.framework.api.metadata.io.ProxyTypedElementIO;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика типизированных функций чтения / записи элементов
 */
public class ElementIOFactoryByMap<T,R extends TypedElementReader<? extends T>,P extends TypedElementPersister<? super T>>
        implements ElementIOFactory<T,R,P> {
    private Class<T> baseElementClass;
    private Map<String, R> names = new HashMap<>();
    private Map<Class, P> classes = new HashMap<>();

    public ElementIOFactoryByMap(Class<T> baseElementClass) {
        this.baseElementClass = baseElementClass;
    }

    @Override
    public ElementIOFactory<T,R,P> add(TypedElementIO<? extends T> tio) {
        ProxyTypedElementIO<? extends T> proxy = new ProxyTypedElementIO<>(tio);
        names.put(tio.getElementName(), (R) proxy);
        classes.put(tio.getElementClass(), (P) proxy);
        return this;
    }

    @Override
    public Class<T> getBaseElementClass() {
        return baseElementClass;
    }

    @Override
    public P produce(Object model) {
        P engine = (P) classes.get(model.getClass());

        if (engine == null) {
            throw new EngineNotFoundException(model.getClass());
        }
        return engine;
    }

    @Override
    public R produce(Element element) {
        R engine = names.get(element.getName());
        if (engine == null) {
            throw new EngineNotFoundException(element.getName());
        }
        return engine;
    }

}
