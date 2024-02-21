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
 * Table for storing glass information
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Builder
@Getter
@Setter
@TableName("glass")
@Schema(description = "Table for storing glass information")
public class Glass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Identifier for the project")
    @TableField("project_id")
    private Integer projectId;

    @Schema(description = "Name of the glass item")
    @TableField("name")
    private String name;

    @Schema(description = "Area of the glass")
    @TableField("area")
    private Double area;

    @Schema(description = "Thickness of the glass")
    @TableField("thickness")
    private Double thickness;

    @Schema(description = "Strength of the glass")
    @TableField("strength")
    private Double strength;

    @Schema(description = "Configuration of the glass")
    @TableField("configuration")
    private String configuration;

    @Schema(description = "Manufacturer of the glass")
    @TableField("manufacturer")
    private String manufacturer;

    @Schema(description = "Time when the glass item was created")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "Time when the glass item was last updated")
    @TableField("update_time")
    private Date updateTime;


}
