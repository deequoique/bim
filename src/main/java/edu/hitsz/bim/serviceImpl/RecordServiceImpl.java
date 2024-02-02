package edu.hitsz.bim.serviceImpl;

import edu.hitsz.bim.entity.Record;
import edu.hitsz.bim.mappers.RecordMapper;
import edu.hitsz.bim.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
