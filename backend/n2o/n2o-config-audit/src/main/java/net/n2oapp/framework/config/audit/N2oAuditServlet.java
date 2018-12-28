package net.n2oapp.framework.config.audit;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.config.ConfigStarter;
import org.springframework.context.support.MessageSourceAccessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static net.n2oapp.context.StaticSpringContext.getBean;
import static net.n2oapp.framework.config.audit.git.util.N2oGitUtil.isRepoExists;

/**
 * Сервлет для инициализации аудита и проверки его состояния
 */
public class N2oAuditServlet extends HttpServlet {
    private N2oConfigAudit auditGit;
    private MessageSourceAccessor messageSourceAccessor;
    private ConfigStarter configStarter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.auditGit = getBean(N2oConfigAudit.class);
        this.messageSourceAccessor = getBean(MessageSourceAccessor.class);
        this.configStarter = getBean(ConfigStarter.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = prepareOut(resp);
        String command = req.getPathInfo() == null ? null : req.getPathInfo().replace("/", "");
        if (command == null) {
            out.println(messageSourceAccessor.getMessage("n2o.audit.wrongCommand"));
            return;
        }
        switch (command) {
            case "merge": {
                if (!checkAuditEnabled(out))
                    return;
                if (!checkRepoInit(out)) {
                    return;
                } else {
                    try {
                        String branchName = req.getParameter("branchName");
                        Boolean isOrigin = Boolean.valueOf(req.getParameter("isOrigin"));
                        if (branchName == null || isOrigin == null) {
                            out.println(messageSourceAccessor.getMessage("n2o.audit.paramsNotFound"));
                            return;
                        }
                        auditGit.merge(branchName,isOrigin);
                        out.println(messageSourceAccessor.getMessage("n2o.audit.mergedSuccessfully"));
                    } catch (Exception e) {
                        out.println(messageSourceAccessor.getMessage("n2o.audit.mergeFailed"));
                        e.printStackTrace(out);
                    }
                }
                break;
            }
            case "updateSystem": {
                if (!checkAuditEnabled(out))
                    return;
                try {
                    auditGit.updateSystem();
                    out.println(messageSourceAccessor.getMessage("n2o.audit.updatedSuccessfully"));
                } catch (Exception e) {
                    out.println(messageSourceAccessor.getMessage("n2o.audit.updateFailed"));
                    e.printStackTrace(out);
                }
                break;
            }
            case "pull": {
                if (!checkAuditEnabled(out))
                    return;
                try {
                    auditGit.pull();
                    out.println(messageSourceAccessor.getMessage("n2o.audit.pullSuccessfully"));
                } catch (Exception e) {
                    out.println(messageSourceAccessor.getMessage("n2o.audit.pullFailed"));
                    e.printStackTrace(out);
                }
                break;
            }
            case "push": {
                if (!checkAuditEnabled(out))
                    return;
                try {
                    auditGit.push();
                    out.println(messageSourceAccessor.getMessage("n2o.audit.pushSuccessfully"));
                } catch (Exception e) {
                    out.println(messageSourceAccessor.getMessage("n2o.audit.pushFailed"));
                    e.printStackTrace(out);
                }
                break;
            }
            case "status": {
                if (!checkAuditEnabled(out))
                    return;
                if (!checkRepoInit(out))
                    return;
                out.println(auditGit.checkStatus());
                break;
            }
            default: {
                out.println(messageSourceAccessor.getMessage("n2o.audit.wrongCommand"));
                break;
            }
        }
    }

    private boolean checkRepoInit(PrintWriter out) {
        if (!isRepoExists(configStarter.getConfigPath())) {
            out.println(messageSourceAccessor.getMessage("n2o.audit.auditIsNotStarted"));
            return false;
        }
        return true;
    }

    private PrintWriter prepareOut(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        return out;
    }

    private boolean checkAuditEnabled(PrintWriter out) {
        if (auditGit.isEnabled()) {
            return true;
        }
        out.println(messageSourceAccessor.getMessage("n2o.audit.auditIsNotEnabled"));
        return false;
    }
}
