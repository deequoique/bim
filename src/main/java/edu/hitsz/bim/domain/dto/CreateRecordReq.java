package edu.hitsz.bim.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/2/14 21:45
 */
@Data
@Builder
public class CreateRecordReq {
    private String project_id;
    private String context;
    private String result;
    private String time;
    private String staff;
}
