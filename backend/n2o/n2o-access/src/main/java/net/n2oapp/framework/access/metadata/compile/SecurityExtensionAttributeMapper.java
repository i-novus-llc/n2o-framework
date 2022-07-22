package net.n2oapp.framework.access.metadata.compile;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.ExtensionAttributeMapper;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Сборка атрибутов прав доступа
 */
@Component
public class SecurityExtensionAttributeMapper implements ExtensionAttributeMapper {
    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/security-1.0";
    }

    @Override
    public Map<String, Object> mapAttributes(Map<String, String> attributes, CompileProcessor p) {
        Map<String, Object> result = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(attributes.containsKey("denied") ? Boolean.valueOf(attributes.get("denied")) : null);
        securityObject.setPermitAll(attributes.containsKey("permit-all") ? Boolean.valueOf(attributes.get("permit-all")) : null);
        securityObject.setRoles(parseAttributes(attributes.get("roles")));
        securityObject.setPermissions(parseAttributes(attributes.get("permissions")));
        securityObject.setUsernames(parseAttributes(attributes.get("usernames")));
        securityObject.setAuthenticated(attributes.containsKey("authenticated") ? Boolean.valueOf(attributes.get("authenticated")) : null);
        securityObject.setAnonymous(attributes.containsKey("anonymous") ? Boolean.valueOf(attributes.get("anonymous")) : null);
        Map<String, Security.SecurityObject> securityMap = new HashMap<>();
        securityMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityMap);
        result.put(Security.SECURITY_PROP_NAME, security);
        return result;
    }

    /**
     * Разбивает строку по запятым, игнорируя пробелы до/после запятой
     */
    private Set<String> parseAttributes(String str) {
        return str != null ? new HashSet<>(Arrays.asList(str.split("\\s*,\\s*"))) : null;
    }
}
