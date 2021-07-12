package net.n2oapp.framework.engine.test.source;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.engine.util.TestEntity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for invocation
 * Using in StaticInvocationEngineTest
 *
 * @author igafurov
 * @since 28.04.2017
 */
public class StaticInvocationTestClass {

    public static String methodWithoutArguments() {
        return "Invocation success";
    }

    public static String methodWithOneArgument(TestEntity argument) {
        return "Entity field mapped success";
    }

    public static TestEntity methodReturnedEntity(TestEntity argument) {
        return argument;
    }

    public static String methodWithOneArgument(String argument) {
        return argument + " invocation success";
    }

    public static String methodWithTwoArguments(String firstArgument, Integer secondArgument) {
        return "Invocation success. First argument: " + firstArgument + ", Second argument: " + secondArgument;
    }

    public static String methodWithThreeArguments(String first, Integer second, Boolean third) {
        return "Invocation success. First argument: " + first +
                ", Second argument: " + second +
                ", Third argument: " + third;
    }

    public static void methodVoid(String argument) {
    }

    public static Integer methodWithModel(Model model) {
        return model.getTestField();
    }

    public static void methodWithException() throws FileNotFoundException {
        throw new FileNotFoundException();
    }

    public static CollectionPage<Model> methodWithCriteria(MyCriteria criteria) {
        List<Model> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Model(i));
        }
        return new CollectionPage<>(100, list, criteria);
    }

    public static Integer sum(Model entityTypeArgument, Integer primitiveTypeArgument, Integer primitiveTypeArgument2, Model classTypeArgument) {
        return entityTypeArgument.getTestField() + primitiveTypeArgument + primitiveTypeArgument2 + classTypeArgument.getTestField();
    }

    /**
     * Test class for invocation with model in arguments
     */
    public static class Model {
        private Integer testField;

        public Model() {
        }

        public Model(Integer testField) {
            this.testField = testField;
        }

        public Integer getTestField() {
            return testField;
        }

        public void setTestField(Integer testField) {
            this.testField = testField;
        }
    }

    @Getter
    @Setter
    public static class MyCriteria extends Criteria {
        private String name;
    }
}
