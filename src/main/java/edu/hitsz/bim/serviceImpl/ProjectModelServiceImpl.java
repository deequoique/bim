package edu.hitsz.bim.serviceImpl;

import edu.hitsz.bim.entity.ProjectModel;
import edu.hitsz.bim.mappers.ProjectModelMapper;
import edu.hitsz.bim.service.ProjectModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Table for storing project models 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Service
public class ProjectModelServiceImpl extends ServiceImpl<ProjectModelMapper, ProjectModel> implements ProjectModelService {

}
