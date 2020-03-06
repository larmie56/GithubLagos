package com.stutern.githublagos.utils;

public class NetworkState {

    public enum State {
        LOADING,
        SUCCESS,
        FAILED
    }

    private final State status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    static {
        LOADED=new NetworkState(State.SUCCESS,"Success");
        LOADING=new NetworkState(State.LOADING,"Running");
    }

    public NetworkState(State status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public State getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

}
