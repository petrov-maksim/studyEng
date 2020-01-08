package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SessionCache {
    INSTANCE;
    private Map<String, Integer> authorizedUsers = new ConcurrentHashMap<>();

    public boolean isAuthorized(String sessionId){
        return authorizedUsers.containsKey(sessionId);
    }

    public void authorizeUser(String sessionId, Integer dbId){
        authorizedUsers.put(sessionId, dbId);
    }

    public void deauthorize(String sessionId){
        authorizedUsers.remove(sessionId);
    }
}
