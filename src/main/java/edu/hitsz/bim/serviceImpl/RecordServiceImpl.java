package edu.hitsz.bim.serviceImpl;

import edu.hitsz.bim.domain.dto.CreateRecordReq;
import edu.hitsz.bim.entity.Record;
import edu.hitsz.bim.mappers.RecordMapper;
import edu.hitsz.bim.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * Table for storing records 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Override
    public Boolean create(CreateRecordReq req) {
        return null;
    }

    @Override
    public List<Record> lists(String projectId) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }
}
