package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Фабрика конструкторов критериев
 */
@Component
public class CriteriaConstructorFactory {

    private final Map<Class<?>, CriteriaConstructor<?>> constructors;

    @Autowired
    public CriteriaConstructorFactory(List<CriteriaConstructor<?>> constructorList) {
        this.constructors = constructorList.stream()
                .collect(Collectors.toMap(CriteriaConstructor::getCriteriaClass, c -> c));
    }

    @SuppressWarnings("unchecked")
    public <T> T construct(N2oPreparedCriteria criteria, T instance) {
        Class<?> criteriaClass = instance.getClass();
        CriteriaConstructor<T> constructor = null;
        while (criteriaClass != null) {
            constructor = (CriteriaConstructor<T>) constructors.get(criteriaClass);
            if (constructor != null)
                break;

            criteriaClass = criteriaClass.getSuperclass();
        }

        if (constructor == null) return instance;
        return constructor.construct(criteria, instance);
    }
}