package services.db;

import util.QueryExecutor;
import util.SessionCache;

public class AccountService extends AbstractDBService {
    private SessionCache sessionCache = SessionCache.INSTANCE;
    private static  final String SELECT_ID = "SELECT id FROM users WHERE email = '%s' AND password = '%s';";
    private static final String INSERT_STRING = "INSERT INTO users (password, login, email) VALUES ('%s', '%s', '%s');";

    public AccountService(int threadsNum, QueryExecutor queryExecutor) {
        super(threadsNum, queryExecutor);
    }

    /**
     * Checks database on user existence, if exists authorize in SessionCache
     * @return true if user exist, false otherwise
     */
    public boolean authorize(String mail, String password, String sessionId){
        int id = getId(mail, password);

        if (id != -1) {
            sessionCache.authorizeUser(sessionId, id);
            return true;
        }
        return false;
    }

    /**
     * Register user in DB
     * @return true if successfully, false otherwise
     */
    public boolean register(String mail, String password, String name, String sessionId){
        int id = getId(mail,password);
        if (id != -1)
            return false;

        queryExecutor.execUpdate(String.format(INSERT_STRING, password, name, mail));
        SessionCache.INSTANCE.authorizeUser(sessionId, id);
        return true;
    }

    /**
     * Might be used to ensure that user exists
     * @return user's id or -1
     */
    private int getId(String mail, String password){
        return queryExecutor.execQuery(String.format(SELECT_ID, mail, password),
                (rs) ->{
                    if (rs.next())
                        return rs.getInt("id");
                    else return -1;
                }
        );
    }
}
