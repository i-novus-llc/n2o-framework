package net.n2oapp.framework.api.metadata.global.view.widget;

public class N2oHtmlWidget extends N2oWidget {
    private String url;
    private boolean dummyObject;

    public N2oHtmlWidget() {
        dummyObject = true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDummyObject() {
        return dummyObject;
    }

    @Override
    public void setObjectId(String objectId) {
        if(objectId != null) {
            super.setObjectId(objectId);
            dummyObject = false;
        }
    }
}
