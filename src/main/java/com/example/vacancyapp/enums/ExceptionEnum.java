package com.example.vacancyapp.enums;

public enum ExceptionEnum {
    USER_NOT_FOUND(401,"User can not find with given id"),

    VALIDATION(402,"Validation error"),
    INPUT(406,"ID cannot be null and string.Please enter number"),

    UNKNOWN(403,"Unknown error"),
    PHOTO(404,"Photo hasn't been uploaded"),
    SUCCESS(200,"Success"),

    ERROR(500,"Something went wrong"),

    EMPTY(405,"Users are not exist yet"),
    EMPTY_SKILL(405,"Skill are not exist"),
    EMPTY_VACANCY(405,"Vacancy are not exist"),

    BAD_REQUEST(408,"Bad request.Cannot send empty data"),
    MAIL(407,"Mail is already in use"),
    ROLE(409,"Role is not defined.There are three roles:USER,ADMIN,HR");

    private final String message;

    private final int code;

    ExceptionEnum(int code, String message) {
        this.message=message;
        this.code=code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
