package net.n2oapp.framework.access.metadata.accesspoint.util;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.api.model.filter.N2oAccessFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Объединение разных фильтров по принципу OR
 */
public class FilterMerger {

    /**
     * Суммирует фильтры одного поля (объединение множеств значений).
     */
    public static List<N2oAccessFilter> merge(List<N2oAccessFilter> filters) {
        //сбор в карту по fieldId
        Map<String, List<N2oAccessFilter>> map = new LinkedHashMap<>();
        for (N2oAccessFilter filter : filters) {
            map.putIfAbsent(filter.getFieldId(), new ArrayList<>());
            map.get(filter.getFieldId()).add(filter);
        }

        //объединение фильтров одного поля
        List<N2oAccessFilter> res = new ArrayList<>();
        for (String fieldId : map.keySet()) {
            merge(map, fieldId);
            res.addAll(validate(map.get(fieldId)));
        }

        return res;

    }

    /**
     * Проверка, что значение у фильтра есть
     */
    private static List<N2oAccessFilter> validate(List<N2oAccessFilter> filters) {
        if (filters == null) return Collections.emptyList();
        Set<String> fieldIds = filters.stream()
                .filter(filter -> filter.getType().equals(FilterType.in) && (filter.getValues() == null
                        || filter.getValues().size() == 0))
                .map(N2oAccessFilter::getFieldId)
                .collect(Collectors.toSet());
        if (fieldIds.size() > 0)
            throw new IllegalArgumentException(String.format("Filter by field [%s] has type [in], but hasn't values", fieldIds.iterator().next()));
        return filters;
    }

    private static void merge(Map<String, List<N2oAccessFilter>> map, String fieldId) {
        int size = map.get(fieldId).size();
        List<N2oAccessFilter> result = new ArrayList<>();
        for (N2oAccessFilter filter : map.get(fieldId)) {
            addToResult(result, filter);
        }
        map.put(fieldId, result);
        List<N2oAccessFilter> fieldFilters = map.get(fieldId);
        if (fieldFilters.size() != size)
            merge(map, fieldId);
        else if (fieldFilters.get(0).getType().equals(FilterType.infinite))
            fieldFilters.clear();
    }

    private static void addToResult(List<N2oAccessFilter> result, N2oAccessFilter newFilter) {
        if (result.size() == 0)
            result.add(newFilter);
        else for (N2oAccessFilter filter : new ArrayList<>(result)) {
            result.remove(filter);
            result.addAll(merge(filter, newFilter));
        }
    }

