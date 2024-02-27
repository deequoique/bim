package edu.hitsz.bim.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.domain.dto.CreateProjectReq;
import edu.hitsz.bim.domain.vo.ProjectVO;
import edu.hitsz.bim.entity.Project;
import edu.hitsz.bim.entity.ProjectModel;
import edu.hitsz.bim.mappers.ProjectMapper;
import edu.hitsz.bim.service.ProjectModelService;
import edu.hitsz.bim.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * Table for storing project information 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private ProjectModelService projectModelService;
    @Override
    public List<Project> findByUser(String userId) {
        return this.list(new LambdaQueryWrapper<Project>().eq(Project::getUserId, userId));
    }

    @Override
    public Boolean create(CreateProjectReq req) {
        Project build = Project.builder().name(req.getName())
                .userId(req.getUser_id())
                .build();
        return this.save(build);
    }

    @Override
    public ProjectVO details(String id) {
        Project project = this.getById(id);
        ProjectModel model = projectModelService.getOne(new LambdaQueryWrapper<ProjectModel>()
                .eq(ProjectModel::getProjectId, id));
        return ProjectVO.builder()
                .id(project.getId())
                .name(project.getName())
                .security(project.getSecurity())
                .model(model.getModel())
                .build();
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }
}
