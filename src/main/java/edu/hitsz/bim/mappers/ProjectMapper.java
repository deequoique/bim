package edu.hitsz.bim.mappers;

import edu.hitsz.bim.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Table for storing project information Mapper 接口
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

}
