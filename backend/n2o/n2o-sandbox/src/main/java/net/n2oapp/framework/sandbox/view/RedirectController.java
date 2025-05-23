package net.n2oapp.framework.sandbox.view;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

    @Value("${n2o.sandbox.url}")
    private String sandboxUrl;

    @GetMapping("/")
    public RedirectView redirect(HttpServletRequest request) {
        return new RedirectView(sandboxUrl + "?stand=" + request.getRequestURL().toString());
    }
}
