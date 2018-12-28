package net.n2oapp.framework.api.util;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.N2oReference;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * Утильный класс для работы с сурсами n2o
 */
@SuppressWarnings("ALL")
public class SourceUtil {

    /**
     * Получить стрим всех филдов в форме
     * @param form source form
     * @return all fields
     */
    //todo:убрать
//    public static Stream<N2oField> getAllFieldsInForm(N2oForm form) {
//        if (form == null || form.getFieldSetContainers() == null) {
//            return Stream.empty();
//        }
//        return stream(form.getFieldSetContainers())
//                .map(SourceUtil::resolve)
//                .filter(Objects::nonNull)
//                .filter(fs -> fs.getBlocks() != null)
//                .flatMap(fs -> stream(fs.getBlocks()))
//                .filter(b -> b.getFields() != null)
//                .flatMap(b -> stream(b.getFields()));
//    }

    public static <T extends N2oMetadata> T resolve(T t) {
        if (t instanceof N2oReference && ((N2oReference) t).getRefId() != null)
            return (T) getOrNull(((N2oReference) t).getRefId(), t.getClass());
        return t;
    }

    public static <T extends N2oMetadata> T getOrNull(String id, Class<T> tClass) {
        try {
            return CompilerHolder.get().getGlobal(id, tClass);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static <T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }

}
