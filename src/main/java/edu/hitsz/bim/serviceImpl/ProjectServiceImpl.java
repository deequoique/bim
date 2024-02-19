package edu.hitsz.bim.serviceImpl;

import edu.hitsz.bim.domain.dto.CreateProjectReq;
import edu.hitsz.bim.domain.vo.ProjectVO;
import edu.hitsz.bim.entity.Project;
import edu.hitsz.bim.mappers.ProjectMapper;
import edu.hitsz.bim.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public List<Project> findByUser(String userId) {
        return null;
    }

    @Override
    public Boolean create(CreateProjectReq req) {
        return null;
    }

    @Override
    public ProjectVO details(String id) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }
}
