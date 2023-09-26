package net.n2oapp.framework.autotest.run;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AutoTestUtil {

    public static void checkChromeDriver() {
        String command = "google-chrome --product-version";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            line = reader.readLine();
        } catch (IOException ignored) {}

        if (line.contains("96.0.4664.110")) {
            WebDriverManager.chromedriver().setup();
        }
    }
}
