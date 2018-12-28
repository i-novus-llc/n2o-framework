package net.n2oapp.framework.api.metadata.global.dao.execution.defaults;

@FunctionalInterface
public interface Calculation {

    String calculate(String expression, String id);

}
