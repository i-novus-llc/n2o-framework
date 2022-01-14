package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;

import java.util.Map;

/**
 * Используется для передачи models в компиляцию fieldset и field
 * а также модели и идентификатора виджета, который сейчас компилируется
 */
public class ModelsScope {

    private ReduxModel model;
    private String globalDatasource;
    private Models models;

    public ModelsScope(ReduxModel model, String globalDatasource, Models models) {
        this.model = model;
        this.globalDatasource = globalDatasource;
        this.models = models;
    }

    public void add(String field, ModelLink link) {
        models.add(model, globalDatasource, field, link);
    }

    public void addAll(Map<String, ModelLink> modelLinks) {
        modelLinks.forEach((k, v) -> models.add(model, globalDatasource, k, v));
    }

    public boolean hasModels() {
        return models != null;
    }
}
