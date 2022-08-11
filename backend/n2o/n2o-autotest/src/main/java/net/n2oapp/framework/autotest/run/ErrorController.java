package net.n2oapp.framework.autotest.run;

import net.n2oapp.framework.api.exception.N2oException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер http ошибок для автотестов
 */

@RestController
public class ErrorController {

    public N2oException sendBadGateway() {
        N2oException n2oException = new N2oException();
        n2oException.setHttpStatus(HttpStatus.BAD_GATEWAY.value());
        throw n2oException;
    }

    public N2oException sendForbidden() {
        N2oException n2oException = new N2oException();
        n2oException.setHttpStatus(HttpStatus.FORBIDDEN.value());
        throw n2oException;
    }

    public N2oException sendNotFound() {
        N2oException n2oException = new N2oException();
        n2oException.setHttpStatus(HttpStatus.NOT_FOUND.value());
        throw n2oException;
    }

    public N2oException sendInternalServerError() {
        N2oException n2oException = new N2oException();
        n2oException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        throw n2oException;
    }
}
