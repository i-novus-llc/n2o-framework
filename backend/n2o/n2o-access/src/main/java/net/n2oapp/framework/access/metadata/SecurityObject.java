package net.n2oapp.framework.access.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import org.apache.commons.collections.CollectionUtils;

import java.util.Set;

@Getter
@Setter
public class SecurityObject implements Compiled {
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
        return CollectionUtils.isEmpty(getRoles())
                && CollectionUtils.isEmpty(getPermissions())
                && CollectionUtils.isEmpty(getUsernames())
                && getAuthenticated() == null
                && getPermitAll() == null
                && getAnonymous() == null;
    }
}
