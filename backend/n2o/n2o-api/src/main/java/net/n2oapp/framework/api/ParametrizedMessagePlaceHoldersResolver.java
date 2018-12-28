package net.n2oapp.framework.api;

/**
 * User: operehod
 * Date: 02.12.2014
 * Time: 15:22
 * @see StringUtils#resolveLinks(String, Object)
 */
@Deprecated
public class ParametrizedMessagePlaceHoldersResolver {

    private static PlaceHoldersResolver instance = new PlaceHoldersResolver("{", "}");

    public static PlaceHoldersResolver getInstance() {
        return instance;
    }

}
