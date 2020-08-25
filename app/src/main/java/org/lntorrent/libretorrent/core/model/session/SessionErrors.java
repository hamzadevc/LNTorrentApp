

package org.lntorrent.libretorrent.core.model.session;

import androidx.annotation.NonNull;

import org.libtorrent4j.ErrorCode;

class SessionErrors
{
    private static class Error
    {
        int errCode;
        String errMsg;

        Error(int errCode, String errMsg)
        {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }
    }

    private static final Error[] errors = new Error[] {
            new Error(11, "Try again"),
            new Error(22, "Invalid argument"),
    };

    static boolean isNonCritical(@NonNull ErrorCode error)
    {
        if (!error.isError())
            return true;

        for (Error nonCriticalError : errors) {
            if (error.value() == nonCriticalError.errCode &&
                nonCriticalError.errMsg.equalsIgnoreCase(error.message()))
                return true;
        }

        return false;
    }

    static String getErrorMsg(ErrorCode error)
    {
        return (error == null ? "" : error.message() + ", code " + error.value());
    }
}
