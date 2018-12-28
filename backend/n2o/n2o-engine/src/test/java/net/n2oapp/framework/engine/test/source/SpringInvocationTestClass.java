package net.n2oapp.framework.engine.test.source;

import java.io.FileNotFoundException;

/**
 * Test class for invocation spring method
 * Using in SpringInvocationEngineTest
 *
 * @author igafurov
 * @since 11.05.2017
 */
public class SpringInvocationTestClass {

    public String methodWithoutArguments() {
        return "Invocation success";
    }

    public String methodWithOneArgument(String argument) {
        return argument + " invocation success";
    }

    public String methodWithTwoArguments(String firstArgument, Integer secondArgument) {
        return "Invocation success. First argument: " + firstArgument + ", Second argument: " + secondArgument;
    }

    public void methodVoid(String argument) {
    }

    public String methodWithModel(Model model) {
        return model.getTestField();
    }

    public void methodWithException() throws FileNotFoundException {
        throw new FileNotFoundException();
    }

    /**
     * Test class for invocation with model in arguments
     */
    public static class Model {
        private String testField;

        public String getTestField() {
            return testField;
        }

        public void setTestField(String testField) {
            this.testField = testField;
        }
    }
}
