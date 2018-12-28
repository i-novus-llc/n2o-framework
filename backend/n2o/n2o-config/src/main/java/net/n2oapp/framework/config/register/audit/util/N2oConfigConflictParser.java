package net.n2oapp.framework.config.register.audit.util;

import net.n2oapp.framework.config.register.audit.model.N2oConfigConflict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author dfirstov
 * @since 12.08.2015
 */
public class N2oConfigConflictParser {
    public static final String REGEX_LINE_END = "(\\r\\n|\\r|\\n|\\n\\r)";
    private static final String DEFAULT_LINE_END = "\n";
    public static final String START_LINE_CONFLICT = "<<<<<<< HEAD";
    public static final String MIDDLE_LINE_CONFLICT = "=======";
    public static final String FINISH_LINE_CONFLICT = ">>>>>>>";

    public static N2oConfigConflict restoreContentsByConflict(N2oConfigConflict configConflict) {
        String conflictContent = configConflict.getConflictContent();
        if (conflictContent == null || "".equals(conflictContent))
            return configConflict;
        Iterator<String> conflictIterator = toList(conflictContent).iterator();
        configConflict.setParentContent(getParent(conflictIterator));
        return configConflict;
    }

    private static List<String> toList(String conflictContent) {
        return Arrays.asList(conflictContent.split(REGEX_LINE_END));
    }

    private static String getParent(Iterator<String> i) {
        List<String> parents = new ArrayList<>();
        while (i.hasNext()) {
            String s = i.next();
            if (s.contains(START_LINE_CONFLICT)) {
                while (i.hasNext()) {
                    String conflict = i.next();
                    if (conflict.contains(MIDDLE_LINE_CONFLICT))
                        break;
                }
                while (i.hasNext()) {
                    String conflict = i.next();
                    if (conflict.contains(FINISH_LINE_CONFLICT)) {
                        break;
                    }
                }
                parents.add("");
            } else {
                parents.add(s);
            }
        }
        return convertToString(parents);
    }

    private static String convertToString(List<String> origins) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < origins.size()-1; i++) {
            str.append(origins.get(i)).append(DEFAULT_LINE_END);
        }
        str.append(origins.get(origins.size()-1));
        return str.toString();
    }
}
