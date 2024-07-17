package com.codiary.backend.global.apiPayload.exception.handler;


import com.codiary.backend.global.apiPayload.code.BaseErrorCode;
import com.codiary.backend.global.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}