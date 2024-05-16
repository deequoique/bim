package edu.hitsz.bim.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.common.BIMException;
import edu.hitsz.bim.common.ResponseEnum;
import edu.hitsz.bim.domain.dto.CreateIndicatorReq;
import edu.hitsz.bim.domain.dto.ValueIndicatorReq;
import edu.hitsz.bim.entity.Indicator;
import edu.hitsz.bim.entity.Project;
import edu.hitsz.bim.mappers.IndicatorMapper;
import edu.hitsz.bim.service.IndicatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hitsz.bim.service.ProjectService;
import edu.hitsz.bim.utils.BigDecimalUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

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
    private static final String REPORT_FOLDER = "/usr/local/bim/report/";

    @Resource
    private ProjectService projectService;
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
                .projectId(createIndicatorReq.getProject_id())
                .weight(createIndicatorReq.getWeight()).build();
        return this.save(build);
    }

    @Override
    public String upload(MultipartFile file, String projectId) {
        if (file.isEmpty()) {
            throw BIMException.build(ResponseEnum.NOT_FOUND);
        }
        try {
            // 保存文件到服务器
            byte[] bytes = file.getBytes();
            String filename = DateUtil.current() + file.getOriginalFilename();
            Path path = Paths.get(REPORT_FOLDER + filename);
            Files.write(path, bytes);
            Project project = projectService.getById(projectId);
            if (Objects.isNull(project)) {
                log.warn("projectID:" + projectId);
                throw BIMException.build(ResponseEnum.DB_ERROR);
            }
            project.setReport(filename);
            projectService.updateById(project);
            return filename;
        } catch (Exception e) {
            throw BIMException.build(ResponseEnum.ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean valueList(List<ValueIndicatorReq> reqs) {
        Integer projectId = this.getById(reqs.get(0).getId()).getProjectId();
        List<Indicator> indicatorList = this.getList(String.valueOf(projectId));
        String totalWeight = "";
        for (Indicator i : indicatorList) {
            BigDecimal add = BigDecimalUtils.add(totalWeight, i.getWeight());
            totalWeight = add.toString();
        }
        String totalValue = "";
        for (ValueIndicatorReq r: reqs) {
            Indicator indicator = this.getById(r.getId());
            indicator.setValue(r.getValue());
            BigDecimal weight = BigDecimalUtils.divide(indicator.getWeight(), totalWeight);
            BigDecimal wValue = BigDecimalUtils.multiply(weight.toString(), r.getValue());
            BigDecimal addValue = BigDecimalUtils.add(totalValue, wValue.toString());
            totalValue = addValue.toString();
            this.updateById(indicator);
        }
        Project project = projectService.getById(projectId);
        project.setResult(totalValue);
        projectService.updateById(project);
        return true;
    }
}
