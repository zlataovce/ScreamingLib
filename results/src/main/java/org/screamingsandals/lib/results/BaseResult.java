package org.screamingsandals.lib.results;

import java.io.Serializable;

public interface BaseResult extends Serializable {

    Status getStatus();

    String getMessage();

    Throwable getThrowable();

    boolean isOk();

    boolean isFail();

    enum Status {
        OK,
        FAIL,
        UNKNOWN
    }
}
