package net.n2oapp.framework.api.metadata.global.view.region;

/**
 * Регион с возможностью добавления в URL информации об активном элементе
 */
public interface RoutableRegion {

    Boolean getRoutable();

    String getActiveParam();
}
