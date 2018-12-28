package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.N2oReference;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.action.control.N2oShowModalForm;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.api.metadata.local.N2oMetadataMerger;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * Утилита для валидации
 */
public class ValidationUtil {

    public ValidationUtil() {
    }

    public static <T extends N2oMetadata> boolean isExists(String id, Class<T> metadataClass) {
        return CompilerHolder.get().isExists(id, metadataClass);
    }

    @SuppressWarnings("unchecked")
    public static <T extends SourceMetadata> T resolve(T t) {
        if (t instanceof N2oReference && ((N2oReference) t).getRefId() != null) {
            N2oReference reference = (N2oReference) t;
            SourceMetadata metadata = getOrNull(reference.getRefId(), t.getClass());
            N2oMetadataMerger merger = reference.getMerger();
            if (merger != null) {
                metadata = merger.merge(metadata, t);
            }
            return (T) metadata;
        }
        return t;
    }

    @Deprecated //use getOrNull
    public static <T extends SourceMetadata> T get(String id, Class<T> tClass) {
        return getOrNull(id, tClass);
    }

    public static <T extends SourceMetadata> T getOrNull(String id, Class<T> tClass) {
        try {
            return CompilerHolder.get().getGlobal(id, tClass);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static <T extends SourceMetadata> T getOrNullIfFound(String id, Class<T> tClass) {
        try {
            return CompilerHolder.get().getGlobal(id, tClass);
        } catch (ReferentialIntegrityViolationException notFound) {
            throw notFound;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static <T extends SourceMetadata> T getOrThrow(String id, Class<T> tClass) {
        return CompilerHolder.get().getGlobal(id, tClass);
    }

    public static List<N2oWidget> retrieveContainers(N2oPage n2oPage) {
        List<N2oWidget> res = new ArrayList<>();
        if (n2oPage.getContainers() != null) {
            res.addAll(n2oPage.getContainers());
        }
        return res;
    }

    public static void checkShowModal(N2oShowModal page) {
        String pageId = page.getPageId();
        if ((pageId != null)
                && (!checkLinkForDymanic(pageId))
                && (!CompilerHolder.get().isExists(pageId, N2oPage.class)))
            throw new N2oMetadataValidationException("n2o.pageNotExists").addData(pageId);
    }

    public static void checkShowModalForm(N2oShowModalForm form) {
        String formId = form.getFormId();
        if (formId != null && !checkLinkForDymanic(formId) && !CompilerHolder.get().isExists(formId, N2oWidget.class))
            throw new N2oMetadataValidationException("n2o.widgetNotExists").addData(formId);
    }

    private static boolean checkLinkForDymanic(String str) {
        return str.contains("{") && str.contains("}");
    }


    public static N2oQuery getQueryOrNull(N2oWidget widget) {
        if (widget.getQueryId() == null)
            return null;
        return getOrThrow(widget.getQueryId(), N2oQuery.class);
    }

    @Deprecated //use getQueryOrNull
    public static N2oQuery findQuery(N2oWidget widget) {
        return getQueryOrNull(widget);
    }

    public static boolean hasLinks(String id) {
        return id.contains("{") && id.contains("}");
    }

    public static boolean notFound(String id, List<? extends IdAware> list) {
        for (IdAware element : list) {
            if (element.getId().equals(id))
                return false;
        }
        return true;
    }

    public <T extends SourceMetadata> void validateId(T metadata, String id) throws N2oMetadataValidationException {
        Pattern pattern = Pattern.compile(".*[а-яА-ЯёЁ].*");
        Matcher matcher = pattern.matcher(id);
        if (matcher.find())
            throw new N2oMetadataValidationException(metadata, "n2o.cyrillicNotSupported");
        if (id.contains("."))
            throw new N2oMetadataValidationException(metadata, "n2o.dotSymbolNotSupported");
    }


    public static <T> Stream<T> safeStreamOf(T[] values) {
        if (values == null)
            return Stream.empty();
        return Stream.of(values);
    }

    public static <T> Stream<T> safeStreamOf(Collection<T> values) {
        if (values == null)
            return Stream.empty();
        return values.stream();
    }

    public static <T extends IdAware> void checkIdsUnique(T[] list, String errorMessage) {
        if (list == null) return;
        checkIdsUnique(Arrays.asList(list), errorMessage);
    }

    public static <T extends IdAware> void checkIdsUnique(List<T> list, String errorMessage) {
        if (list == null)
            return;
        Set<String> uniqueSet = new HashSet<>();
        for (T idAware : list) {
            if (idAware.getId() == null)
                continue;
            if (!uniqueSet.add(idAware.getId())) {
                throw new N2oMetadataValidationException(String.format(errorMessage, idAware.getId()));
            }
        }
    }

    public static <T extends SourceMetadata> void checkForExists(String id, Class<T> n2oMetadataClass, String errorMessage) {
        if (!CompilerHolder.get().isExists(id, n2oMetadataClass))
            throw new N2oMetadataValidationException(errorMessage);
    }
}
