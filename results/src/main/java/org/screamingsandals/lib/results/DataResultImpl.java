package org.screamingsandals.lib.results;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Builder(access = AccessLevel.PACKAGE)
@Getter
@ToString
public class DataResultImpl<T> implements DataResult<T> {
    private final Status status;
    private final String message;
    private final Throwable throwable;
    private final T data;

    @Override
    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    @Override
    public boolean isOk() {
        return status == Status.OK;
    }

    @Override
    public boolean isFail() {
        return status == Status.FAIL;
    }
}
