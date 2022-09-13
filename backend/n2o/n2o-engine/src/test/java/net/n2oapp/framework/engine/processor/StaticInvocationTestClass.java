package net.n2oapp.framework.engine.processor;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.engine.data.normalize.Normalizer;

import java.io.FileNotFoundException;
import java.util.*;


public class StaticInvocationTestClass {

    public static DataSet normalizeOrganization() {
        DataSet result = new DataSet();

        List<DataSet> employees = new ArrayList<>();
        DataSet employee1 = new DataSet();
        employee1.put("normalizedId", 173);
        employee1.put("normalizedName", "employee173");
        employees.add(employee1);

        result.put("normalizedCode", 102);
        result.put("normalizedTitle", "refNormalize");
        result.put("normalizedEmployees", employees);
        return result;
    }

    public static DataList normalizeEmployees() {
        DataList result = new DataList();

        DataSet employee1 = new DataSet();
        employee1.put("normalizedId", 273);
        employee1.put("normalizedName", "employee273");

        result.add(employee1);
        return result;
    }

    public static DataList normalizeDepartments() {
        DataList result = new DataList();

        DataSet normalizedDepartment1 = new DataSet();
        normalizedDepartment1.put("id", 103);
        normalizedDepartment1.put("name", "department103");

        DataList normalizedGroups1 = new DataList();
        DataSet group11 = new DataSet();
        group11.put("id", 109);
        group11.put("name", "group109");
        DataSet group12 = new DataSet();
        group12.put("id", 110);
        group12.put("name", "group110");
        normalizedGroups1.add(group11);
        normalizedGroups1.add(group12);
        normalizedDepartment1.put("groups", normalizedGroups1);

        DataSet normalizedManager1 = new DataSet();
        normalizedManager1.put("id", 422);
        normalizedManager1.put("name", "manager422");
        normalizedDepartment1.put("manager", normalizedManager1);
        result.add(normalizedDepartment1);

        DataSet normalizedDepartment2 = new DataSet();
        normalizedDepartment2.put("id", 104);
        normalizedDepartment2.put("name", "department104");

        DataList normalizedGroups2 = new DataList();
        DataSet group21 = new DataSet();
        group21.put("id", 109);
        group21.put("name", "group109");
        DataSet group22 = new DataSet();
        group22.put("id", 110);
        group22.put("name", "group110");
        normalizedGroups2.add(group21);
        normalizedGroups2.add(group22);
        normalizedDepartment2.put("groups", normalizedGroups2);

        DataSet normalizedManager2 = new DataSet();
        normalizedManager2.put("id", 1422);
        normalizedManager2.put("name", "manager1422");
        normalizedDepartment2.put("manager", normalizedManager2);
        result.add(normalizedDepartment2);

        return result;
    }

    public static DataSet defaultOrganization() {
        DataSet result = new DataSet();
        DataList employees = new DataList();

        DataSet employee1 = new DataSet();
        DataSet employee2 = new DataSet("name", "nameFromReferenceNormalize");
        employees.add(employee1);
        employees.add(employee2);

        result.put("employees", employees);
        return result;
    }

    public static List<DataSet> resultListNormalize(List<DataSet> data) {
        DataSet resultDataSet = new DataSet();
        List<DataSet> ids = new ArrayList<>();
        List<DataSet> names = new ArrayList<>();
        for (DataSet item : data) {
            ids.add(new DataSet("id", item.get("id")));
            names.add(new DataSet("name", item.get("name")));
        }
        resultDataSet.put("ids", ids);
        resultDataSet.put("names", names);

        return Collections.singletonList(resultDataSet);
    }

    public static DataSet resultUniqueNormalize(DataSet data) {
        DataSet result = new DataSet();
        result.put("id", data.get("id"));

        DataSet info = new DataSet();
        info.put("name", data.get("name"));
        info.put("type", data.get("type"));
        result.put("info", info);
        return result;
    }

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

    public static CollectionPage<Model> methodWithThreeArguments(Integer first, MyCriteria criteria, Boolean third) {
        return new CollectionPage<>(1, Arrays.asList(new Model(first, third.toString(), criteria.getName())), criteria);
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

    @Normalizer("test")
    public static String customFunction(String value) {
        return value.toUpperCase();
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
