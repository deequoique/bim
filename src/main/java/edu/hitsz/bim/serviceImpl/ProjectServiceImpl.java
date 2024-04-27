package edu.hitsz.bim.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.common.BIMException;
import edu.hitsz.bim.common.ResponseEnum;
import edu.hitsz.bim.domain.dto.CreateProjectReq;
import edu.hitsz.bim.domain.dto.SecurityReq;
import edu.hitsz.bim.domain.vo.ProjectVO;
import edu.hitsz.bim.entity.Project;
import edu.hitsz.bim.entity.ProjectModel;
import edu.hitsz.bim.mappers.ProjectMapper;
import edu.hitsz.bim.service.ProjectModelService;
import edu.hitsz.bim.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Table for storing project information 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    private static final String REPORT_FOLDER = "/usr/local/bim/report/";

    private static final String DIRECTORY_FOLDER = "/usr/local/bim/directory/";
    @jakarta.annotation.Resource
    private ProjectModelService projectModelService;

    @Override
    public List<Project> findByUser(String userId) {
        return this.list(new LambdaQueryWrapper<Project>().eq(Project::getUserId, userId));
    }

    @Override
    public String create(CreateProjectReq req) {
        Project build = Project.builder().name(req.getName())
                .userId(req.getUser_id())
                .location(req.getLocation())
                .coordinate(req.getCoordinate())
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
                .location(project.getLocation())
                .coordinate(project.getCoordinate())
                .model(model.getModel())
                .build();
    }

    @Override
    public Boolean delete(String id) {
        Project project = this.getById(id);
        String report = project.getReport();
        if (!report.isEmpty()) {
            deleteReport(report);
        }
        String directory = project.getFile();
        if (!directory.isEmpty()) {
            deleteDirectory(directory);
        }
        return this.removeById(id);
    }


    @Override
    public void deleteReport(String name) {
        Path path = Paths.get(REPORT_FOLDER + name);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw BIMException.build(ResponseEnum.ERROR);
        }
    }

    @Override
    public void deleteDirectory(String name) {
        Path path = Paths.get(DIRECTORY_FOLDER + name);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            Path path = Paths.get(DIRECTORY_FOLDER + filename);
            Files.write(path, bytes);
            return filename;
        } catch (Exception e) {
            throw BIMException.build(ResponseEnum.ERROR);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadReport(HttpServletResponse response, String projectId) {
        Path path = Paths.get(REPORT_FOLDER + this.getById(projectId).getReport());
        log.info(path.toString());
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // 确保文件存在
        if (resource.exists() || resource.isReadable()) {
            log.info(resource.getFilename());
            // 设置内容类型和头信息
            String contentType = null;
            try {
                contentType = Files.probeContentType(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String encodedFileName = null;
            encodedFileName = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).replace("+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
            log.info(encodedFileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .headers(headers)
                    .body(resource);
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }

    @Override
    public Boolean securityResult(SecurityReq req) {
        Project project = this.getById(req.getId());
        project.setResult(req.getResult());
        return this.updateById(project);
    }

    @Override
    public Boolean deleteReportById(String projectId) {
        Project project = this.getById(projectId);
        String report = project.getReport();
        deleteReport(report);
        project.setReport("");
        this.updateById(project);
        return true;
    }
}