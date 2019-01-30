package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Трансформатор доступа тулбара
 */
@Component
public class ToolbarAccessTransformer extends BaseAccessTransformer<Toolbar, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Toolbar.class;
    }

    @Override
    public Toolbar transform(Toolbar compiled, CompileContext context, CompileProcessor p) {
        for (List<Group> groupList : compiled.values()) {
            for (Group group : groupList) {
                for (Button b : group.getButtons()) {
                    if (b.getActionId() != null) {
                        Action action = p.getScope(MetaActions.class).get(b.getActionId());
                        transfer(action, b);
                    } else if (b.getSubMenu() != null) {
                        Map<String, List<Security.SecurityObject>> securityObjects = new HashMap<>();
                        for (MenuItem menuItem : b.getSubMenu()) {
                            if (menuItem.getActionId() != null) {
                                Action action = p.getScope(MetaActions.class).get(menuItem.getActionId());
                                transfer(action, menuItem);
                                if (menuItem.getProperties() != null
                                        && menuItem.getProperties().get("security") != null
                                        && ((Security) menuItem.getProperties().get("security")).getSecurityMap() != null) {
                                    Map<String, Security.SecurityObject> securityMap = ((Security) menuItem
                                            .getProperties().get("security")).getSecurityMap();

                                    for (String securityObject : securityMap.keySet()) {
                                        if (!securityObjects.containsKey(securityObject)) {
                                            securityObjects.put(securityObject, new ArrayList<>());
                                        }
                                        securityObjects.get(securityObject).add(securityMap.get(securityObject));
                                    }
                                }
                            }
                        }
                        Security security = new Security();
                        security.setSecurityMap(new HashMap<>());
                        for (String securityKey : securityObjects.keySet()) {
                            Security.SecurityObject securityObject = new Security.SecurityObject();
                            merge(securityObject, securityObjects.get(securityKey));
                            if (!securityObject.isEmpty())
                                security.getSecurityMap().put(securityKey, securityObject);
                        }
                        if (!security.getSecurityMap().isEmpty()) {
                            if (b.getProperties() == null)
                                b.setProperties(new HashMap<>());
                            b.getProperties().put("security", security);
                        }
                    }
                }
            }
        }
        return compiled;
    }
}