    private static List<N2oAccessFilter> merge(N2oAccessFilter filter, N2oAccessFilter newFilter) {
        List<N2oAccessFilter> res = new ArrayList<>();

        // infinite
        if (filter.getType().equals(FilterType.infinite) || newFilter.getType().equals(FilterType.infinite)) {
            res.add(new N2oAccessFilter(filter.getFieldId(), FilterType.infinite));
        }

        // empty
        else if (filter.getType().equals(FilterType.empty) || newFilter.getType().equals(FilterType.empty)) {
            res.add(new N2oAccessFilter(filter.getFieldId(), FilterType.empty));
        }

        // in OR isNull
        else if (filter.getType().equals(FilterType.in) && newFilter.getType().equals(FilterType.isNull)) {
            InIsNull(filter, newFilter, res);
        } else if (filter.getType().equals(FilterType.isNull) && newFilter.getType().equals(FilterType.in)) {
            InIsNull(newFilter, filter, res);
        }

        // eq OR isNull
        else if (filter.getType().equals(FilterType.eq) && newFilter.getType().equals(FilterType.isNull)) {
            EqIsNull(filter, newFilter, res);
        } else if (filter.getType().equals(FilterType.isNull) && newFilter.getType().equals(FilterType.eq)) {
            EqIsNull(newFilter, filter, res);
        }

        // eq OR eq
        else if (filter.getType().equals(FilterType.eq) && newFilter.getType().equals(FilterType.eq)) {
            EqEq(filter, newFilter, res);
        }

        // eq OR in
        else if (filter.getType().equals(FilterType.eq) && newFilter.getType().equals(FilterType.in)) {
            EqIn(filter, newFilter, res);
        } else if (filter.getType().equals(FilterType.in) && newFilter.getType().equals(FilterType.eq)) {
            EqIn(newFilter, filter, res);
        }

        // eq OR overlap
        else if (filter.getType().equals(FilterType.eq) && newFilter.getType().equals(FilterType.overlaps)) {
            eqOverlap(filter, newFilter, res);
        } else if (filter.getType().equals(FilterType.overlaps) && newFilter.getType().equals(FilterType.eq)) {
            eqOverlap(newFilter, filter, res);
        }

        // notEq OR notEq
        else if (filter.getType().equals(FilterType.notEq) && newFilter.getType().equals(FilterType.notEq)) {
            NotEqNotEq(filter, newFilter, res);
        }

        // eq OR notEq
        else if (filter.getType().equals(FilterType.eq) && newFilter.getType().equals(FilterType.notEq)) {
            EqNotEq(filter, newFilter, res);
        } else if (filter.getType() == FilterType.notEq && newFilter.getType() == FilterType.eq) {
            EqNotEq(newFilter, filter, res);
        }

        // isNull OR isNotNull
        else if ((filter.getType().equals(FilterType.isNull) && newFilter.getType().equals(FilterType.isNotNull))
                || (filter.getType().equals(FilterType.isNotNull) && newFilter.getType().equals(FilterType.isNull))) {
            res.add(new N2oAccessFilter(filter.getFieldId(), FilterType.infinite));
        }

        // in OR in
        else if (filter.getType().equals(FilterType.in) && newFilter.getType().equals(FilterType.in)) {
            InIn(filter, newFilter, res);
        }

        // in OR overlap
        else if (filter.getType().equals(FilterType.in) && newFilter.getType().equals(FilterType.overlaps)) {
            inOverlap(filter, newFilter, res);
        } else if (filter.getType().equals(FilterType.overlaps) && newFilter.getType().equals(FilterType.in)) {
            inOverlap(newFilter, filter, res);
        }

        // overlap OR overlap
        else if (filter.getType().equals(FilterType.overlaps) && newFilter.getType().equals(FilterType.overlaps)) {
            overlapOverlap(filter, newFilter, res);
        }

        // notIn OR notIn
        else if (filter.getType().equals(FilterType.notIn) && newFilter.getType().equals(FilterType.notIn)) {
            NotInNotIn(filter, newFilter, res);
        }

        // notIn OR in
        else if (filter.getType().equals(FilterType.notIn) && newFilter.getType().equals(FilterType.in)) {
            NotInIn(filter, newFilter, res);
        } else if (filter.getType().equals(FilterType.in) && newFilter.getType().equals(FilterType.notIn)) {
            NotInIn(newFilter, filter, res);
        }

        // notIn OR eq
        else if (filter.getType().equals(FilterType.notIn) && newFilter.getType().equals(FilterType.eq)) {
            NotInEq(filter, newFilter, res);
        } else if (filter.getType().equals(FilterType.eq) && newFilter.getType().equals(FilterType.notIn)) {
            NotInEq(newFilter, filter, res);
        }

        // в противном случае
        else {
            res.add(filter);
            res.add(newFilter);
        }

        return res;
    }

    private static void InIsNull(N2oAccessFilter inFilter, N2oAccessFilter isNullFilter, List<N2oAccessFilter> res) {
        N2oAccessFilter inOrIsNullFilter = new N2oAccessFilter(inFilter.getFieldId(), FilterType.inOrIsNull);
        inOrIsNullFilter.setValues(inFilter.getValues());
        res.add(inOrIsNullFilter);
    }

    private static void EqIsNull(N2oAccessFilter eqFilter, N2oAccessFilter isNullFilter, List<N2oAccessFilter> res) {
        N2oAccessFilter eqOrIsNullFilter = new N2oAccessFilter(eqFilter.getFieldId(), FilterType.eqOrIsNull);
        eqOrIsNullFilter.setValue(eqFilter.getValue());
        res.add(eqOrIsNullFilter);
    }

    private static void NotInEq(N2oAccessFilter notInFilter, N2oAccessFilter eqFilter, List<N2oAccessFilter> res) {
        N2oAccessFilter inFilter = new N2oAccessFilter(eqFilter.getFieldId(), FilterType.in);
        inFilter.setValues(new ArrayList<>(Collections.singletonList(eqFilter.getValue())));
        NotInIn(notInFilter, inFilter, res);
    }

