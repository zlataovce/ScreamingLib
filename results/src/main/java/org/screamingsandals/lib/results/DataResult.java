package org.screamingsandals.lib.results;

import java.util.Optional;

public interface DataResult<T> extends BaseResult {

    Optional<T> getData();

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> ok() {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.OK)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> okData(T data) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.OK)
                .data(data)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> ok(String message, T data) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.OK)
                .message(message)
                .data(data)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> fail(String message) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.FAIL)
                .message(message)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> fail(String message, Throwable throwable) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.FAIL)
                .message(message)
                .throwable(throwable)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> fail(Throwable throwable) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.FAIL)
                .message(throwable.getMessage())
                .throwable(throwable)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> unknown() {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.UNKNOWN)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> unknown(String message) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Status.UNKNOWN)
                .message(message)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> transform(DataResult<?> dataResult) {
        return (DataResult<T>) dataResult;
    }

    default SimpleResult toSimple() {
        switch (this.getStatus()) {
            case FAIL:
                return SimpleResult.fail(getMessage());
            case OK:
                return SimpleResult.ok(getMessage());
            default:
                return SimpleResult.unknown(getMessage());
        }
    }
}
