package me.voler.wireless.administrator.util;

public enum LoginError {

    SYSTEM_ERROR(-200),

    LEVEL_ERROR(-300),

    EMAIL_ERROR(-301),

    NOT_EQUAL_ERROR(-400),

    NONE_ERROR(0);

    private int errCode;

    public final int getErrCode() {
        return errCode;
    }

    LoginError(int errCode) {
        this.errCode = errCode;
    }

}

