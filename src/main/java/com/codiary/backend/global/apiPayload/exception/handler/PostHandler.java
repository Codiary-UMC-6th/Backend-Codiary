package com.codiary.backend.global.apiPayload.exception.handler;

import com.codiary.backend.global.apiPayload.code.BaseErrorCode;
import com.codiary.backend.global.apiPayload.exception.GeneralException;

public class PostHandler extends GeneralException {

    public PostHandler(BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
