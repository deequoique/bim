package edu.hitsz.bim.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/4/26 13:41
 */
@Data
@Builder
public class ValueIndicatorReq {
    private String id;
    private String value;
}
