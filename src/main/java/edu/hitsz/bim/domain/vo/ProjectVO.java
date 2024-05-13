package edu.hitsz.bim.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/2/13 22:51
 */
@Data
@Builder
public class ProjectVO {
    private Integer id;
    private String name;
    private String location;
    private String coordinate;
    private String model;
    private String assetId;

}
