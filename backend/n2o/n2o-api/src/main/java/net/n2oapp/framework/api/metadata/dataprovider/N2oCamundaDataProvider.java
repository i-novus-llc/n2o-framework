package net.n2oapp.framework.api.metadata.dataprovider;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oMapInvocation;

/**
 * Camunda провайдер данных
 */
@Getter
@Setter
public class N2oCamundaDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private OperationEnum operation;

    @RequiredArgsConstructor
    @Getter
    public enum OperationEnum implements N2oEnum {
        COUNT_TASKS("countTasks"),
        FIND_TASKS("findTasks"),
        GET_TASK("getTask"),
        SET_TASK_VARIABLES("setTaskVariables"),
        COMPLETE_TASK("completeTask"),
        START_PROCESS("startProcess");

        private final String id;
    }

}
