package com.job.assignment.crossborder.service.exception;

import java.util.function.Supplier;

public class WrongTargetWordException extends RuntimeException  {
    public WrongTargetWordException(String target) {
        super("Wrong target word typed, " + target);
    }
}