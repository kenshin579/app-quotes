package kr.pe.advenoh.quote.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public abstract class DefaultSpringTestSupport {
    protected String username = "testUsername";
    protected String email = "test@gmail.com";
    protected String name = "Frank";
    protected String password = "testpass";
}
