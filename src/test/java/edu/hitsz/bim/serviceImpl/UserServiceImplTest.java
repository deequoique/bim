package edu.hitsz.bim.serviceImpl;

import edu.hitsz.bim.entity.User;
import edu.hitsz.bim.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lane
 * @description
 * @since 2024/2/23 16:10
 */
@SpringBootTest
class UserServiceImplTest {

    @Resource
    private UserService userService;
    @Test
    void create() {
        User u = User.builder().name("admin")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .build();
        userService.save(u);
    }
}