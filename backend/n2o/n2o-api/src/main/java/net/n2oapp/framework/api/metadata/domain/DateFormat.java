package net.n2oapp.framework.api.metadata.domain;

/**
 * User: iryabov
 * Date: 14.04.2014
 * Time: 17:57
 */
public enum DateFormat {
    DDMMYYYY("date", "DD.MM.YYYY"),
    DDMMYYYY_HHMM("date", "'DD.MM.YYYY HH:mm'");

    private String arg;
    private String func;
    private String format;

    DateFormat(String func, String arg) {
        this.arg = arg;
        format = func + " " + arg;
    }

    public String getFormat() {
        return format;
    }

    public String getFunc() {
        return func;
    }

    public String getArg() {
        return arg;
    }
}
