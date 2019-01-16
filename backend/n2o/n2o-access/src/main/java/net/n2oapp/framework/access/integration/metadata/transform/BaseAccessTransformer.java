package net.n2oapp.framework.access.integration.metadata.transform;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;
import net.n2oapp.framework.access.simple.PermissionAndRoleCollector;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileTransformer;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.access.simple.PermissionAndRoleCollector.OBJECT_ACCESS;
import static net.n2oapp.framework.access.simple.PermissionAndRoleCollector.PAGE_ACCESS;

@Getter
@Setter
public abstract class BaseAccessTransformer<D extends Compiled, C extends CompileContext<?, ?>>
        implements CompileTransformer<D, C>, CompiledClassAware {

    @Value("${n2o.access.N2oObjectAccessPoint.default:false}")
    private Boolean defaultObjectAccess;

    @Value("${n2o.access.N2oPageAccessPoint.default:true}")
    private Boolean defaultPageAccess;

    @Value("${n2o.access.N2oUrlAccessPoint.default:true}")
    private Boolean defaultUrlAccess;

    protected void collectObjectAccess(PropertiesAware compiled, String objectId,
                                       String operationId, SimpleCompiledAccessSchema schema) {
        if (objectId == null) return;
        if (compiled.getProperties() == null) {
            compiled.setProperties(new HashMap<>());
        }
        if (compiled.getProperties().get("security") == null) {
            compiled.getProperties().put("security", new Security());
        }
        Security security = (Security) compiled.getProperties().get("security");
        Security.SecurityObject securityObject = new Security.SecurityObject();
        if (security.getSecurityMap() == null) {
            security.setSecurityMap(new HashMap<>());
        } else if (security.getSecurityMap().get("object") != null
                || security.getSecurityMap().get("custom") != null) return;

        if (schema.getPermitAllPoints() != null) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oObjectAccessPoint
                            && StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getObjectId(), objectId)
                            && (((N2oObjectAccessPoint) ap).getAction() == null ||
                                StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getAction(), operationId)))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setPermitAll(true);
                                    securityObject.setAnonymous(false);
                                    securityObject.setAuthenticated(false);
                                }
                                return list;
                            }
                    ));
        }

        if (schema.getAuthenticatedPoints() != null) {
            schema.getAuthenticatedPoints().stream()
                    .filter(ap -> ap instanceof N2oObjectAccessPoint
                            && StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getObjectId(), objectId)
                            && (((N2oObjectAccessPoint) ap).getAction() == null ||
                                StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getAction(), operationId)))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setPermitAll(false);
                                    securityObject.setAnonymous(false);
                                    securityObject.setAuthenticated(true);
                                }
                                return list;
                            }
                    ));
        }

        if (schema.getAnonymousPoints() != null) {
            schema.getAnonymousPoints().stream()
                .filter(ap -> ap instanceof N2oObjectAccessPoint
                        && StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getObjectId(), objectId)
                        && (((N2oObjectAccessPoint) ap).getAction() == null ||
                            StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getAction(), operationId)))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setPermitAll(false);
                                    securityObject.setAnonymous(true);
                                    securityObject.setAuthenticated(false);
                                }
                                return list;
                            }
                    ));
        }

        List<N2oUserAccess> userAccesses = PermissionAndRoleCollector.collectUsers(N2oObjectAccessPoint.class,
                OBJECT_ACCESS.apply(objectId, operationId), schema);
        if (userAccesses != null && userAccesses.size() > 0) {
            securityObject.setUsernames(userAccesses
                    .stream()
                    .map(N2oUserAccess::getId)
                    .collect(Collectors.toList()));
        }
        List<N2oPermission> permissions = PermissionAndRoleCollector.collectPermission(N2oObjectAccessPoint.class,
                OBJECT_ACCESS.apply(objectId, operationId), schema);
        if (permissions != null && permissions.size() > 0) {
            securityObject.setPermissions(permissions
                    .stream()
                    .map(N2oPermission::getId)
                    .collect(Collectors.toList()));
        }
        List<N2oRole> roles = PermissionAndRoleCollector.collectRoles(N2oObjectAccessPoint.class,
                OBJECT_ACCESS.apply(objectId, operationId), schema);
        if (roles != null && roles.size() > 0) {
            securityObject.setRoles(roles
                    .stream()
                    .map(N2oRole::getId)
                    .collect(Collectors.toList()));
        }

        if (securityObject.isEmpty()) {
            securityObject.setPermitAll(defaultObjectAccess);
            securityObject.setDenied(!defaultObjectAccess);
        }
        security.getSecurityMap().put("object", securityObject);
    }

    protected void collectPageAccess(PropertiesAware compiled, String pageId, SimpleCompiledAccessSchema schema) {
        if (compiled.getProperties() == null) {
            compiled.setProperties(new HashMap<>());
        }
        if (compiled.getProperties().get("security") == null) {
            compiled.getProperties().put("security", new Security());
        }
        Security security = (Security) compiled.getProperties().get("security");
        if (security.getSecurityMap() == null) {
            security.setSecurityMap(new HashMap<>());
        } else if (security.getSecurityMap().get("page") != null
                || security.getSecurityMap().get("custom") != null) return;

        Security.SecurityObject securityObject = new Security.SecurityObject();

        if (schema.getPermitAllPoints() != null) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oPageAccessPoint
                            && ((N2oPageAccessPoint)ap).getPage().equals(pageId))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setAnonymous(true);
                                    securityObject.setAuthenticated(false);
                                }
                                return list;
                            }
                    ));
        }

        if (schema.getAuthenticatedPoints() != null) {
            schema.getAuthenticatedPoints().stream()
                    .filter(ap -> ap instanceof N2oPageAccessPoint
                            && ((N2oPageAccessPoint)ap).getPage().equals(pageId))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setAnonymous(false);
                                    securityObject.setAuthenticated(true);
                                }
                                return list;
                            }
                    ));
        }

        List<N2oRole> roles = PermissionAndRoleCollector.collectRoles(N2oPageAccessPoint.class,
                PAGE_ACCESS.apply(pageId), schema);
        if (roles != null && roles.size() > 0) {
            securityObject.setRoles(
                    roles
                            .stream()
                            .map(N2oRole::getId)
                            .collect(Collectors.toList())
            );
        }

        List<N2oPermission> permissions = PermissionAndRoleCollector.collectPermission(N2oPageAccessPoint.class,
                PAGE_ACCESS.apply(pageId), schema);
        if (permissions != null && permissions.size() > 0) {
            securityObject.setPermissions(
                    permissions
                            .stream()
                            .map(N2oPermission::getId)
                            .collect(Collectors.toList())
            );
        }

        List<N2oUserAccess> userAccesses = PermissionAndRoleCollector.collectUsers(N2oPageAccessPoint.class,
                PAGE_ACCESS.apply(pageId), schema);
        if (userAccesses != null && userAccesses.size() > 0) {
            securityObject.setUsernames(
                    userAccesses
                            .stream()
                            .map(N2oUserAccess::getId)
                            .collect(Collectors.toList())
            );
        }

        if (securityObject.isEmpty()) {
            securityObject.setPermitAll(defaultPageAccess);
            securityObject.setDenied(!defaultPageAccess);
        }
        security.getSecurityMap().put("page", securityObject);
    }

    protected void transfer(PropertiesAware from, PropertiesAware to) {
        if (from == null || from.getProperties() == null)
            return;
        Map<String, Object> properties = from.getProperties();
        if (properties == null
                || properties.get("security") == null
                || to.getProperties() != null
                && to.getProperties().containsKey("security"))
            return;
        if (to.getProperties() == null) {
            to.setProperties(new HashMap<>());
        }
        to.getProperties().put("security", properties.get("security"));
    }

}
