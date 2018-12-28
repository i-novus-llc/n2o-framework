package net.n2oapp.framework.config.metadata.event.factory;

import net.n2oapp.engine.factory.simple.BeanListAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.factory.EventCompiler;
import net.n2oapp.framework.api.metadata.event.factory.EventCompilerFactory;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author operehod
 * @since 23.10.2015
 */
@Component
public class EventCompilerFactoryImpl extends BeanListAware<EventCompiler> implements EventCompilerFactory {

    private volatile Map<ThreeTypeKey<N2oAction, CompileContext, Object>, EventCompiler> register;


    public EventCompiler produce(Class<? extends N2oAction> eventClass, Class<? extends CompileContext> contextClass,
                                 Class<? extends Object> eventContainerClass) {
        if (register == null)
            initFactory();
        ThreeTypeKey<N2oAction, CompileContext, Object> typeKey = new ThreeTypeKey<>(eventClass, contextClass, eventContainerClass);
        if (!register.containsKey(typeKey))
            tryFindMoreGenericCompiler(typeKey);
        if (!register.containsKey(typeKey))
            return null;
        return register.get(typeKey);
    }

    private synchronized void tryFindMoreGenericCompiler(ThreeTypeKey<N2oAction, CompileContext, Object> typeKey) {
        if (!register.containsKey(typeKey)) {
            ThreeTypeKey<N2oAction, CompileContext, Object> nearestKey = findNearestKey(typeKey);
            if (nearestKey != null)
                register.put(typeKey, register.get(nearestKey));
        }
    }

    private ThreeTypeKey<N2oAction, CompileContext, Object> findNearestKey(ThreeTypeKey<N2oAction, CompileContext, Object> typeKey) {
        ThreeTypeKey<N2oAction, CompileContext, Object> keyByContext = findNearestKeyByContext(typeKey);
        if (keyByContext != null) {
            return keyByContext;
        }
        return findBaseKey(typeKey);
    }

    private ThreeTypeKey<N2oAction, CompileContext, Object> findNearestKeyByContext(ThreeTypeKey<N2oAction, CompileContext, Object> typeKey) {
        for (ThreeTypeKey<N2oAction, CompileContext, Object> key : register.keySet()) {
            if (key.t1Class == typeKey.t1Class && (key.t2Class == typeKey.t2Class || typeKey.t2Class == null)
                    && key.t3Class == typeKey.t3Class){
                return key;
            }
        }
        return null;
    }

    private synchronized  ThreeTypeKey<N2oAction, CompileContext, Object> findBaseKey(ThreeTypeKey<N2oAction, CompileContext, Object> typeKey) {
        for (ThreeTypeKey<N2oAction, CompileContext, Object> key : register.keySet()) {
            if (key.t1Class == typeKey.t1Class && (key.t2Class == typeKey.t2Class || key.t2Class == null)
                    && (key.t3Class == typeKey.t3Class || key.t3Class == null)){
                return key;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private synchronized void initFactory() {
        if (register == null) {
            Map<ThreeTypeKey<N2oAction, CompileContext, Object>, EventCompiler> tmpRegister = new HashMap<>();
            for (EventCompiler b : getBeans()) {
                Class<? extends N2oAction> type1Class = b.getEventClass();;
                Class<? extends CompileContext> type2Class = b.getContextClass();;
                Class<? extends Object> type3Class = b.getEventContainerClass();;
                tmpRegister.put(new ThreeTypeKey<>(type1Class, type2Class, type3Class), b);
            }
            register = tmpRegister;
        }
    }

    public static class ThreeTypeKey<T1, T2, T3> {
        private Class<? extends T1> t1Class;
        private Class<? extends T2> t2Class;
        private Class<? extends T3> t3Class;

        public ThreeTypeKey(Class<? extends T1> t1Class, Class<? extends T2> t2Class, Class<? extends T3> t3Class) {
            this.t1Class = t1Class;
            this.t2Class = t2Class;
            this.t3Class = t3Class;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ThreeTypeKey key = (ThreeTypeKey) o;

            if (t1Class.equals(key.t1Class) && t2Class.equals(key.t2Class) && t3Class.equals(key.t3Class)) return true;
            return false;
        }

        @Override
        public int hashCode() {
            int result = t1Class.getName().hashCode();
            result = 31 * result + (t2Class != null ? t2Class.getName().hashCode() : 0);
            result = 31 * result + (t3Class != null ? t3Class.getName().hashCode() : 0);
            return result;
        }
    }

    public Class<EventCompiler> getBeanClass() {
        return EventCompiler.class;
    }
}
