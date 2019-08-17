package com.mall.common.advice;

import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MallException.class)
    public ResponseEntity<ExceptionResult> handleException(MallException e) {
        ExceptionEnum exEnum = e.getExceptionEnum();
        ExceptionResult exceptionResult = new ExceptionResult(exEnum);
        return ResponseEntity.status(exEnum.getCode()).body(exceptionResult);
    }
}
