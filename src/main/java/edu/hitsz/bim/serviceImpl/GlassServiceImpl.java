package edu.hitsz.bim.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.common.BIMException;
import edu.hitsz.bim.common.ResponseEnum;
import edu.hitsz.bim.domain.dto.CreateGlassReq;
import edu.hitsz.bim.entity.Glass;
import edu.hitsz.bim.mappers.GlassMapper;
import edu.hitsz.bim.service.GlassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Table for storing glass information 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Service
public class GlassServiceImpl extends ServiceImpl<GlassMapper, Glass> implements GlassService {

    @Override
    public Boolean create(CreateGlassReq req) {
        Glass build = Glass.builder().projectId(Integer.valueOf(req.getProject_id()))
                .name(req.getName())
                .area(Double.valueOf(req.getArea()))
                .thickness(Double.valueOf(req.getThickness()))
                .strength(Double.valueOf(req.getStrength()))
                .configuration(req.getConfiguration())
                .manufacturer(req.getManufacturer())
                .build();
        return this.save(build);
    }

    @Override
    public List<Glass> getList(String projectId) {
        return this.list(new LambdaQueryWrapper<Glass>().eq(Glass::getProjectId, projectId));
    }

    @Override
    public Boolean delete(String id) {
        if (Objects.isNull(this.getById(id))) {
           throw BIMException.build(ResponseEnum.ERROR);
        }
        return this.removeById(id);
    }
}
