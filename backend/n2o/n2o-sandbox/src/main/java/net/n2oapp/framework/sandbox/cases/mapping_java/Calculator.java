package net.n2oapp.framework.sandbox.cases.mapping_java;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Calculator {

    public static long sum(Long a, Long b) {
        return a + b;
    }
}
