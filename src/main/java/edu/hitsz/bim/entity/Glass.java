package edu.hitsz.bim.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Getter
@Setter
@TableName("glass")
@ApiModel(value = "Glass对象", description = "Table for storing glass information")
public class Glass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("Identifier for the project")
    @TableField("project_id")
    private Integer projectId;

    @ApiModelProperty("Name of the glass item")
    @TableField("name")
    private String name;

    @ApiModelProperty("Area of the glass")
    @TableField("area")
    private Double area;

    @ApiModelProperty("Thickness of the glass")
    @TableField("thickness")
    private Double thickness;

    @ApiModelProperty("Strength of the glass")
    @TableField("strength")
    private Double strength;

    @ApiModelProperty("Configuration of the glass")
    @TableField("configuration")
    private String configuration;

    @ApiModelProperty("Manufacturer of the glass")
    @TableField("manufacturer")
    private String manufacturer;

    @ApiModelProperty("Time when the glass item was created")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("Time when the glass item was last updated")
    @TableField("update_time")
    private Date updateTime;


}
