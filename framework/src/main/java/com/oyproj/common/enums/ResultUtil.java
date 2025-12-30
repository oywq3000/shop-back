package com.oyproj.common.enums;

import com.oyproj.common.vo.ResultMessage;

public class ResultUtil<T> {
    /**
     * abstract class, store the result
     */
    private final ResultMessage<T> resultMessage;

    //正常响应
    private static final Integer SUCCESS = 200;

    //构造话方法，给响应结果默认值
    public ResultUtil(){
        resultMessage = new ResultMessage<>();
        resultMessage.setSuccess(true);
        resultMessage.setMessage("success");
        resultMessage.setCode(ResultCode.SUCCESS.code());
    }

    //返回数据
    public ResultMessage<T> setData(T t){
        this.resultMessage.setResult(t);
        return this.resultMessage;
    }

    //返回成功消息
    public ResultMessage<T> setSuccessMsg(ResultCode resultCode){
        this.resultMessage.setSuccess(true);
        this.resultMessage.setMessage(resultCode.message());
        this.resultMessage.setCode(resultCode.code());
        return this.resultMessage;
    }

    //服务器异常
    public ResultMessage<T> setErrorMsg(ResultCode resultCode){
        this.resultMessage.setSuccess(false);
        this.resultMessage.setMessage(resultCode.message());
        this.resultMessage.setCode(resultCode.code());
        return this.resultMessage;
    }

    public ResultMessage<T> setErrorMsg(Integer code,String msg){
        this.resultMessage.setSuccess(false);
        this.resultMessage.setMessage(msg);
        this.resultMessage.setCode(code);
        return this.resultMessage;
    }

    //返回结果集
    public static <T> ResultMessage<T> data(T t) {
        return new ResultUtil<T>().setData(t);
    }

    //返回状态码
    public static <T> ResultMessage<T> success(ResultCode resultCode) {
        return new ResultUtil<T>().setSuccessMsg(resultCode);
    }

    //返回成功消息
    public static <T> ResultMessage<T> success() {
        return new ResultUtil<T>().setSuccessMsg(ResultCode.SUCCESS);
    }

    //返回失败消息
    public static <T> ResultMessage<T> error(ResultCode resultCode) {
        return new ResultUtil<T>().setErrorMsg(resultCode);
    }
    //返回失败
    public static <T> ResultMessage<T> error(Integer code, String msg) {
        return new ResultUtil<T>().setErrorMsg(code, msg);
    }
    public static <T> ResultMessage<T> error() {
        return new ResultUtil<T>().setErrorMsg(ResultCode.ERROR);
    }


}
