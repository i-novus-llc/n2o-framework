package net.n2oapp.framework.config.register.audit.model;

import static net.n2oapp.framework.config.register.audit.model.N2oConfigMergeMode.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * Виды префиксов сообщения git по соглашениям аудита конфигураций
 */
public enum N2oConfigMessage {
    INIT_COMMIT_PREFIX("[INIT COMMIT] "),
    SERVER_PREFIX("[SERVER] "),
    CREATED_PREFIX("[CREATED] "),
    UPDATED_PREFIX("[UPDATED] "),
    DELETED_PREFIX("[DELETED] "),
    DUPLICATE_DELETED_PREFIX("[DUPLICATE DELETED] "),
    RESOLVED_PREFIX("[RESOLVED] "),
    RESOLVED_AUTO_PREFIX("RESOLVED AUTO "),
    RESOLVED_TO_SYSTEM_PREFIX("[RESOLVED TO SYSTEM] "),
    MERGE_PREFIX("[MERGE] "),
    CONFLICT_MERGE_PREFIX("[CONFLICT MERGE] "),
    MERGE_BRANCH_INFO(" branch '%s' into '%s' "),
    TEMPLATE_PREFIX("[%s] "),
    MANUAL_COMMIT("[MANUAL COMMIT] "),
    SYSTEM_UPDATE_PREFIX("[UPDATE SYSTEM] ");

    N2oConfigMessage(String value) {
        this.value = value;
    }

    public final String value;

    @Override
    public String toString() {
        return this.value;
    }

    public static String translate(String message) {
        if (message.startsWith(SERVER_PREFIX.value))
            return "{n2o.serverVersionUpdated}";
        if (message.startsWith(CREATED_PREFIX.value))
            return "{n2o.audit.created}";
        if (message.startsWith(UPDATED_PREFIX.value))
            return "{n2o.audit.updated}";
        if (message.startsWith(DELETED_PREFIX.value))
            return "{n2o.audit.deleted}";
        if (message.startsWith(DUPLICATE_DELETED_PREFIX.value))
            return "{n2o.duplicateDeleted}";
        if (message.startsWith(RESOLVED_PREFIX.value))
            return "{n2o.manualModeConflictResolved}";
        if (message.startsWith(RESOLVED_TO_SYSTEM_PREFIX.value))
            return "{n2o.manualModeConflictResolvedSystemVersionUpdated}";
        if (message.startsWith(MERGE_PREFIX.value))
            return "{n2o.mergedSuccessfully}";
        if (message.startsWith(CONFLICT_MERGE_PREFIX.value))
            return "{n2o.mergedWithConflicts}";
        if (message.startsWith(MANUAL_COMMIT.value))
            return message.replace(MANUAL_COMMIT.value, "{n2o.manualCommit}");
        if (containsMergeMode(message, OURS))
            return "{n2o.autoModeConflictResolvedRollbackToServerVer}";
        if (containsMergeMode(message, THEIRS))
            return "{n2o.autoModeConflictResolvedRollbackToSystemVer}";
        if (containsMergeMode(message, MERGE_OURS))
            return "{n2o.autoModeConflictResolvedUpdateConflictStringsWithServerVer}";
        if (containsMergeMode(message, MERGE_THEIRS))
            return "{n2o.autoModeConflictResolvedUpdateConflictStringsWithSystemVer}";
        return message;
    }

    private static boolean containsMergeMode(String message, N2oConfigMergeMode mode) {
        return message.contains(mode.getValue().toUpperCase());
    }

    public static boolean hasPrefix(String message) {
        List<String> prefixes = Arrays.asList(N2oConfigMessage.values()).stream()
                .flatMap(m -> Stream.of(m.value)).collect(Collectors.toList());
        List<String> mergeModes = Arrays.asList(N2oConfigMergeMode.values()).stream()
                .flatMap(m -> Stream.of(String.format(TEMPLATE_PREFIX.value, RESOLVED_AUTO_PREFIX + m.getValue().toUpperCase()))).collect(Collectors.toList());
        prefixes.addAll(mergeModes);//todo точно ли это используется?
        return prefixes.stream().anyMatch(message::startsWith);
    }

    public static Optional<N2oConfigMessage> getPrefix(String fullMessage) {
        return Arrays.asList(N2oConfigMessage.values()).stream()
                .filter(m -> fullMessage.startsWith(m.value)).findFirst();
    }

    public static String getMessage(String fullMessage) {
        Optional<N2oConfigMessage> prefix = getPrefix(fullMessage);
        return prefix.isPresent() ? fullMessage.substring(prefix.get().value.length()) : fullMessage;
    }

}