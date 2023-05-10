package net.n2oapp.framework.api.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportResponse extends N2oResponse{
    byte[] file;

    String contentType;

    String contentDisposition;

    int contentLength;

    String characterEncoding;
}
