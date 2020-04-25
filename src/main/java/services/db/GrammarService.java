package services.db;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import util.QueryExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис, осуществляющий работу, связанную с грамматическим разделом
 */
public class GrammarService implements Abonent, Runnable {
    private final static Address address = new Address();
    private final QueryExecutor queryExecutor;

    public GrammarService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    private static final int eng_rusId = 1;
    private static final int rus_engId = 2;
    private static final int writingId = 3;
    private static final int cardsId = 4;

    private static final String WORD_ALREADY_EXISTS_QUERY = "SELECT COUNT(*) FROM wordSet_word WHERE wordSet_id = %d AND word_id = %d;";
    private static final String ADD_WORD_TO_WORDSET_QUERY = "INSERT INTO wordSet_word (wordSet_id, word_id) VALUES ('%d', '%d');";
    private static final String ADD_TO_USER_WORD_TRAINING_QUERY = "INSERT INTO user_word_training (user_id, word_id, training_id) VALUES('%d', '%d', '%d')," +
            "('%d', '%d', '%d')," +
            "('%d', '%d', '%d')," +
            "('%d', '%d', '%d');";
    private static final String ADD_WORDS_TO_USER_QUERY_FOR_PREPAREDSTATEMENT = "INSERT INTO user_word_translation (user_id, word_id, translation_id) VALUES (?, ?, ?);";
    private static final String GET_CURRENT_LVL_FOR_USER_QUERY = "SELECT cur_lvl FROM users WHERE id = %d;";
    private static final String UPDATE_CURRENT_LVL_FOR_USER_QUERY = "UPDATE users SET cur_lvl = '%s' WHERE id = %d;";
    private static final String GET_WORD_ID_BY_WORD_QUERY = "SELECT id FROM words WHERE word = '%s';";
    private static final String ADD_NEW_WORD_QUERY = "INSERT INTO words (word) VALUES('%s') RETURNING id;";
    private static final String GET_TRANSLATION_ID_BY_TRANSLATION_QUERY = "SELECT id FROM translations WHERE translation = '%s';";
    private static final String ADD_NEW_TRANSLATION_QUERY = "INSERT INTO translations (translation) VALUES('%s') RETURNING id;";
    private static final String GET_MAINWS_ID_FOR_USER_QUERY = "SELECT id FROM wordSets WHERE user_id = '%d' AND isMain = true;";

    /**
     * Добовляет слово пользователю
     */
    public void addWordsForUser(int userId, Word words[]){
        int wordSetId = getMainWordSetIdForUser(userId);
        int wordId;
        int translationId;

        for (Word wordTO : words){
            String word = wordTO.getWord();

            if ((wordId = getWordId(word)) == -1)
                wordId = addNewWord(word);
            if (wordId == -1) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Didn't get wordId neither added new word");
                continue;
            }

            boolean wordAlreadyExists = queryExecutor.execQuery(String.format(WORD_ALREADY_EXISTS_QUERY,
                    wordSetId, wordId), resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("COUNT") > 0;
                return false;
            });

            if (wordAlreadyExists)
                continue;

            queryExecutor.execUpdate(String.format(ADD_WORD_TO_WORDSET_QUERY,
                    wordSetId, wordId));
            queryExecutor.execUpdate(String.format(ADD_TO_USER_WORD_TRAINING_QUERY, userId, wordId,
                    eng_rusId, userId, wordId, rus_engId, userId, wordId, writingId, userId, wordId, cardsId));

            PreparedStatement stmt = queryExecutor.prepareStatement(ADD_WORDS_TO_USER_QUERY_FOR_PREPAREDSTATEMENT);
            for (String translation : wordTO.getTranslations()){
                if ((translationId = getTranslationId(translation)) == -1)
                    translationId = addNewTranslation(translation);
                try{
                    stmt.setInt(1, userId);
                    stmt.setInt(2, wordId);
                    stmt.setInt(3, translationId);
                    stmt.addBatch();
                }catch (SQLException e){
                    Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
                }
            }
            queryExecutor.executeBatch(stmt);
        }
    }

    /**
     * Обновление текущего, грамматического уровня
     * @param lvl по типу A1L1, где A1 - первыйы блок, L1 - первый урок
     */
    public void updateCurrentLvl(int userId, String lvl){
        String curLvl = getCurrentLvl(userId);

        if (curLvl != null && curLvl.compareTo(lvl) < 0)
            queryExecutor.execUpdate(String.format(UPDATE_CURRENT_LVL_FOR_USER_QUERY, lvl, userId));
    }

    public String getCurrentLvl(int userId){
        return queryExecutor.execQuery(String.format(GET_CURRENT_LVL_FOR_USER_QUERY, userId),resultSet -> {
            if (resultSet.next())
                return resultSet.getString("cur_lvl");
            return null;
        });
    }

    private int getWordId(String word){
        return queryExecutor.execQuery(String.format(GET_WORD_ID_BY_WORD_QUERY, word), (rs) -> {
            if (rs.next())
                return rs.getInt("id");
            return -1;
        });
    }

    private int addNewWord(String word) {
        return queryExecutor.execQuery(String.format(ADD_NEW_WORD_QUERY, word), (resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        }));
    }

    private int getTranslationId(String translation){
        return queryExecutor.execQuery(String.format(GET_TRANSLATION_ID_BY_TRANSLATION_QUERY, translation), (rs) -> {
            if (rs.next())
                return rs.getInt("id");
            return -1;
        });
    }

    private int addNewTranslation(String translation) {
        return queryExecutor.execQuery(String.format(ADD_NEW_TRANSLATION_QUERY, translation), (resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        }));
    }

    private int getMainWordSetIdForUser(int userId){
        return queryExecutor.execQuery(String.format(GET_MAINWS_ID_FOR_USER_QUERY, userId), resultSet -> {
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
