package net.n2oapp.framework.test;

import net.n2oapp.framework.api.metadata.dataprovider.N2oCamundaDataProvider;
import net.n2oapp.framework.boot.camunda.CamundaDataProviderEngine;
import net.n2oapp.framework.boot.camunda.ExtTask;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CamundaDataProviderEngineTest {

    @MockBean
    ProcessEngine processEngine;

    @MockBean
    TaskService taskService;

    @MockBean
    TaskQuery taskQuery;

    @MockBean
    RuntimeService runtimeService;

    @Autowired
    CamundaDataProviderEngine engine;

    private N2oCamundaDataProvider invocation;

    @BeforeEach
    public void config() {
        invocation = new N2oCamundaDataProvider();
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
    }

    @Test
    void getCountTasksTest() {
        when(taskQuery.count()).thenReturn(99L);

        invocation.setOperation(N2oCamundaDataProvider.OperationEnum.countTasks);
        Object result = engine.invoke(invocation, new HashMap<>());

        assertThat(result, instanceOf(Long.class));
        assertThat(result, is(99L));
    }

    @Test
    void findTasksTest() {
        List<Task> list = new ArrayList<>();
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId("2-2-2");
        list.add(taskEntity);
        when(taskQuery.listPage(0, 10)).thenReturn(list);

        when(taskService.getVariables(eq("2-2-2"), anyCollection())).thenReturn(
                new HashMap<String, Object>() {{
                    put("position", "pos1");
                    put("salary", "100500");
                }});

        invocation.setOperation(N2oCamundaDataProvider.OperationEnum.findTasks);
        Map<String, Object> params = new HashMap<>();
        params.put("select", Collections.singletonList("position"));

        Object result = engine.invoke(invocation, params);

        assertThat(result, instanceOf(List.class));
        ExtTask taskRes = (ExtTask) ((List) result).get(0);
        assertThat(taskRes.getId(), is("2-2-2"));
        assertThat(taskRes.getVariables().size(), is(2));
    }

    @Test
    void getTaskTest() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId("1-1");
        when(taskQuery.taskId("1-1")).thenReturn(taskQuery);
        when(taskQuery.singleResult()).thenReturn(taskEntity);

        when(taskService.getVariables("1-1")).thenReturn(new HashMap<String, Object>() {{
            put("position", "pos1");
            put("salary", "100500");
        }});

        invocation.setOperation(N2oCamundaDataProvider.OperationEnum.getTask);

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("id", "1-1");
        }};

        ExtTask result = (ExtTask)engine.invoke(invocation, params);
        assertThat(result.getId(), is("1-1"));
        assertThat(result.getVariables().size(), is(2));
    }

    @Test
    void setTaskVariablesTest() {
        invocation.setOperation(N2oCamundaDataProvider.OperationEnum.setTaskVariables);

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("id", "3-3-3");
            put("position", "pos1");
            put("salary", "100500");
        }};

        Object result = engine.invoke(invocation, params);
        assertThat(result, is(true));

        Mockito.verify(taskService).setVariables(eq("3-3-3"), anyMap());
    }

    @Test
    void completeTaskVariablesTest() {
        invocation.setOperation(N2oCamundaDataProvider.OperationEnum.completeTask);

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("id", "3-4-5");
            put("position", "pos1");
            put("salary", "100500");
        }};

        Object result = engine.invoke(invocation, params);
        assertThat(result, is(true));

        Mockito.verify(taskService).complete(eq("3-4-5"), anyMap());
    }

    @Test
    void startProcessTest() {
        when(runtimeService.startProcessInstanceByKey(eq("recruitment"), anyMap())).thenAnswer(a -> {
            ExecutionEntity res = new ExecutionEntity();
            res.setId(a.getArgument(0));
            return res;
        });

        invocation.setOperation(N2oCamundaDataProvider.OperationEnum.startProcess);

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("id", "recruitment");
            put("position", "pos1");
            put("salary", "100500");
        }};

        Object result = engine.invoke(invocation, params);
        assertThat(result, instanceOf(String.class));
        assertThat(result, is("recruitment"));
    }


}
