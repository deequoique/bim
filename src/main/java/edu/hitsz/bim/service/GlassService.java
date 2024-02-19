package edu.hitsz.bim.service;

import edu.hitsz.bim.domain.dto.CreateGlassReq;
import edu.hitsz.bim.entity.Glass;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * Table for storing glass information 服务类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
public interface GlassService extends IService<Glass> {
    Boolean create(CreateGlassReq req);
}
