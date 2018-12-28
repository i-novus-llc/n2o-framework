package net.n2oapp.framework.config.register.audit.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author dfirstov
 * @since 12.08.2015
 */
public class N2oConfigCommit {
    private String id;
    private String content;
    private String author;
    private N2oConfigMessage messagePrefix;
    private String message;
    private Date date;
    private Type type;
    private State state;
    private boolean conflict;

    public enum State{
        CHANGED, COMMITED, PUSHED
    }

    public enum Type{
        CREATE("{n2o.audit.created}", N2oConfigMessage.CREATED_PREFIX),
        UPDATE("{n2o.audit.updated}", N2oConfigMessage.UPDATED_PREFIX),
        DELETE("{n2o.audit.deleted}", N2oConfigMessage.DELETED_PREFIX),
        MERGE("{n2o.audit.merge}", N2oConfigMessage.MERGE_PREFIX, N2oConfigMessage.CONFLICT_MERGE_PREFIX),
        COMMIT("{n2o.audit.commit}", N2oConfigMessage.MANUAL_COMMIT, N2oConfigMessage.INIT_COMMIT_PREFIX),
        UPDATE_SYSTEM("{n2o.audit.updating}", N2oConfigMessage.SYSTEM_UPDATE_PREFIX);

        private String description;
        private List<N2oConfigMessage> prefixes;

        Type(String desc, N2oConfigMessage... prefix) {
            this.description = desc;
            this.prefixes = Arrays.asList(prefix);
        }

        public String getDescription() {
            return description;
        }

        public static Type byPrefix(N2oConfigMessage prefix) {
            if (prefix == null)
                return null;
            Optional<Type> first = Stream.of(Type.values()).filter(t -> t.prefixes.contains(prefix)).findFirst();
            if (!first.isPresent())
                return null;
            return first.get();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public N2oConfigMessage getMessagePrefix() {
        return messagePrefix;
    }

    public void setMessagePrefix(N2oConfigMessage messagePrefix) {
        this.messagePrefix = messagePrefix;
    }

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
