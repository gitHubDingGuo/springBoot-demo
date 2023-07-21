package top.javahouse.swagger.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVo {
    @ApiModelProperty(name ="userName", value = "用户名称", dataType = "String",required = true)
    private String userName;
    @ApiModelProperty(name ="age", value = "用户年龄", dataType = "int")
    private int age;
}
