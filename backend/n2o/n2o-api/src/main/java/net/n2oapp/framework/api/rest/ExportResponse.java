package net.n2oapp.framework.api.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportResponse extends N2oResponse{
    String fileName;
    byte[] file;
    String format;
    String charset;
}
