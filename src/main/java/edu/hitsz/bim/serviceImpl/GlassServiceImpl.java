package edu.hitsz.bim.serviceImpl;

import edu.hitsz.bim.entity.Glass;
import edu.hitsz.bim.mappers.GlassMapper;
import edu.hitsz.bim.service.GlassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Table for storing glass information 服务实现类
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Service
public class GlassServiceImpl extends ServiceImpl<GlassMapper, Glass> implements GlassService {

}
