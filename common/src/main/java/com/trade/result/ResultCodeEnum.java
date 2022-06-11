package com.trade.result;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResultCodeEnum {

    SUCCESS(true, 20000, "成功"),
    UNKNOWN_REASON(false, 20099, "未知错误"),

    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
    JSON_PARSE_ERROR(false, 21002, "json解析异常"),
    PARAM_ERROR(false, 21003, "参数不正确"),

    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),
    FILE_DELETE_ERROR(false, 21005, "文件刪除错误"),
    EXCEL_DATA_IMPORT_ERROR(false, 21006, "Excel数据导入错误"),


    URL_ENCODE_ERROR(false, 23001, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR(false, 23002, "非法回调请求"),
    FETCH_ACCESSTOKEN_FAILD(false, 23003, "获取accessToken失败"),
    FETCH_USERINFO_ERROR(false, 23004, "获取用户信息失败"),
    LOGIN_ERROR(false, 23005, "用户名或密码不正确"),
    LOGOUT_ERROR(false, 23006,"登出错误"),

    COMMENT_EMPTY(false, 24006, "评论内容必须填写"),
    GET_INDEX_KLINE_FAILED(false, 24001, "获取均k线数据失败"),
    GET_KLINE_FAILED(false, 24002, "获取k线数据失败"),
    GET_STOCK_INFO_FAILED(false, 24003, "获取股票查询结果失败"),
    GET_STOCK_LIST_INFO_FAILED(false, 24004, "获取股票分页查询结果失败"),

    PAY_RUN(false, 25000, "支付中"),
    PAY_UNIFIEDORDER_ERROR(false, 25001, "统一下单错误"),
    PAY_ORDERQUERY_ERROR(false, 25002, "查询支付结果错误"),


    GATEWAY_ERROR(false, 26000, "服务不能访问"),

    MAIL_ERROR(false,28008,"邮箱发送失败"),
    CODE_ERROR(false, 28000, "验证码错误"),
    LOGIN_DISABLED_ERROR(false, 28002, "该用户已被禁用"),
    REGISTER_USERINFO_ERROR(false, 28003, "手机号或者邮箱已被注册"),
    LOGIN_AUTH(false, 28004, "请先进行登录"),
    LOGIN_ACL(false, 28005, "没有权限"),
    SMS_SEND_ERROR(false, 28006, "短信发送失败"),
    SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL(false, 28007, "短信发送过于频繁"),
    MMS_SEND_ERROR(false, 28008, "邮件发送失败"),
    MMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL(false, 28009, "邮件发送过于频繁"),
    AUTH_ERROR(false, 28010, "用户认证失败");

    private final Boolean success;

    private final Integer code;

    private final String message;

    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
