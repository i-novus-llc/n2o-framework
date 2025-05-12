package net.n2oapp.framework.ui.controller.api;

import net.n2oapp.framework.api.exception.N2oException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

/**
 * Контроллер, предназначенный для кастомизации статики
 * (Реализация должна иметь аннотацию {@link org.springframework.stereotype.Controller})
 */
public abstract class StaticCustomizationController {

    @GetMapping(value = "favicon.ico", produces = "image/x-icon")
    private @ResponseBody byte[] favicon() {
        try {
            InputStream is = new ClassPathResource(faviconPath()).getInputStream();
            return StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            throw new N2oException("Wrong favicon path: " + faviconPath());
        }
    }

    protected abstract String faviconPath();
}