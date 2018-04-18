package com.mavbids.app;

/**
 * Created by SaiKumar on 11/25/2015.
 */
public class SessionManager {
    private static SessionManager sessionManager;
    private String sessionId;
    private String mavBidsHost;
    private String userId;

    protected SessionManager(){
    }

    public static SessionManager getInstance(){

        if(sessionManager == null){
            sessionManager = new SessionManager();
        }

        return sessionManager;
    }

    public String getSessionId() {
        if(sessionId == null)
            return "";

        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMavBidsHost() {
        return mavBidsHost;
    }

    public void setMavBidsHost(String mavBidsHost) {
        this.mavBidsHost = mavBidsHost;
    }

    public void clearSession(){
        this.sessionId = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
