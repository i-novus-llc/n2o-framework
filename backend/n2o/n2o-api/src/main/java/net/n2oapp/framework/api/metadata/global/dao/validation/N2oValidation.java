package net.n2oapp.framework.api.metadata.global.dao.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 16.12.13
 * Time: 14:42
 */
@Getter
@Setter
public abstract class N2oValidation implements IdAware, Source, NamespaceUriAware {
    private String id;
    private String fieldId;
    private SeverityType severity;
    private ClientMoment clientMoment;
    private ServerMoment serverMoment;
    private String message;
    private String namespaceUri;
    private String enabled;
    private String side;

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getNamespaceUri() {
        return "";
    }

    //для персистеров 1.0, 2.0
    public String getMoment() {
        if (serverMoment == null) return null;
        if (serverMoment.equals(ServerMoment.beforeOperation))
            return "before-action";
        else if (serverMoment.equals(ServerMoment.afterSuccessOperation))
            return "after-success-action";
        else if (serverMoment.equals(ServerMoment.afterFailOperation))
            return "after-fail-action";
        else
            return serverMoment.getId();
    }

    //для ридеров 1.0, 2.0
    public void setMoment(String moment) {
        if (moment == null)
            this.serverMoment = null;
        else if ("before-action".equalsIgnoreCase(moment))
            this.serverMoment = ServerMoment.beforeOperation;
        else if ("after-success-action".equalsIgnoreCase(moment))
            this.serverMoment = ServerMoment.afterSuccessOperation;
        else if ("after-fail-action".equalsIgnoreCase(moment))
            this.serverMoment = ServerMoment.afterFailOperation;
        else if ("before-query".equalsIgnoreCase(moment))
            this.serverMoment = ServerMoment.beforeQuery;
        else if ("after-success-query".equalsIgnoreCase(moment))
            this.serverMoment = ServerMoment.afterSuccessQuery;
        else if ("after-fail-query".equalsIgnoreCase(moment))
            this.serverMoment = ServerMoment.afterFailQuery;
    }

    //для персистеров 1.0, 2.0
    public Level getLevel() {
        if (SeverityType.danger.equals(severity))
            return Level.error;
        return Level.valueOf(severity.name());
    }

    //для ридеров 1.0, 2.0
    public void setLevel(Level level) {
        severity = level.getSeverity();
    }

    @Deprecated
    public enum Level implements Serializable {
        error(SeverityType.danger), warning(SeverityType.warning), info(SeverityType.info), success(SeverityType.success);

        private SeverityType severity;

        Level(SeverityType severity) {
            this.severity = severity;
        }

        public SeverityType getSeverity() {
            return severity;
        }
    }

    public enum ServerMoment implements IdAware {
        beforeOperation("before-operation"),
        beforeQuery("before-query"),
        afterSuccessQuery("after-success-query"),
        afterFailQuery("after-fail-query"),
        afterFailOperation("after-fail-operation"),
        afterSuccessOperation("after-success-operation");

        private String id;

        ServerMoment(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    public enum ClientMoment implements IdAware {
        beforeLoad("before-load"),
        afterLoad("after-load"),
        beforeStore("before-store"),
        afterStore("after-store"),
        beforeSubmit("before-submit"),
        afterSubmit("after-submit");

        private String id;

        ClientMoment(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