    private static void NotInIn(N2oAccessFilter notInFilter, N2oAccessFilter inFilter, List<N2oAccessFilter> res) {
        List<String> values = new ArrayList<>(notInFilter.getValues());
        inFilter.getValues().forEach(values::remove);
        N2oAccessFilter result;
        if (values.size() == 0) {
            result = new N2oAccessFilter(notInFilter.getFieldId(), FilterType.infinite);
        } else {
            result = new N2oAccessFilter(notInFilter.getFieldId(), FilterType.notIn);
            result.setValues(values);
        }
        res.add(result);

    }

    private static void EqIn(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        if (newFilter.getValues().contains(filter.getValue())) {
            res.add(newFilter);
        } else {
            N2oAccessFilter result = new N2oAccessFilter(filter.getFieldId(), FilterType.in);
            List<String> tmp = new ArrayList<>(newFilter.getValues());
            tmp.add(filter.getValue());
            result.setValues(tmp);
            res.add(result);
        }
    }

    private static void eqOverlap(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        if (newFilter.getValues().contains(filter.getValue())) {
            res.add(newFilter);
        } else {
            N2oAccessFilter result = new N2oAccessFilter(filter.getFieldId(), FilterType.overlaps);
            List<String> tmp = new ArrayList<>(newFilter.getValues());
            tmp.add(filter.getValue());
            result.setValues(tmp);
            res.add(result);
        }
    }

    private static void NotInNotIn(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        List<String> cross = filter.getValues()
                .stream()
                .filter(s -> newFilter.getValues().contains(s))
                .collect(Collectors.toList());
        if (cross.size() != 0) {
            N2oAccessFilter result = new N2oAccessFilter(filter.getFieldId(), FilterType.notIn);
            result.setValues(cross);
            res.add(result);
        } else res.add(new N2oAccessFilter(filter.getFieldId(), FilterType.infinite));
    }

    private static void InIn(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        List<String> resultList = new ArrayList<>(filter.getValues());
        newFilter.getValues().stream()
                .filter(s -> !resultList.contains(s))
                .forEach(resultList::add);
        N2oAccessFilter result = new N2oAccessFilter(filter.getFieldId(), FilterType.in);
        result.setValues(resultList);
        res.add(result);
    }

    private static void overlapOverlap(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        List<String> resultList = new ArrayList<>(filter.getValues());
        newFilter.getValues().stream()
                .filter(s -> !resultList.contains(s))
                .forEach(resultList::add);
        N2oAccessFilter result = new N2oAccessFilter(filter.getFieldId(), FilterType.overlaps);
        result.setValues(resultList);
        res.add(result);
    }

    private static void inOverlap(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        List<String> resultList = new ArrayList<>(filter.getValues());
        newFilter.getValues().stream()
                .filter(s -> !resultList.contains(s))
                .forEach(resultList::add);
        N2oAccessFilter result = new N2oAccessFilter(filter.getFieldId(), FilterType.overlaps);
        result.setValues(resultList);
        res.add(result);
    }

    private static void EqNotEq(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        if (!Objects.equals(filter.getValue(), newFilter.getValue())) {
            res.add(newFilter);
        } else res.add(new N2oAccessFilter(filter.getFieldId(), FilterType.infinite));
    }

    private static void NotEqNotEq(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        if (Objects.equals(filter.getValue(), newFilter.getValue())) {
            N2oAccessFilter result = new N2oAccessFilter(filter.getFieldId(), FilterType.notEq);
            result.setValue(filter.getValue());
            res.add(result);
        } else
            res.add(new N2oAccessFilter(filter.getFieldId(), FilterType.infinite));
    }


    private static void EqEq(N2oAccessFilter filter, N2oAccessFilter newFilter, List<N2oAccessFilter> res) {
        N2oAccessFilter result;
        if (Objects.equals(filter.getValue(), newFilter.getValue())) {
            result = new N2oAccessFilter(filter.getFieldId(), FilterType.eq);
            result.setValue(filter.getValue());
        } else {
            result = new N2oAccessFilter(filter.getFieldId(), FilterType.in);
            result.setValues(Arrays.asList(filter.getValue(), newFilter.getValue()));
        }
        res.add(result);
    }


}
