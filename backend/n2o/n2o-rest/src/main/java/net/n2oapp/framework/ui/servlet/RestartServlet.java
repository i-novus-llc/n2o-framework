package net.n2oapp.framework.ui.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.config.ConfigStarter;
import net.n2oapp.framework.mvc.n2o.N2oServlet;

import java.io.IOException;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * Сервлет рестарта метаданных
 */
@WebServlet("/restart")
public class RestartServlet extends N2oServlet {

    @Override
    protected void safeDoGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getBean(ConfigStarter.class).restart();
        response.sendRedirect("");//после успешного рестарта отправляем на home page
    }
}
