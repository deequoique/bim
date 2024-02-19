package edu.hitsz.bim.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private Integer userId;

    @Schema(description ="Security level of the project")
    @TableField("security")
    private String security;

    @Schema(description ="Time when the project was created")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description ="Time when the project was last updated")
    @TableField("update_time")
    private Date updateTime;


}
