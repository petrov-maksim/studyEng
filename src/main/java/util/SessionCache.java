package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * В данном классе кэшируются авторизованные пользователи
 */
public enum SessionCache {
    INSTANCE;
    private Map<String, Integer> authorizedUsers = new ConcurrentHashMap<>();

    public boolean isAuthorized(String sessionId){
        return authorizedUsers.containsKey(sessionId);
    }

    public void authorizeUser(String sessionId, Integer dbUserId){
        authorizedUsers.put(sessionId, dbUserId);
    }

    public void deauthorize(String sessionId){
        authorizedUsers.remove(sessionId);
    }

    public int getUserIdBySessionId(String sessionId){
        return authorizedUsers.get(sessionId);
    }
}
