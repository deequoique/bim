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
 * Table for storing project information
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Getter
@Setter
@TableName("project")
@ApiModel(value = "Project对象", description = "Table for storing project information")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("Name of the project")
    @TableField("name")
    private String name;

    @ApiModelProperty("Identifier for the user associated with the project")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("Security level of the project")
    @TableField("security")
    private String security;

    @ApiModelProperty("Time when the project was created")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("Time when the project was last updated")
    @TableField("update_time")
    private Date updateTime;


}
