package kr.pe.advenoh.spring.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        log.error("Responding with unauthorized error. Message - {}", e.getMessage());

//        if(e instanceof BadCredentialsException) {
//            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            Result invalidUserNameOrPassword = ControllerUtils.buildErrorResult(101, "Incorrect account or password");
//            httpServletResponse.getOutputStream().write(new ObjectMapper()
//                    .writeValueAsBytes(invalidUserNameOrPassword));
//        } else {
//            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//                    "Unauthorized access");
//        }

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}
