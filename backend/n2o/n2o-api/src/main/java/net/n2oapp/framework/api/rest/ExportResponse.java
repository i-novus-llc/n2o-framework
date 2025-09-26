package net.n2oapp.framework.api.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportResponse extends N2oResponse {
    private byte[] file;
    private String contentType;
    private String contentDisposition;
    private int contentLength;
    private String characterEncoding;
}
