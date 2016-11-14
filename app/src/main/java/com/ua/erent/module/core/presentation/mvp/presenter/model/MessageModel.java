package com.ua.erent.module.core.presentation.mvp.presenter.model;

/**
 * Created by Максим on 11/14/2016.
 */

public final class MessageModel {

    public enum Direction {

        LEFT(0), RIGHT(1);

        final int type;

        Direction(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static Direction forType(int type) {
            for (final Direction direction : Direction.values()) {
                if (direction.getType() == type) return direction;
            }
            return null;
        }
    }

    private final String timestamp;
    private final String body;
    private final Direction direction;

    public MessageModel(String timestamp, String body, Direction direction) {
        this.timestamp = timestamp;
        this.body = body;
        this.direction = direction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getBody() {
        return body;
    }

    public Direction getDirection() {
        return direction;
    }
}
