package net.n2oapp.framework.api.metadata.global.util;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Соотношение объектов json с типами N2O
 */
public class ComponentTypeResolver implements TypeIdResolver {
    public static final List<String> searchPackages = Arrays.asList(
            "net.n2oapp.framework.api.metadata",
            "net.n2oapp.framework.api.metadata.application",
            "net.n2oapp.framework.api.metadata.control",
            "net.n2oapp.framework.api.metadata.control.interval",
            "net.n2oapp.framework.api.metadata.control.list",
            "net.n2oapp.framework.api.metadata.control.multi",
            "net.n2oapp.framework.api.metadata.control.plain",
            "net.n2oapp.framework.api.metadata.dataprovider",
            "net.n2oapp.framework.api.metadata.datasource",
            "net.n2oapp.framework.api.metadata.event.action",
            "net.n2oapp.framework.api.metadata.global.dao",
            "net.n2oapp.framework.api.metadata.global.dao.object",
            "net.n2oapp.framework.api.metadata.global.dao.validation",
            "net.n2oapp.framework.api.metadata.global.view",
            "net.n2oapp.framework.api.metadata.global.view.action.control",
            "net.n2oapp.framework.api.metadata.global.view.fieldset",
            "net.n2oapp.framework.api.metadata.global.view.page",
            "net.n2oapp.framework.api.metadata.global.view.region",
            "net.n2oapp.framework.api.metadata.global.view.tools",
            "net.n2oapp.framework.api.metadata.global.view.widget",
            "net.n2oapp.framework.api.metadata.global.view.widget.chart",
            "net.n2oapp.framework.api.metadata.global.view.widget.dependency",
            "net.n2oapp.framework.api.metadata.global.view.widget.list",
            "net.n2oapp.framework.api.metadata.global.view.widget.table",
            "net.n2oapp.framework.api.metadata.global.view.widget.table.column",
            "net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell",
            "net.n2oapp.framework.api.metadata.global.view.widget.toolbar",
            "net.n2oapp.framework.api.metadata.global.view.widget.tree",
            "net.n2oapp.framework.api.metadata.header",
            "net.n2oapp.framework.api.metadata.menu");
    private JavaType mBaseType;

    @Override
    public void init(JavaType javaType) {
        mBaseType = javaType;
    }

    @Override
    public String idFromValue(Object o) {
        return idFromValueAndType(o, o.getClass());
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        String name = aClass.getName();
        if (searchPackages.contains(name.substring(0, name.lastIndexOf(".")))) {
            return name.substring(name.lastIndexOf(".") + 1);
        }
        throw new IllegalStateException("class " + aClass + " is not in the packages");
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String s) throws IOException {
        Class<?> clazz = null;
        for (String pack : searchPackages) {
            try {
                clazz = Class.forName(pack + "." + s);
            } catch (ClassNotFoundException e) {
            }
        }
        if (clazz == null)
            throw new IllegalStateException("Class not found " + s);
        return TypeFactory.defaultInstance().constructSpecializedType(mBaseType, clazz);
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
