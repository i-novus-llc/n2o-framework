package net.n2oapp.framework.sandbox.cases.fileupload;

public interface StorageController {

    String LIST_PREFIX = "/list";

    ListResponse getList();

    interface ListResponse{
    }
}
