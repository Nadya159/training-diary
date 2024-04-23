package com.nadya159.entity.types;

public enum ActionType {
    REGISTRATION,

    /**
     * Represents the action of user authorization.
     */
    AUTHORIZATION,

    /**
     * Represents the action of submitting meter readings.
     */
    SUBMIT_TRAINING,

    /**
     * Represents the action of getting the history of meter readings.
     */
    GET_HISTORY
}
