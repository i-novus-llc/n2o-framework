package net.n2oapp.framework.access.api.model;

import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.N2oSecurityException;
import net.n2oapp.framework.api.exception.N2oUserException;

/**
 * User: operhod
 * Date: 28.10.13
 * Time: 15:58
 */
public class Permission {

    public static Permission denied(String detailedMessage, String techMessage) {
        Permission res = new Permission(false);
        res.setDetailedMessage(detailedMessage);
        res.setTechMessage(techMessage);
        return res;
    }

    public static Permission denied(String techMessage) {
        Permission res = new Permission(false);
        res.setTechMessage(techMessage);
        return res;
    }

    public static Permission allowed(String techMessage) {
        Permission res = new Permission(true);
        res.setTechMessage(techMessage);
        return res;
    }

    public static Permission allowed() {
        return new Permission(true);
    }


    private boolean allowed;
    private String detailedMessage;
    private String techMessage;
    private Object placeholders;

    public Permission(boolean allowed) {
        this.allowed = allowed;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public void setTechMessage(String techMessage) {
        this.techMessage = techMessage;
    }

    public void setPlaceholders(Object placeholders) {
        this.placeholders = placeholders;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public String getTechMessage() {
        return techMessage;
    }

    private void throwException() throws N2oSecurityException {
        throw getException();
    }

    public N2oUserException getException() {
        return new AccessDeniedException(detailedMessage);
    }

    public void resolveToException() throws N2oSecurityException {
        if (!allowed)
            throwException();
    }

    public void addCommentToTechInfo(String comment) {
        if (getTechMessage() != null)
            setTechMessage("[" + comment + "]\r\n" + getTechMessage());
    }

    @Override
    public String toString() {
        return String.format("{allowed:%s, message:%s, tech-message:%s}", allowed, detailedMessage, techMessage);
    }
}
