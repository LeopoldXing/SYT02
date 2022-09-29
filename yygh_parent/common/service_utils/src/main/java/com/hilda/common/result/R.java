package com.hilda.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@ApiModel(description = "通用的返回结果")
@Data//set get 方法
public class R {

    @ApiModelProperty(value = "表示是否操作成功")
    private Boolean success;//表示是否操作成功  true  false

    @ApiModelProperty(value = "自定义的状态码")
    private Integer code;//自定义的状态码   20000 成功  20001失败

    @ApiModelProperty(value = "提示信息")
    private String message;//提示信息   例如：  "操作成功"  "操作失败" ...


    @ApiModelProperty(value = "存储返回数据")
    private Map<String,Object> data = new HashMap<>();// Map 把数据存入到map中  (   list  obj   array 。。。。 )

    //封装方法，直接返回创建好的R对象
    //别的地方你就不能直接new了
    private R(){
    }

    /**
     * 操作成功，调用ok方法返回R对象
     * @return
     */
    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("操作成功");
//        r.setData();
        return r;
    }

    /**
     * 操作失败，调用error方法返回R对象
     * @return
     */
    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setMessage("操作失败");
        r.setCode(ResultCode.ERROR);
//        r.setData();
        return r;
    }

    //修改R对象的属性值  code message  success data
    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R data(Map<String,Object> data){
        this.setData(data);
        return this;
    }

    public R data(String key , Object value){
//        this.setData(data);  key -value --> data中
        this.data.put(key,value);//如果 data不初始化，会有空指针异常
        return this;
    }
}
