package net.n2oapp.framework.ui.servlet;

import net.n2oapp.framework.mvc.n2o.N2oServlet;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("")
public class SPAServlet extends N2oServlet {

    private Resource index = new ClassPathResource("/META-INF/resources/index.html");

    @Override
    protected void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        IOUtils.copy(index.getInputStream(), resp.getWriter());
    }
}
