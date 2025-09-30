package io.github.lazzz.sagittarius.common.result;

import io.github.lazzz.sagittarius.common.utils.condition.IfFlattener;
import io.github.lazzz.sagittarius.common.utils.condition.ReturningIfFlattener;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应结果类
 * 用于前后端数据交互的统一格式
 *
 * @author Lazzz
 */
@Data
public class Result<T> implements Serializable {

    private String code;

    private T data;

    private String msg;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> failed() {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), ResultCode.SYSTEM_EXECUTION_ERROR.getMsg(), null);
    }

    public static <T> Result<T> failed(String msg) {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> judge(boolean status) {
        return judge(status, null);
    }

    public static <T> Result<T> judge(boolean status, String msg) {
        return ReturningIfFlattener.<Boolean, Result<T>>of(status)
                .when(status, Result::success)
                .otherwise(() -> Result.failed(msg))
                .get();
    }

    public static <T> Result<T> failed(IResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> failed(IResultCode resultCode, String msg) {
        return result(resultCode.getCode(), msg, null);
    }

    private static <T> Result<T> result(IResultCode resultCode, T data) {
        return result(resultCode.getCode(), resultCode.getMsg(), data);
    }

    private static <T> Result<T> result(String code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static boolean isSuccess(Result<?> result) {
        return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
    }
}