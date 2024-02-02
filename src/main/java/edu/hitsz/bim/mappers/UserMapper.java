package edu.hitsz.bim.mappers;

import edu.hitsz.bim.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Table for storing user information Mapper 接口
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:32:10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
