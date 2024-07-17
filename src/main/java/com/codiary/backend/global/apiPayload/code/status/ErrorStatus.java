package com.codiary.backend.global.apiPayload.code.status;

import com.codiary.backend.global.apiPayload.code.BaseErrorCode;
import com.codiary.backend.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 회원 관려 에러 1000
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_1001", "사용자가 없습니다."),
    MEMBER_NAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER_1002", "이름입력은 필수 입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER_1003", "이미 존재하는 유저입니다."),
    MEMBER_ID_NULL(HttpStatus.BAD_REQUEST, "MEMBER_1004", "사용자 아이디는 필수 입니다."),
    MEMBER_ADMIN_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "MEMBER_1005", "관리자 권한이 없습니다."),

    // 팀 관련 에러 2000
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "TEAM_2001", "팀이 없습니다."),
    TEAM_NAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "TEAM_2002", "팀 이름입력은 필수 입니다."),
    TEAM_ALREADY_EXISTS(HttpStatus.CONFLICT, "TEAM_2003", "이미 존재하는 팀입니다."),
    TEAM_ADMIN_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "TEAM_2004", "관리자 권한이 없습니다."),

    // 일기 관려 에러 3000
    DIARY_CREATE_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "DIARY_3001", "일기 생성 권한이 없습니다."),
    DIARY_UPDATE_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "DIARY_3002", "일기 수정 권한이 없습니다."),
    DIARY_DELETE_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "DIARY_3003", "일기 삭제 권한이 없습니다."),
    DIARY_ADD_MEMBER_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "DIARY_3004", "일기에 사용자를 추가할 권한이 없습니다."),
    DIARY_MEMBER_DELETE_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "DIARY_3005", "일기 참여자 삭제 권한이 없습니다.");

    // 코멘트 관련 에러 4000


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}