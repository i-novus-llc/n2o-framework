package net.n2oapp.framework.api.metadata.control.properties;

import net.n2oapp.framework.api.metadata.control.N2oFieldCondition;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toSet;

/**
 * User: operhod
 * Date: 19.08.14
 * Time: 16:30
 */
public class RequiredCondition extends N2oFieldCondition {

    private Map<String, String> vars = new HashMap<>();
    private String clientCondition;

    public String getClientCondition() {
        return clientCondition;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public void prepareForClient(ScriptProcessor scriptProcessor) {
        String condition = getCondition();

        Set<String> on;
        if (getOn() != null) {
            on = Arrays.stream(getOn().split(Pattern.quote(",")))
                    .map(String::trim)
                    .collect(toSet());
        } else {
            on = ScriptProcessor.extractVars(condition);
        }

        for (String var : on) {
            String jsVar = replaceBraces(var);
            jsVar = jsVar.replace(".", "");
            condition = condition.replaceAll(var, "\\$" + jsVar);
            vars.put("$" + jsVar, var);
        }
        // если выражение зависит от region и region.id, то во время замены может случится, что region заменится
        // дважды и перед ним задублируется знак $
        Pattern pattern = Pattern.compile("\\${2,}");
        Matcher matcher = pattern.matcher(condition);
        if(matcher.find()){
            condition = matcher.replaceAll("\\$");
        }

        this.clientCondition = condition;
    }

    public String getVarsEnumeration() {
        StringBuilder sb = new StringBuilder();
        boolean begin = true;
        for (String var : vars.keySet()) {
            if (!begin) sb.append(',');
            sb.append(var);
            begin = false;
        }
        return sb.toString();
    }

    private String replaceBraces(String var) {
        return var.replaceAll("\\[", "_").replaceAll("\\]", "_");
    }


}
