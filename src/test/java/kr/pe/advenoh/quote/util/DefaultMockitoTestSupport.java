package kr.pe.advenoh.quote.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class DefaultMockitoTestSupport {
    protected String username = "testUsername";
    protected String email = "test@gmail.com";
    protected String name = "Frank";
    protected String password = "testpass";
}
