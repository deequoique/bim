package edu.hitsz.bim.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Table for storing project information
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Getter
@Setter
@Builder
@TableName("project")
@Schema(description = "Table for storing project information")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description ="Name of the project")
    @TableField("name")
    private String name;

    @Schema(description ="Identifier for the user associated with the project")
    @TableField("user_id")
    private String userId;

    @Schema(description ="Location of the project")
    @TableField("location")
    private String location;

    @TableField("coordinate")
    private String coordinate;

    @TableField("file")
    private String file;

    @TableField("report")
    private String report;

    @TableField("result")
    private String result;

    @TableField("asset_id")
    private String assetId;

    @Schema(description ="Time when the project was created")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description ="Time when the project was last updated")
    @TableField("update_time")
    private Date updateTime;


}
