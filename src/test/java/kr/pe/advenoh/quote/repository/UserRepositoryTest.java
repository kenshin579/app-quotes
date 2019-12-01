package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void user_저장_조회() {
        User user = new User("Frank", "kenshin579", "email@gmail.com", "password");

        userRepository.save(user);

        User result = userRepository.findByUsername(user.getUsername());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
    }
}