package kr.pe.advenoh.user.domain;

import jdk.nashorn.internal.ir.annotations.Ignore;
import kr.pe.advenoh.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Slf4j
@Transactional
class UserRepositoryTest extends SpringBootTestSupport {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Ignore
    void user_저장_조회() {
//        User user = new User("Frank", "testuser", "email@gmail.com", "password");

//        userRepository.save(user);

//        User result = userRepository.findByUsername(user.getUsername()).get();
//        assertThat(result.getUsername()).isEqualTo(user.getUsername());
    }
}