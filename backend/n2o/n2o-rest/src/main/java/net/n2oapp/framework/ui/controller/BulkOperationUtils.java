package net.n2oapp.framework.ui.controller;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.api.exception.N2oValidationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.regex.Pattern.quote;

/**
 * User: operhod
 * Date: 18.02.14
 * Time: 12:51
 */
public class BulkOperationUtils {

    public static final String BULK_MODEL_FIELD = "$bulkModel";
    public static final String MSG = "msg";
    public static final String SEVERITY = "severity";

    private static final String IDS_PREFIX = "_ids_";
    private static final String ABORT = null;
    private static final String IGNORE_ALL = "ignoreAll";
    private static final String IGNORE = "ignore";

    //API
    public static void throwExceptionWithChoice(Exception e, Map<String, Boolean> ignoreIds, String id, CompiledObject.Operation operation) {
        String summary = null;
        int httpStatus = 300;
        if (e instanceof N2oValidationException) {
            N2oValidationException n2oException = (N2oValidationException) e;
            summary = n2oException.getMessage();
            httpStatus = n2oException.getHttpStatus();
        }
        String defaultSummary = "net.n2oapp.framework.ui.dialogs.bulk.fail";
        summary = summary != null ? summary : defaultSummary;
        summary = String.format("<div class=\"n2o-dialog-choice__text\">%s</div>", summary);
        N2oException n2oException = new N2oUserException(summary);
        n2oException.setSeverity(SeverityType.warning);
        n2oException.setHttpStatus(httpStatus);
        //n2oException.setChoice(createChoice(ignoreIds)); todo use dialog
        n2oException.setData(Arrays.asList(operation.getName(), id));
        throw n2oException;
    }

    public static Choice deserializeChoice(String choice) {
        Choice res = new Choice();
        if (choice == null || choice.length() < 1) {
            return res;
        }
        String s[] = choice.split(quote(IDS_PREFIX));
        switch (s[0]) {
            case IGNORE_ALL:
                res.ignoreAll = true;
                res.ignoreIds = deserializeIgnoreIds(s[1]);
                break;
            case IGNORE:
                res.ignore = true;
                res.ignoreIds = deserializeIgnoreIds(s[1]);
                break;
        }
        return res;
    }

    public static ResponseMessage createFinalResponse(Choice choice, CompiledObject.Operation operation) {
        ResponseMessage message = new ResponseMessage();
        if (choice.getErrorIdsCount() == 0) {
            message.setText("net.n2oapp.framework.ui.dialogs.bulk.successAll");
            message.setSeverityType(SeverityType.success);
        } else if (choice.getErrorIdsCount() == choice.getIdsCount()) {
            message.setText("net.n2oapp.framework.ui.dialogs.bulk.failAll");
            message.setSeverityType(SeverityType.danger);
        } else {
            message.setText("net.n2oapp.framework.ui.dialogs.bulk.success");
            message.setSeverityType(SeverityType.info);
        }
        return message;
    }

    //utils

    @Deprecated //use dialog
    private static Map<String, String> createChoice(Map<String, Boolean> ignoreIds) {
        Map<String, String> res = new HashMap<>();
        String serializedIgnoreIds = serializeIgnoreIds(ignoreIds);
        res.put("Пропустить", IGNORE + IDS_PREFIX + serializedIgnoreIds);
        res.put("Пропустить все", IGNORE_ALL + IDS_PREFIX + serializedIgnoreIds);
        res.put("Прервать", ABORT);
        return res;
    }

    private static String serializeIgnoreIds(Map<String, Boolean> ignoreIds) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : ignoreIds.keySet()) {
            if (!first) sb.append(',');
            sb.append(key);
            sb.append(':');
            sb.append(ignoreIds.get(key));
            first = false;
        }
        return sb.toString();
    }

    private static Map<String, Boolean> deserializeIgnoreIds(String ignoreIds) {
        if (ignoreIds == null || ignoreIds.length() < 1) return Collections.emptyMap();
        Map<String, Boolean> res = new HashMap<>();
        String[] idAndSuccessPairs = ignoreIds.split(",");
        for (String idAndSuccessPair : idAndSuccessPairs) {
            String[] tmp = idAndSuccessPair.split(":");
            res.put(tmp[0], Boolean.valueOf(tmp[1]));
        }
        return res;
    }


    public static class Choice {
        Map<String, Boolean> ignoreIds = new HashMap<>();
        private boolean ignoreAll = false;
        private boolean ignore = false;
        private boolean confirmationPast = false;

        public boolean isConfirmationPast() {
            return confirmationPast;
        }

        public boolean isIgnoreAll() {
            return ignoreAll;
        }

        public boolean isIgnore() {
            return ignore;
        }

        public Map<String, Boolean> getIgnoreIdsMap() {
            return ignoreIds;
        }

        public int getSuccessIdsCount() {
            int i = 0;
            for (String id : getIgnoreIdsMap().keySet()) {
                if (ignoreIds.get(id)) i++;
            }
            return i;
        }

        public int getErrorIdsCount() {
            return getIdsCount() - getSuccessIdsCount();
        }

        public int getIdsCount() {
            return ignoreIds.size();
        }


    }


}
