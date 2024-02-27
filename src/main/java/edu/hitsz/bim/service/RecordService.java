package edu.hitsz.bim.service;

import edu.hitsz.bim.domain.dto.CreateRecordReq;
import edu.hitsz.bim.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * Table for storing records 服务类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
public interface RecordService extends IService<Record> {

    String create(CreateRecordReq req);

    List<Record> lists(String projectId);

    Boolean delete(String id);
}
