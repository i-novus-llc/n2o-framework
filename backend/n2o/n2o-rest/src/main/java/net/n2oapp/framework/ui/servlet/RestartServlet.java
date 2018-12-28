package net.n2oapp.framework.ui.servlet;

import net.n2oapp.framework.config.ConfigStarter;
import net.n2oapp.framework.mvc.n2o.N2oServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * Сервлет рестарта метаданных
 */
@WebServlet("/restart")
public class RestartServlet extends N2oServlet {

    @Override
    protected void safeDoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getBean(ConfigStarter.class).restart();
        response.sendRedirect("");//после успешного рестарта отправляем на home page
    }
}
