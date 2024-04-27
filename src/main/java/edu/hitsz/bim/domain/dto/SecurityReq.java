package edu.hitsz.bim.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/4/27 14:04
 */
@Builder
@Data
public class SecurityReq {
    private String id;
    private String result;
}
