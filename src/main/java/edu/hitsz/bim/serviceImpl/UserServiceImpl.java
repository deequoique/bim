package edu.hitsz.bim.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.common.BIMException;
import edu.hitsz.bim.common.ResponseEnum;
import edu.hitsz.bim.domain.dto.CreateUserReq;
import edu.hitsz.bim.entity.User;
import edu.hitsz.bim.mappers.UserMapper;
import edu.hitsz.bim.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

        if (Objects.isNull(req.getPassword())) {
            throw BIMException.build(ResponseEnum.ERROR);
        }

        User one = this.getOne(new LambdaQueryWrapper<User>().eq(User::getName, req.getUsername()));
        if (Objects.nonNull(one)) {
            throw BIMException.build(ResponseEnum.DUPLICATE);
        }
        User u = User.builder().name(req.getUsername())
                .password(new BCryptPasswordEncoder().encode(req.getPassword()))
                .build();
        return this.save(u);
    }
}
