package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.BehaviorEnum;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.DndColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.MultiColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;

/**
 * Трансформатор доступа строки таблицы
 */
@Component
public class TableAccessTransformer extends BaseAccessTransformer<Table, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Table.class;
    }

    @Override
    public Table transform(Table compiled, CompileContext context, CompileProcessor p) {
        if (compiled.getComponent() != null && compiled.getComponent().getBody().getRow() != null &&
                compiled.getComponent().getBody().getRow().getClick() != null) {
            transfer(compiled.getComponent().getBody().getRow().getClick().getAction(),
                    compiled.getComponent().getBody().getRow());
        }
        Map<String, Cell> cellsById = indexCellsById(compiled);
        transferColumnSecurityToCells(compiled, cellsById);
        transferToolbarButtonsSecurityToColumn(compiled, cellsById);
        return compiled;
    }

    private Map<String, Cell> indexCellsById(Table compiled) {
        if (compiled.getComponent() == null) return Collections.emptyMap();
        List<Cell> cells = compiled.getComponent().getBody().getCells();
        if (cells == null) return Collections.emptyMap();
        Map<String, Cell> map = new HashMap<>();
        for (Cell cell : cells) {
            if (cell.getId() != null) map.put(cell.getId(), cell);
        }
        return map;
    }

    private void transferColumnSecurityToCells(Table compiled, Map<String, Cell> cellsById) {
        if (compiled.getComponent() == null) return;
        List<AbstractColumn> columns = compiled.getComponent().getHeader().getCells();
        if (columns == null) return;
        for (AbstractColumn column : flattenLeafColumns(columns)) {
            Cell cell = cellsById.get(column.getId());
            if (column instanceof PropertiesAware columnProps && cell instanceof PropertiesAware cellProps) {
                transfer(columnProps, cellProps);
            }
        }
    }

    private void transferToolbarButtonsSecurityToColumn(Table compiled, Map<String, Cell> cellsById) {
        if (compiled.getComponent() == null) return;
        List<AbstractColumn> columns = compiled.getComponent().getHeader().getCells();
        if (columns == null) return;
        for (AbstractColumn column : flattenLeafColumns(columns)) {
            Cell cell = cellsById.get(column.getId());
            if (column instanceof PropertiesAware columnProps && cell instanceof ToolbarCell toolbarCell) {
                applyButtonsSecurityToColumn(columnProps, toolbarCell);
            }
        }
    }

    private List<AbstractColumn> flattenLeafColumns(List<AbstractColumn> columns) {
        List<AbstractColumn> result = new ArrayList<>();
        for (AbstractColumn column : columns) {
            switch (column) {
                case MultiColumn multi when multi.getChildren() != null && !multi.getChildren().isEmpty() ->
                        result.addAll(flattenLeafColumns(new ArrayList<>(multi.getChildren())));
                case DndColumn dnd when dnd.getChildren() != null && !dnd.getChildren().isEmpty() ->
                        result.addAll(flattenLeafColumns(new ArrayList<>(dnd.getChildren())));
                default -> result.add(column);
            }
        }
        return result;
    }

    private void applyButtonsSecurityToColumn(PropertiesAware column, ToolbarCell toolbarCell) {
        if (toolbarCell.getToolbar() == null || toolbarCell.getToolbar().isEmpty()) return;
        if (hasSecurity(column)) return;

        List<AbstractButton> buttons = collectAccessRelevantButtons(toolbarCell.getToolbar());
        if (buttons.isEmpty()) return;
        for (AbstractButton b : buttons) {
            if (!hasSecurity(b)) return;
        }

        Map<String, SecurityObject> mergedByKey = mergeButtonsSecurityByKey(buttons);
        if (mergedByKey.isEmpty()) return;

        if (column.getProperties() == null) column.setProperties(new HashMap<>());
        Security security = new Security();
        security.add(mergedByKey);
        column.getProperties().put(SECURITY_PROP_NAME, security);
    }

    /**
     * Возвращает список кнопок, релевантных для решения о видимости колонки:
     * <ul>
     *   <li>обычная кнопка — как есть;</li>
     *   <li>submenu со своим security — как одна кнопка (его собственное security ограничивает всё содержимое);</li>
     *   <li>submenu без своего security — раскрывается до своих menu-item'ов.</li>
     * </ul>
     */
    private List<AbstractButton> collectAccessRelevantButtons(List<Group> toolbar) {
        List<AbstractButton> result = new ArrayList<>();
        for (Group group : toolbar) {
            if (group.getButtons() == null) continue;
            for (AbstractButton b : group.getButtons()) {
                if (b instanceof Submenu submenu && submenu.getContent() != null && !hasSecurity(submenu)) {
                    result.addAll(submenu.getContent());
                } else {
                    result.add(b);
                }
            }
        }
        return result;
    }

    private static boolean hasSecurity(PropertiesAware element) {
        return element.getProperties() != null
                && element.getProperties().get(SECURITY_PROP_NAME) != null;
    }

    /**
     * Объединяет security кнопок в результирующий map с сохранением исходных ключей
     * (object/page/url/custom). Для каждого ключа отдельно собирается union ролей/permissions/usernames
     * и флагов permitAll/authenticated/anonymous, а denied=true выставляется только если все кнопки,
     * у которых был данный ключ, имели denied=true под этим же ключом.
     */
    private Map<String, SecurityObject> mergeButtonsSecurityByKey(List<AbstractButton> buttons) {
        Map<String, MergeAccumulator> perKey = new HashMap<>();
        Map<String, Integer> deniedButtonsPerKey = new HashMap<>();
        boolean anyDisable = false;

        for (AbstractButton button : buttons) {
            if (button.getProperties() == null) continue;
            Security buttonSecurity = (Security) button.getProperties().get(SECURITY_PROP_NAME);
            if (buttonSecurity == null) continue;
            Set<String> deniedKeysOfButton = new HashSet<>();
            for (Map<String, SecurityObject> map : buttonSecurity) {
                for (Map.Entry<String, SecurityObject> entry : map.entrySet()) {
                    String key = entry.getKey();
                    SecurityObject so = entry.getValue();
                    perKey.computeIfAbsent(key, k -> new MergeAccumulator()).mergeSecurityObject(so);
                    if (so.getBehavior() == BehaviorEnum.DISABLE) anyDisable = true;
                    if (Boolean.TRUE.equals(so.getDenied())) deniedKeysOfButton.add(key);
                }
            }
            for (String key : deniedKeysOfButton) {
                deniedButtonsPerKey.merge(key, 1, Integer::sum);
            }
        }

        BehaviorEnum behavior = anyDisable ? BehaviorEnum.DISABLE : BehaviorEnum.HIDE;
        int totalButtons = buttons.size();
        Map<String, SecurityObject> result = new HashMap<>();
        for (Map.Entry<String, MergeAccumulator> entry : perKey.entrySet()) {
            String key = entry.getKey();
            boolean deniedForKey = deniedButtonsPerKey.getOrDefault(key, 0) == totalButtons;
            result.put(key, entry.getValue().build(deniedForKey, behavior));
        }
        return result;
    }

    private static final class MergeAccumulator {
        private final Set<String> roles = new HashSet<>();
        private final Set<String> permissions = new HashSet<>();
        private final Set<String> usernames = new HashSet<>();
        private boolean anyPermitAll;
        private boolean anyAuthenticated;
        private boolean anyAnonymous;

        void mergeSecurityObject(SecurityObject so) {
            if (so.getRoles() != null) roles.addAll(so.getRoles());
            if (so.getPermissions() != null) permissions.addAll(so.getPermissions());
            if (so.getUsernames() != null) usernames.addAll(so.getUsernames());
            if (Boolean.TRUE.equals(so.getPermitAll())) anyPermitAll = true;
            if (Boolean.TRUE.equals(so.getAuthenticated())) anyAuthenticated = true;
            if (Boolean.TRUE.equals(so.getAnonymous())) anyAnonymous = true;
        }

        SecurityObject build(boolean denied, BehaviorEnum behavior) {
            SecurityObject merged = new SecurityObject();
            if (!roles.isEmpty()) merged.setRoles(roles);
            if (!permissions.isEmpty()) merged.setPermissions(permissions);
            if (!usernames.isEmpty()) merged.setUsernames(usernames);
            if (anyPermitAll) merged.setPermitAll(true);
            if (anyAuthenticated) merged.setAuthenticated(true);
            if (anyAnonymous) merged.setAnonymous(true);
            if (denied) merged.setDenied(true);
            merged.setBehavior(behavior);
            return merged;
        }
    }
}
