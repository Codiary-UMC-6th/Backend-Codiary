package com.codiary.backend.global.apiPayload.exception.handler;

import com.codiary.backend.global.apiPayload.code.BaseErrorCode;
import com.codiary.backend.global.apiPayload.exception.GeneralException;

public class BookmarkHandler extends GeneralException {

    public BookmarkHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
