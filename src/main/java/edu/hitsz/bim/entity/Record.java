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
 * Table for storing records
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Getter
@Setter
@TableName("record")
@ApiModel(value = "Record对象", description = "Table for storing records")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("Context of the record")
    @TableField("context")
    private String context;

    @ApiModelProperty("Result of the record")
    @TableField("result")
    private String result;

    @ApiModelProperty("Time when the record was made")
    @TableField("time")
    private Date time;

    @ApiModelProperty("Staff involved in making the record")
    @TableField("staff")
    private String staff;

    @ApiModelProperty("Time when the record was created")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("Time when the record was last updated")
    @TableField("update_time")
    private Date updateTime;


}
