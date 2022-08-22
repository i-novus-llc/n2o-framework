package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Утилитарные методы для работы с URL
 */
public abstract class RouteUtil {

    /**
     * Возврат на один уровень назад в маршруте
     *
     * @param route Маршрут
     * @return Маршрут на один уровень назад
     */
    public static String parent(String route) {
        return route.startsWith("/") ? ".." + route : "../" + route;
    }

    /**
     * Нормализация URL.
     * Убирает двойные слешы. Убирает слеш в конце. Ставит слеш в начало.
     *
     * @param url Адрес URL
     * @return Нормализованный адрес URL
     */
    public static String normalize(String url) {
        url = url.replaceAll("[/]+", "/");
        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        url = !url.startsWith("/") && !url.startsWith("../") ? "/" + url : url;
        return url;
    }

    /**
     * Нормализация параметра URL.
     * Заменяет все не буквенно цифровые символы на подчеркивание
     *
     * @param field Поле выборки или формы
     * @return Нормализованный параметр
     */
    public static String normalizeParam(String field) {
        return field.replaceAll("\\W", "_").replaceAll("_{1,}", "_");
    }

    /**
     * Изменение исходного url. Добавляет в конец параметры запроса переданные в queryParams
     * Если это константа, то она сразу попадает в url, иначе добавляется в виде плейсхолдера с :
     *
     * @param route        url
     * @param queryMapping параметры запроса для добавления
     * @return дополненный url
     */
    public static String addQueryParams(String route, Map<String, ModelLink> queryMapping) {
        if (queryMapping == null || queryMapping.isEmpty())
            return route;
        StringBuilder params = new StringBuilder();
        queryMapping.keySet().stream().forEach(k -> {
            ModelLink link = queryMapping.get(k);
            params.append(params.length() > 0 ? "&" : "")
                    .append(link.getParam() == null ? k : link.getParam()).append("=")
                    .append(link.isConst() ? link.getValue() : ":" + k);
        });
        if (params.length() == 0) {
            return route;
        }
        if (route.contains("?")) {
            return String.format("%s&%s", route, params.toString());
        } else {
            return String.format("%s?%s", route, params.toString());
        }
    }


    /**
     * Получение всех параметров url
     *
     * @param url
     * @return Список параметров
     */
    public static List<String> getParams(String url) {
        List<String> result = new ArrayList<>();
        String[] urlParts = url.split("\\?");
        String[] splitUrl = urlParts[0].split("/");
        for (int i = 0; i < splitUrl.length && i < splitUrl.length; i++) {
            String token = splitUrl[i];
            if (token.startsWith(":")) {
                result.add(token.substring(1));
            }
        }
        if (urlParts.length == 2) {
            String[] splitParam = urlParts[1].split("&");
            for (int i = 0; i < splitParam.length && i < splitParam.length; i++) {
                if (splitParam[i].contains(":")) {
                    result.add(splitParam[i].substring(splitParam[i].indexOf(":") + 1));
                }
            }
        }
        return result;
    }

    /**
     * Получение всех path параметров из url
     * /:a/test/:b?id=:c -> [a, b]
     * @param url
     * @return Список path параметров
     */
    public static List<String> getPathParams(String url) {
        List<String> pathParams = new ArrayList<>();
        String[] urlParts = url.split("\\?");
        Matcher matcher = Pattern.compile("(:\\w+)").matcher(urlParts[0]);
        while (matcher.find())
            pathParams.add(matcher.group().substring(1));
        return pathParams;
    }


