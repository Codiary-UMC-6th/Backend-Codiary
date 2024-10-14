package com.codiary.backend.global.apiPayload.exception.handler;

import com.codiary.backend.domain.team.entity.Team;
import com.codiary.backend.global.apiPayload.code.BaseErrorCode;
import com.codiary.backend.global.apiPayload.exception.GeneralException;

public class TeamHandler extends GeneralException {

    public TeamHandler(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
