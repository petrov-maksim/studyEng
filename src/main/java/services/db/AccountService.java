package services.db;

import services.AbstractService;
import util.QueryExecutor;
import util.SessionCache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class AccountService extends AbstractService {
    private SessionCache sessionCache = SessionCache.INSTANCE;
    private final QueryExecutor queryExecutor;
    private static  final String SELECT_ID = "SELECT id FROM users WHERE email = '%s' AND password = '%s';";
    private static final String REGISTER_USER = "INSERT INTO users (name, email, password) VALUES ('%s', '%s', '%s') RETURNING id;";

    public AccountService(int threadsNum, QueryExecutor queryExecutor) {
        super(threadsNum);
        this.queryExecutor = queryExecutor;
    }

    /**
     * Register user in DB and authorize
     * @return true if successfully, false otherwise
     */
    public boolean signUp(String mail, String password, String name, String sessionId){
        Integer id = queryExecutor.execQuery(String.format(REGISTER_USER, name, mail, password), (resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        }));
        if (id == null || id == -1)
            return false;

        createMainWordSet(id);

        SessionCache.INSTANCE.authorizeUser(sessionId, id);
        return true;
    }

    /**
     * Checks database on user existence, if exists authorize in SessionCache
     * @return true if user exist, false otherwise
     */
    public boolean signIn(String mail, String password, String sessionId){
        int id = getId(mail, password);

        if (id != -1) {
            sessionCache.authorizeUser(sessionId, id);
            return true;
        }
        return false;
    }

    /**
     * Might be used to ensure that user exists
     * @return user's id or -1
     */
    private int getId(String mail, String password){
        return queryExecutor.execQuery(String.format(SELECT_ID, mail, password), (rs) ->{
            if (rs.next())
                return rs.getInt("id");
            else return -1;
        });
    }

    /**
     * У каждого пользователя при регистрации создается главдый wordSet
     */
    private void createMainWordSet(int userId) {
        queryExecutor.execUpdate(String.format("INSERT INTO wordSets (user_id, name, isMain) VALUES ('%d', '%s', '%b');",
                userId, "All Words", true));
    }
}
