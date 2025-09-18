package net.n2oapp.framework.sandbox.templates;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FileCheckerResponse {
    private boolean success;
    private List<String> errors;
}
