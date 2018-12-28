package net.n2oapp.framework.api.metadata.local.context;

/**
 * User: operhod
 * Date: 05.06.14
 * Time: 14:02
 */
public class MasterDetail {

    public enum Model {
        field, filter
    }


    private Model model = Model.field;

    private String master;
    private String detail;

    private String masterModel;
    private String detailModel;


    public MasterDetail() {
    }

    public MasterDetail(String master, String detail) {
        if (master == null || detail == null)
            throw new NullPointerException(String.format("master (%s) or detail (%s) are null", master, detail));
        setMaster(master);
        setDetail(detail);
    }


    public MasterDetail(String master, String detail, Model model) {
        this(master, detail);
        setModel(model);
    }


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setDetail(String detail) {
        this.detail = detail;
        if (detail.equals("id")) detailModel = "";
        if (detail.endsWith(".id")) {
            detailModel = detail.substring(0, detail.indexOf(".id") + 1);
        }
    }

    public void setMaster(String master) {
        this.master = master;
        if (master.equals("id")) masterModel = "";
        if (master.endsWith(".id")) {
            masterModel = master.substring(0, master.indexOf(".id") + 1);
        }
    }

    public boolean isModelReference() {
        return masterModel != null && detailModel != null;
    }

    public boolean isIdReference() {
        return "id".equals(detail);
    }

    public String getMaster() {
        return master;
    }

    public String getDetail() {
        return detail;
    }

    public String getMasterModel() {
        return masterModel;
    }

    public String getDetailModel() {
        return detailModel;
    }
}
