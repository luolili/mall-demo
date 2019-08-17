package com.mall.common.exception;

import com.mall.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MallException extends RuntimeException{

    private ExceptionEnum exceptionEnum;
}
