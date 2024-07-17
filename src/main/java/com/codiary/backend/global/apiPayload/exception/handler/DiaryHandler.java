package com.codiary.backend.global.apiPayload.exception.handler;

import com.codiary.backend.global.apiPayload.code.BaseErrorCode;
import com.codiary.backend.global.apiPayload.exception.GeneralException;

public class DiaryHandler extends GeneralException {
    public DiaryHandler(BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
