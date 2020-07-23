package kr.pe.advenoh.quote.util;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class SpringBootTestSupport {
	protected final String username = "testuser";
	protected final String email = "test@gmail.com";
	protected final String name = "Test User";
	protected final String password = "testpass";

	protected final String ROLE_USER = "USER";
}
