package edu.hitsz.bim.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.domain.dto.CreateIndicatorReq;
import edu.hitsz.bim.entity.Indicator;
import edu.hitsz.bim.mappers.IndicatorMapper;
import edu.hitsz.bim.service.IndicatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * Table for storing various indicators related to projects 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-04-25 08:59:42
 */
@Service
public class IndicatorServiceImpl extends ServiceImpl<IndicatorMapper, Indicator> implements IndicatorService {

    @Override
    public Boolean delete(String s) {
        return this.removeById(s);
    }

    @Override
    public List<Indicator> getList(String projectId) {
        return this.list(new LambdaQueryWrapper<Indicator>().eq(Indicator::getProjectId, projectId));
    }

    @Override
    public Boolean create(CreateIndicatorReq createIndicatorReq) {
        Indicator build = Indicator.builder()
                .name(createIndicatorReq.getName())
                .projectId(createIndicatorReq.getProjectId())
                .weight(createIndicatorReq.getWeight()).build();
        return this.save(build);
    }
}
