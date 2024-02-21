package edu.hitsz.bim.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/2/19 16:54
 */
@Data
@Builder
public class CreateGlassReq {
    private String project_id;
    private String name;
    private String area;
    private String thickness;
    private String strength;
    private String configuration;
    private String manufacturer;
}
