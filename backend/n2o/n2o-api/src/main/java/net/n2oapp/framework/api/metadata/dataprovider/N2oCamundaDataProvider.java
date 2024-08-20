package net.n2oapp.framework.api.metadata.dataprovider;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oMapInvocation;

/**
 * Camunda провайдер данных
 */
@Getter
@Setter
public class N2oCamundaDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private Operation operation;

    public enum Operation {
        countTasks,
        findTasks,
        getTask,
        setTaskVariables,
        completeTask,
        startProcess
    }

}
