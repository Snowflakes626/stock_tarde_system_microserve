> result包下面的内容与返回结果相关
> 

## R
这个类是返回结果类型的类
```java
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<>();
```
返回以下几类结果：
- ok
- error

并可以设置各个属性
## ResultCodeEnum 
这个类是错误类型枚举类
```java
    // 成功状态
    private final Boolean success;
    // 返回的具体状态码
    private final Integer code;
    // 返回的错误信息
    private final String message;
```
有以下几类错误类型：
- 成功/失误
- sql/JSON等解析错误
- 文件上传/删除错误
- 视频上传/删除错误
- 用户信息/登录/token错误
- 支付错误
- 验证码错误