    /**
     * Парсинг части url с query параметрами
     * name=Ivan&age=4 превращает в ["name":"Ivan", "age":4]
     *
     * @param url
     * @return мапа с параметрами и их значениями
     */
    public static Map<String, String[]> parseQueryParams(String url) {
        if (url == null || !(url.contains("=") || url.contains("&")))
            return null;

        HashMap<String, List<String>> result = new HashMap();
        String[] params = url.split("&");
        for (String param : params) {
            String[] paramValue = param.split("=");
            result.putIfAbsent(paramValue[0], new ArrayList<>());
            result.get(paramValue[0]).add(paramValue[1]);
        }

        return result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toArray(String[]::new)));
    }


    /**
     * Конвертация URL в идентификатор.
     * Заменяет все "/" на подчеркивание, параметры в пути пропускает
     *
     * @param url Адрес URL
     * @return Идентификатор
     */
    public static String convertPathToId(String url) {
        if (url == null)
            return null;
        url = normalize(url);
        if (url.startsWith("/"))
            url = url.substring(1);
        if (url.isEmpty())
            return "_";
        return url.replaceAll("/:\\w+/", "/")
                .replaceAll("/:\\w+$", "")
                .replace("/", "_")
                .replace(":", "");
    }

    /**
     * Заменить ссылки в маршруте на значения
     *
     * @param url  Маршрут
     * @param data Значения
     * @return Маршрут без ссылок
     */
    public static String resolveUrlParams(String url, Map<String, Object> data) {
        return resolveUrlParams(url, data, null, null);
    }

    /**
     * Заменить ссылки в маршруте на значения, кроме исключений
     *
     * @param url    Маршрут
     * @param data   Значения
     * @param except Исключения
     * @return Маршрут без ссылок
     */
    public static String resolveParams(String url, Map<String, Object> data, Set<String> except) {
        Set<String> pathParams = new HashSet<>(getParams(url));
        if (except != null) {
            pathParams.removeAll(except);
        }
        for (String param : pathParams) {
            Object value = data.get(param);
            if (value != null)
                url = url.replace(":" + param, value.toString());
        }
        return url;
    }


    /**
     * Заменить ссылки в маршруте на значения, кроме исключений
     *
     * @param url       Маршрут
     * @param data      Значения
     * @param whiteList Параметры для замены (если null, значит заменяем все найденные)
     * @param blackList Исключения
     * @return Маршрут без ссылок
     */
    public static String resolveUrlParams(String url, Map<String, Object> data, Set<String> whiteList, Set<String> blackList) {
        if (data == null) {
            return url;
        }
        Set<String> pathParams = new HashSet<>(getParams(url));
        if (whiteList != null) {
            pathParams = pathParams.stream().filter(s -> whiteList.contains(s)).collect(Collectors.toSet());
        }
        if (blackList != null) {
            pathParams.removeAll(blackList);
        }
        for (String param : pathParams) {
            Object value = data.get(param);
            if (value != null)
                url = url.replace(":" + param, value.toString());
        }
        return url;
    }

    /**
     * Адресуется ли URL внутри приложения?
     *
     * @param url Адрес URL
     * @return true внутри, false снаружи
     */
    public static boolean isApplicationUrl(String url) {
        // target self or newWindow
        return !url.startsWith("http") && !url.startsWith("//");
    }

    /**
     * Преобразование относительного маршрутав абсолютный
     *
     * @param baseRoute     Базовый маршрут
     * @param relativeRoute Относительный маршрут
     * @return Абсолютный маршрут
     */
    public static String absolute(String relativeRoute, String baseRoute) {
        if (!isApplicationUrl(relativeRoute))
            return relativeRoute;
        if (relativeRoute.startsWith("/"))
            return relativeRoute;
        return join(baseRoute, relativeRoute);
    }

    /**
     * Соединение родитеслького маршрута с дочерним
     *
     * @param parentRoute Родительский маршрут
     * @param childRoute  Отнсительный маршрут
     * @return Соединенный маршрут
     */
    public static String join(String parentRoute, String childRoute) {
        if (!isApplicationUrl(childRoute))
            return childRoute;
        if (parentRoute == null)
            return normalize(childRoute);
        int k = 0;
        String child = childRoute;
        while (child.startsWith("../")) {
            child = child.substring(child.indexOf("../") + 3);
            k++;
        }
        child = "/" + child;
        StringBuilder result = new StringBuilder();
        if (k > 0) {
            String[] parent = parentRoute.split("/");
            if (parent.length <= k) {
                throw new IncorrectRouteException(childRoute);
            }
            for (int i = 0; i < parent.length - k; i++) {
                result.append("/").append(parent[i]);
            }
            result.append(child);
        } else {
            result.append(parentRoute).append(normalize(childRoute));
        }
        return normalize(result.toString());
    }

    /**
     * Получает часть адреса url до параметров запроса
     * @param url Адресс
     * @return Часть адреса url
     */
    public static String parsePath(String url) {
        return url.contains("?") ? url.substring(0, url.indexOf('?')) : url;
    }

    /**
     * Получает часть адреса url отвечающего за ппараметры запроса
     * @param url Адресс
     * @return Часть адреса url
     */
    public static String parseQuery(String url) {
        return url.contains("?") ? url.substring(url.indexOf('?') + 1) : null;
    }
}
