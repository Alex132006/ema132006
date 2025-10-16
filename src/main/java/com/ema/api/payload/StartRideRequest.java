package com.ema.api.payload;

public class StartRideRequest {
    private Long userId;
    private Long scooterId;

    public StartRideRequest(Long userId, Long scooterId) {
        this.userId = userId;
        this.scooterId = scooterId;
    }

    public StartRideRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getScooterId() {
        return scooterId;
    }

    public void setScooterId(Long scooterId) {
        this.scooterId = scooterId;
    }
}
