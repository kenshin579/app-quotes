package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.util.DefaultSpringTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Slf4j
@Transactional
class UserRepositoryTest extends DefaultSpringTestSupport {
    @Autowired
    private UserRepository userRepository;

    @Test
    void user_저장_조회() {
        User user = new User("Frank", "kenshin579", "email@gmail.com", "password");

        userRepository.save(user);

//        User result = userRepository.findByUsername(user.getUsername()).get();
//        assertThat(result.getUsername()).isEqualTo(user.getUsername());
    }
}