package net.n2oapp.framework.access.metadata;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Права доступа на клиенте
 */
@Setter
public class Security implements Serializable {
    /**
     * Ключ объекта Security в properties
     */
    public static String SECURITY_PROP_NAME = "security";

    private Map<String, SecurityObject> securityMap;

    @JsonAnyGetter
    public Map<String, SecurityObject> getSecurityMap() {
        return securityMap;
    }

    @Getter
    @Setter
    public static class SecurityObject implements Serializable {
        @JsonProperty
        private Boolean denied;
        @JsonProperty
        private Boolean permitAll;
        @JsonProperty
        private Set<String> roles;
        @JsonProperty
        private Set<String> permissions;
        @JsonProperty
        private Set<String> usernames;
        @JsonProperty
        private Boolean authenticated;
        @JsonProperty
        private Boolean anonymous;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SecurityObject that = (SecurityObject) o;

            if (roles != null ? !roles.equals(that.roles) : that.roles != null) return false;
            if (permissions != null ? !permissions.equals(that.permissions) : that.permissions != null) return false;
            if (usernames != null ? !usernames.equals(that.usernames) : that.usernames != null) return false;
            if (authenticated != null ? !authenticated.equals(that.authenticated) : that.authenticated != null)
                return false;
            return anonymous != null ? anonymous.equals(that.anonymous) : that.anonymous == null;
        }

        @Override
        public int hashCode() {
            int result = roles != null ? roles.hashCode() : 0;
            result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
            result = 31 * result + (usernames != null ? usernames.hashCode() : 0);
            result = 31 * result + (authenticated != null ? authenticated.hashCode() : 0);
            result = 31 * result + (anonymous != null ? anonymous.hashCode() : 0);
            return result;
        }

        public boolean isEmpty() {
            return (getRoles() == null || getRoles().size() == 0)
                    && (getPermissions() == null || getPermissions().size() == 0)
                    && (getUsernames() == null || getUsernames().size() == 0)
                    && getAuthenticated() == null
                    && getPermitAll() == null
                    && getAnonymous() == null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Security security = (Security) o;

        return securityMap != null ? securityMap.equals(security.securityMap) : security.securityMap == null;
    }

    @Override
    public int hashCode() {
        return securityMap != null ? securityMap.hashCode() : 0;
    }
}
