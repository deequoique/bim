package edu.hitsz.bim.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.common.BIMException;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.common.ResponseEnum;
import edu.hitsz.bim.domain.dto.CreateProjectReq;
import edu.hitsz.bim.domain.vo.ProjectVO;
import edu.hitsz.bim.entity.Project;
import edu.hitsz.bim.entity.ProjectModel;
import edu.hitsz.bim.mappers.ProjectMapper;
import edu.hitsz.bim.service.ProjectModelService;
import edu.hitsz.bim.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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

    private static final String TARGET_FOLDER = "/usr/local/bim/directory/";
    @Resource
    private ProjectModelService projectModelService;

    @Override
    public List<Project> findByUser(String userId) {
        return this.list(new LambdaQueryWrapper<Project>().eq(Project::getUserId, userId));
    }

    @Override
    public String create(CreateProjectReq req) {
        Project build = Project.builder().name(req.getName())
                .userId(req.getUser_id())
                .build();
        this.save(build);
        return String.valueOf(build.getId());
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

    @Override
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw BIMException.build(ResponseEnum.NOT_FOUND);
        }

        try {
            // 保存文件到服务器
            byte[] bytes = file.getBytes();
            String filename = DateUtil.current() + file.getOriginalFilename();
            Path path = Paths.get(TARGET_FOLDER + filename);
            Files.write(path, bytes);
            return filename;
        } catch (Exception e) {
            throw BIMException.build(ResponseEnum.ERROR);
        }
    }
}
