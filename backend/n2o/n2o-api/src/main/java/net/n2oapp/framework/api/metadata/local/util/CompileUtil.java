package net.n2oapp.framework.api.metadata.local.util;

import net.n2oapp.framework.api.metadata.control.N2oFieldCondition;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.tools.AfterSubmit;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.local.GlobalMetadataProvider;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * User: iryabov
 * Date: 22.07.13
 * Time: 16:14
 */
public class CompileUtil {
    private static final Logger logger = LoggerFactory.getLogger(CompileUtil.class);


    public static boolean isTrue(Boolean bool) {
        return bool != null && bool;
    }

    public static boolean castDefault(Boolean value, boolean defaultValue) {
        return value == null ? defaultValue : value;

    }

    public static boolean castDefault(Boolean value, Boolean defaultValue1, Boolean... defaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (defaultValues == null) return false;
        for (Boolean defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return false;
    }

    public static String collectLinks(Set<String> strings) {
        String res = "";
        boolean begin = true;
        for (String s : strings) {
            if (!begin) res += ",";
            res += s;
            begin = false;
        }
        return res;
    }


    public static void castConditionDefaults(N2oFieldCondition condition, ScriptProcessor sp) {
        if (condition == null || condition.getOn() != null) return;
        String on = ScriptProcessor.simplifyArrayLinks(CompileUtil.collectLinks(ScriptProcessor.extractVars(condition.getCondition())));
        condition.setOn(on);
    }

    /**
     * создается анонимная(без id!) страница, с копированием виджета
     * Желательно добиться того чтобы это был единственный метод в движке которые создает пейдж на базе виджета
     */
    public static N2oPage createSimplePage(N2oWidget widget, GlobalMetadataProvider provider) {
        N2oSimplePage page = new N2oSimplePage();
        page.setProcessable(widget.isProcessable());//важно, т.к. может быть заглушка-виджет
        String objectId = widget.getObjectId() != null ? widget.getObjectId() : provider.getGlobal(widget.getQueryId(), N2oQuery.class).getObjectId();
        page.setObjectId(objectId);
        page.setName(widget.getName());
        page.setWidget(widget);
        return page;
    }

    /**
     * создается анонимная(без id!) страница со ссылкой на реальный виджет
     */
    public static N2oPage createSimplePage(String widgetId, GlobalMetadataProvider provider) {
        N2oWidget widget = provider.getGlobal(widgetId, N2oWidget.class);
        widget.setRefId(widgetId);
        return createSimplePage(widget, provider);
    }


    public static void castShowModalDefaultsWithAfterSubmit(N2oAbstractPageAction showModal, final AfterSubmit afterSubmit) {
        if (showModal.getRefreshOnClose() == null)
            showModal.setRefreshOnClose(false);
        showModal.setMasterFieldId(castDefault(showModal.getMasterFieldId()
                , "id"));
        showModal.setDetailFieldId(castDefault(showModal.getDetailFieldId()
                , "id"));
    }

    public static void castShowModalDefaults(N2oAbstractPageAction showModal) {
        castShowModalDefaultsWithAfterSubmit(showModal, AfterSubmit.closeModal);
    }

    public static String castDefault(String value, String defaultValue1, String... defaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (defaultValues == null) return null;
        for (String defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return null;
    }

    /**
     * Ленивое вычисление дефолтных значений
     * @param value source value
     * @param supplier возращает монаду храняющую значение
     * @param getter распаковывает монаду
     * @param defaultValues предустановленные дефолтные значения
     * @param <T> возращаемый тип
     * @param <R> тип монады
     * @return вычисленное дефолтное значение
     */
    @SafeVarargs
    public static <T, R> T castDefault(T value, Supplier<R> supplier, Function<R, T> getter, T... defaultValues) {
        if (value != null) return value;
        R holder = Optional.ofNullable(supplier).orElse(() -> null).get();
        if (holder != null) {
            T res = getter.apply(holder);
            if (res != null) {
                return res;
            }
        }
        return defaultValues == null ? null : Arrays.stream(defaultValues).filter(Objects::nonNull).findFirst().orElse(null);
    }

    /**
     * Ленивое вычисление дефолтных значений
     * @param value source value
     * @param supplier возращает вычисленное дефолтное значение
     * @param defaultValues предустановленные дефолтные значения
     * @param <T> возращаемый тип
     * @return вычисленное дефолтное значение
     */
    @SafeVarargs
    public static <T> T castDefault(T value, Supplier<T> supplier, T... defaultValues) {
        return castDefault(value, supplier, Function.identity(), defaultValues);
    }

    public static Integer castDefault(Integer value, Integer defaultValue1, Integer... defaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (defaultValues == null) return null;
        for (Integer defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return null;
    }

    public static BigDecimal castDefault(BigDecimal value, BigDecimal defValue1, BigDecimal... defValues) {
        if (value != null) return value;
        if (defValue1 != null) return defValue1;
        if (defValues == null) return null;
        for (BigDecimal defValue : defValues) {
            if (defValue != null) {
                return defValue;
            }
        }
        return null;
    }

    public static <T extends Enum<?>> T castDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;

    }

    @SafeVarargs
    public static <T extends Enum<?>> T castDefault(T value, T defaultValue1, T... defaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (defaultValues == null) return null;
        for (T defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return null;
    }


    @SafeVarargs
    public static <T extends IdAware, S extends IdAware> void arrayToMap(S[] sourceArray, Callback<T, S> callback,
                                                                         Map<String, T>... resultMaps) {
        if (sourceArray != null && sourceArray.length > 0) {
            for (S s : sourceArray) {
                for (Map<String, T> resultMap : resultMaps) {
                    T t = callback.compile(s);
                    if (t != null) resultMap.put(t.getId(), t);
                }
            }
        }
    }

    /**
     * Добавить элементы в новый массив
     * @param arr массив
     * @param elements элементы
     * @param <T> тип данных массива
     * @return новый массив
     */
    @SafeVarargs
    public static <T> T[] append(T[] arr, T... elements) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + elements.length);
        for (int i = 0; i < elements.length; i++) {
            arr[N+i] = elements[0];
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    public static <T> T copy(T cloningObject) {
        return (T) SerializationUtils.deserialize(SerializationUtils.serialize(cloningObject));
    }


    public static <T extends IdAware, S extends IdAware> void listToMap(Collection<S> list, Callback<T, S> callback,
                                                                        Map<String, T>... resultMaps) {
        arrayToMap((S[]) list.toArray(), callback, resultMaps);
    }


    public interface Callback<T extends IdAware, S extends IdAware> {
        T compile(S s);
    }


    public static N2oFieldCondition createConditionOnBase(N2oFieldCondition base, String expression) {
        if (base == null)
            return createCondition(expression);
        N2oFieldCondition condition = new N2oFieldCondition();
        String on = CompileUtil.collectLinks(ScriptProcessor.extractVars(expression));
        condition.setCondition(ScriptProcessor.buildAddConjunctionCondition(expression, base.getCondition()));
        if (base.getOn() == null) {
            condition.setOn(on);
        } else {
            condition.setOn(base.getOn() + "," + on);
        }
        return condition;
    }


    public static N2oFieldCondition createCondition(String expression) {
        return new N2oFieldCondition(expression, CompileUtil.collectLinks(ScriptProcessor.extractVars(expression)));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] safeArray(T[] array, Class<T> tClass) {
        return array != null ? array : (T[]) Array.newInstance(tClass, 0);
    }
}
