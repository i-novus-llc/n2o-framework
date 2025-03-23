package net.n2oapp.framework.migrate;

import net.n2oapp.framework.api.metadata.io.ElementIOFactory;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.io.ElementIOFactoryByMap;

public class MigratorElementIOFactoryByMap<T, R extends TypedElementReader<? extends T>, P extends TypedElementPersister<? super T>> extends ElementIOFactoryByMap<T, R, P> {

    public MigratorElementIOFactoryByMap(Class<T> baseElementClass) {
        super(baseElementClass);
    }

    @Override
    public ElementIOFactory<T, R, P> add(TypedElementIO<? extends T> tio) {
        MigratorProxyTypedElementIO<? extends T> proxy = new MigratorProxyTypedElementIO<>(tio);
        getNames().computeIfAbsent(tio.getElementName(), k -> (R) proxy);
        getClasses().computeIfAbsent(tio.getElementClass(), k -> (P) proxy);
        return this;
    }
}