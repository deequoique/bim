package edu.hitsz.bim.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/4/25 21:01
 */
@Data
@Builder
public class CreateIndicatorReq {
    private Integer project_id;
    private String name;
    private String weight;
}
