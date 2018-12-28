package net.n2oapp.framework.api.bean;


import java.util.*;

/**
 * User: operhod
 * Date: 23.11.13
 * Time: 17:08
 */
public class BeansSorting {


    @SuppressWarnings("unchecked")
    public static <T extends LocatedBean> List<T> sort(List<T> list, List<LocatedBeanPack<T>> packs) throws BeansOrderException {
        List<LocatedBean> fullList = new ArrayList<>();
        fullList.addAll(list);
        fullList.addAll(packs);
        fullList = sort(fullList);
        List<T> result = new ArrayList<>();
        for (LocatedBean orderedBean : fullList) {
            if (orderedBean instanceof LocatedBeanPack)
                result.addAll(((LocatedBeanPack) orderedBean).getPack());
            else result.add((T) orderedBean);
        }
        return result;
    }


    public static <T extends LocatedBean> List<T> sort(List<T> inCollection) throws BeansOrderException {
        Map<T, List<T>> orderMap = new HashMap<>();
        List<T> res = new ArrayList<>();
        List<T> stack = new ArrayList<>();

        for (T n2oModule : inCollection) {
            List<T> after = getAfterBeans(n2oModule);
            List<T> before = getBeforeBeans(n2oModule);

            put(n2oModule, after, orderMap);
            for (T next : before) {
                put(next, Arrays.asList(n2oModule), orderMap);
            }
        }


        LinkedList<Map.Entry<T, List<T>>> tmp1 = new LinkedList<>();
        LinkedList<Map.Entry<T, List<T>>> tmp2 = new LinkedList<>();
        for (Map.Entry<T, List<T>> entry : orderMap.entrySet()) {
            if (entry.getKey().isBeforeAll()) tmp1.addFirst(entry);
            else if (entry.getKey().isAfterAll()) tmp2.add(entry);
            else tmp1.addLast(entry);
        }
        orderMap = new LinkedHashMap<>();
        for (Map.Entry<T, List<T>> entry : tmp1) {
            orderMap.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<T, List<T>> entry : tmp2) {
            orderMap.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<T, List<T>> entry : orderMap.entrySet()) {
            addToRes(entry.getKey(), orderMap, stack, res);
        }
        return res;
    }


    @SuppressWarnings("unchecked")
    private static <T extends LocatedBean> List<T> getBeforeBeans(T n2oModule) {
        if (n2oModule.getNextBeans() == null)
            return Collections.emptyList();
        else
            return (List<T>) Arrays.asList(n2oModule.getNextBeans());
    }

    @SuppressWarnings("unchecked")
    private static <T extends LocatedBean> List<T> getAfterBeans(T n2oModule) {
        if (n2oModule.getPrevBeans() == null)
            return Collections.emptyList();
        else
            return (List<T>) Arrays.asList(n2oModule.getPrevBeans());
    }

    private static <T extends LocatedBean> void addToRes(T key, Map<T, List<T>> orderMap, List<T> stack,
                                                         List<T> res) throws BeansOrderException {
        if (stack.contains(key)) throw new BeansOrderException();
        stack.add(key);
        for (T module : orderMap.get(key)) {
            if (!res.contains(key)) addToRes(module, orderMap, stack, res);
        }
        if (!res.contains(key)) res.add(key);
        stack.remove(key);
    }


    private static <T extends LocatedBean> void put(T n2oModule, List<T> list, Map<T, List<T>> orderMap) {
        List<T> res = orderMap.get(n2oModule);
        if (res == null) {
            res = new ArrayList<>(list);
        } else {
            res.addAll(list);
        }
        orderMap.put(n2oModule, res);
    }


}
