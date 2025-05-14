package net.n2oapp.framework.examples.bpm;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest extends AbstractProcessEngineRuleTest {

    @Autowired
    public RuntimeService runtimeService;

    @Test
    void contextLoads() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("recruitment");

        BpmnAwareTests.assertThat(processInstance).isStarted()
                .task()
                .hasDefinitionKey("response")
                .isNotAssigned();
    }
}