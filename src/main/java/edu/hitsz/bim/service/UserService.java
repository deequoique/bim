package edu.hitsz.bim.service;

import edu.hitsz.bim.domain.dto.CreateUserReq;
import edu.hitsz.bim.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * Table for storing user information 服务类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:32:10
 */
public interface UserService extends IService<User> {

    Boolean create(CreateUserReq req);
}
