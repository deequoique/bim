package edu.hitsz.bim.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/5/8 22:03
 */
@Data
@Builder
public class FileUploadResult {
    private String name;
    private String etag;
    private String checksum;
}
