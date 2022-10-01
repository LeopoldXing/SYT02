package com.hilda.common.exception;

import com.hilda.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        return R.error();
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message(e.getMessage());
    }

    //自定义异常处理
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public R error(YyghException e){
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }

}
