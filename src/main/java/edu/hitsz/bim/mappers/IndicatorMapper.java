package edu.hitsz.bim.mappers;

import edu.hitsz.bim.entity.Indicator;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Table for storing various indicators related to projects Mapper 接口
 * </p>
 *
 * @author lane
 * @since 2024-04-25 08:59:42
 */
@Mapper
public interface IndicatorMapper extends BaseMapper<Indicator> {

}
