package edu.hitsz.bim.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author lane
 * @description
 * @since 2024/2/19 15:33
 */
@Data
@Builder
public class CreateUserReq {
    private String username;
    private String password;
}
