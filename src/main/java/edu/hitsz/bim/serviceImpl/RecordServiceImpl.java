package edu.hitsz.bim.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hitsz.bim.common.BIMException;
import edu.hitsz.bim.common.ResponseEnum;
import edu.hitsz.bim.domain.dto.CreateRecordReq;
import edu.hitsz.bim.entity.Record;
import edu.hitsz.bim.mappers.RecordMapper;
import edu.hitsz.bim.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        Record build = Record.builder().context(req.getContext())
                .result(req.getResult())
                .time(req.getTime())
                .staff(req.getStaff())
                .build();

        return this.save(build);
    }

    @Override
    public List<Record> lists(String projectId) {
        return this.list(new LambdaQueryWrapper<Record>().eq(Record::getProjectId, projectId));
    }

    @Override
    public Boolean delete(String id) {
        if (Objects.isNull(this.getById(id))) {
            throw BIMException.build(ResponseEnum.ERROR);
        }
        return this.removeById(id);
    }
}
