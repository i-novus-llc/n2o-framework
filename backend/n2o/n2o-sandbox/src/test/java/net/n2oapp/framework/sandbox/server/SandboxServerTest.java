package net.n2oapp.framework.sandbox.server;

import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxServerTest {

    @Autowired
    private ViewController viewController;

    @Test
    public void testGetConfig() {
        viewController.getConfig("qwe");
    }
}