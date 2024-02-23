package edu.hitsz.bim.serviceImpl;

import edu.hitsz.bim.domain.dto.CreateUserReq;
import edu.hitsz.bim.entity.User;
import edu.hitsz.bim.mappers.UserMapper;
import edu.hitsz.bim.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Table for storing user information 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:32:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Boolean create(CreateUserReq req) {
        User u = User.builder().name(req.getUsername())
                .password(new BCryptPasswordEncoder().encode(req.getPassword()))
                .build();
        return this.save(u);
    }
}
