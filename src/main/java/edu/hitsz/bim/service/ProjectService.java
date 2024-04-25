package edu.hitsz.bim.service;

import edu.hitsz.bim.domain.dto.CreateProjectReq;
import edu.hitsz.bim.domain.vo.ProjectVO;
import edu.hitsz.bim.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * Table for storing project information 服务类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
public interface ProjectService extends IService<Project> {

    List<Project> findByUser(String userId);

    String create(CreateProjectReq req);

    ProjectVO details(String id);

    Boolean delete(String id);

    String upload(MultipartFile file);
}
