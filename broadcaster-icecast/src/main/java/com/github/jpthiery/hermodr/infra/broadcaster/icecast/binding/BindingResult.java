package com.github.jpthiery.hermodr.broadcaster.infra.broadcaster.icecast.binding;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class BindingResult {

    private final BindingLibShout bindingLibShout;

    private final String errorMessage;

    BindingResult(BindingLibShout bindingLibShout, String errorMessage) {
        requireNonNull(bindingLibShout, "bindingLibShout must be defined.");
        this.bindingLibShout = bindingLibShout;
        this.errorMessage = errorMessage;
    }

    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    public boolean isSuccess() {
        return errorMessage == null || errorMessage.length() == 0;
    }

    public @NotNull
    BindingResult then(Function<BindingLibShout, BindingResult> applier) {
        if (isSuccess()) {
            return applier.apply(bindingLibShout);
        }
        return this;
    }

    static BindingResult success(BindingLibShout bindingLibShout) {
        return new BindingResult(bindingLibShout, null);
    }

    static BindingResult failed(BindingLibShout bindingLibShout, String errorMessage) {
        if (errorMessage == null || errorMessage.length() == 0) {
            throw new IllegalArgumentException("errorMessage must be defined.");
        }
        return new BindingResult(bindingLibShout, errorMessage);
    }

}
