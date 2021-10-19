package net.n2oapp.framework.api.metadata.meta;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbList extends ArrayList<Breadcrumb> {

    public BreadcrumbList() {
    }

    public BreadcrumbList(List<? extends Breadcrumb> objects) {
        if (objects != null) {
            objects.forEach(b -> this.add(new Breadcrumb(b)));
        }
    }
}