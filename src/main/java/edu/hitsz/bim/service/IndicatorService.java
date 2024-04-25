package edu.hitsz.bim.service;

import edu.hitsz.bim.domain.dto.CreateIndicatorReq;
import edu.hitsz.bim.entity.Indicator;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * Table for storing various indicators related to projects 服务类
 * </p>
 *
 * @author lane
 * @since 2024-04-25 08:59:42
 */
public interface IndicatorService extends IService<Indicator> {

    Boolean delete(String s);

    List<Indicator> getList(String projectId);

    Boolean create(CreateIndicatorReq createIndicatorReq);
}
