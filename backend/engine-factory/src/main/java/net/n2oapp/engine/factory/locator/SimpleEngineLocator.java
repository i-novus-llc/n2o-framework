package net.n2oapp.engine.factory.locator;

import net.n2oapp.engine.factory.MultiEngineFactory;
import net.n2oapp.engine.factory.integration.spring.OverrideBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class SimpleEngineLocator<G> implements EngineLocator<G> {
    private Collection<G> engines;

    public SimpleEngineLocator(Map<String, G> beans) {
        this.engines = new ArrayList<>(OverrideBean.removeOverriddenBeans(beans).values());
    }

    @Override
    public <T> MultiEngineFactory<T, G> locate(BiPredicate<G, T> predicate) {
        return type -> engines.stream().filter(g -> predicate.test(g, type)).collect(Collectors.toList());
    }

    public void add(G engine) {
//        if (engines.stream().noneMatch(e -> e.getClass().equals(engine.getClass())))
            engines.add(engine);
    }
}
