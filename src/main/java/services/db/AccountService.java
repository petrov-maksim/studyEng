package services.db;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import util.QueryExecutor;
import util.SessionCache;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис, осуществляющий работу, свзанную с аккаунтами пользователей
 */
public class AccountService implements Abonent, Runnable {
    private static final Address address = new Address();
    private final QueryExecutor queryExecutor;

    public AccountService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    private static final String GET_ID_BY_EMAIL_AND_PASS_QUERY = "SELECT id FROM users WHERE email = '%s' AND password = '%s';";
    private static final String REGISTER_USER_QUERY = "INSERT INTO users (name, email, password) VALUES ('%s', '%s', '%s') RETURNING id;";
    private static final String CREATE_MAIN_WS_QUERY = "INSERT INTO wordSets (user_id, name, isMain) VALUES ('%d', '%s', '%b') RETURNING id;";
    private static final String MAIN_WS_NAME = "Все слова";


    /**
     * Register user in DB and authorize
     * @return true if successfully, false otherwise
     */
    public boolean signUp(String mail, String password, String name, String sessionId){
        if (getId(mail, password) != -1)
            return false;

        Integer id = queryExecutor.execQuery(String.format(REGISTER_USER_QUERY, name, mail, password), (resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        }));

        if (id == null || id == -1) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Didn't get user's id");
            return false;
        }

        if (createMainWordSet(id) == -1) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Didn't create main wordset for user");
            return false;
        }

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
            SessionCache.INSTANCE.authorizeUser(sessionId, id);
            return true;
        }
        return false;
    }

    /**
     * Might be used to ensure that user exists
     * @return user's id or -1
     */
    private int getId(String mail, String password){
        return queryExecutor.execQuery(String.format(GET_ID_BY_EMAIL_AND_PASS_QUERY, mail, password), (rs) ->{
            if (rs.next())
                return rs.getInt("id");
            else return -1;
        });
    }

    /**
     * У каждого пользователя при регистрации создается главдый wordSet
     */
    private int createMainWordSet(int userId) {
        return queryExecutor.execQuery(String.format(CREATE_MAIN_WS_QUERY,
                userId, MAIN_WS_NAME, true), resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        });
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public static Address getAdr(){
        return address;
    }

    @Override
    public void run() {
        while(true){
            try{
                MessageSystem.INSTANCE.execForService(this);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            }
        }
    }
}
