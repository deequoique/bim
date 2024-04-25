package edu.hitsz.bim.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/2/4 19:23
 */
@Data
@Builder
public class CreateProjectReq {
    private String user_id;
    private String name;
    private String location;
    private String coordinate;
}
