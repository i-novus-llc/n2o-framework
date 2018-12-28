package net.n2oapp.framework.api.metadata.compile.collector;

import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.List;

public interface ActionsCollector {
    List<Action> collectActions();
}
