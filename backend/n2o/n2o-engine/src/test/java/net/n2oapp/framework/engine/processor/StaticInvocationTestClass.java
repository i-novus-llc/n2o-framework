package net.n2oapp.framework.engine.processor;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class StaticInvocationTestClass {

    public static List<Model> methodWithoutArguments() {
        List<Model> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Model(i));
        }
        return list;
    }

    public static List<Model> methodWithOneArgument(String argument) {
        List<Model> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Model(i, argument, argument));
        }
        return list;
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

    public static CollectionPage<Model> methodWithCriteria(String id, MyCriteria criteria) {
        List<Model> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add(new Model(i, id, criteria.getName()));
        }
        return new CollectionPage<>(1, list, criteria);
    }

    public static Integer sum(Model entityTypeArgument, Integer primitiveTypeArgument, Model classTypeArgument) {
        return entityTypeArgument.getTestField() + primitiveTypeArgument + classTypeArgument.getTestField();
    }

    @Getter
    @Setter
    public static class Model {
        private Integer testField;
        private String value;
        private String name;

        public Model(Integer testField) {
            this.testField = testField;
        }

        public Model(Integer testField, String value, String name) {
            this.testField = testField;
            this.value = value;
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class MyCriteria extends Criteria {
        private String id;
        private String name;
    }
}
