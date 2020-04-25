package services.db;

import entities.Word;
import entities.WordSet;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.apache.commons.fileupload.FileItem;
import util.QueryExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис, осуществляющий работу, связанную со словарем
 */
public class DictionaryService implements Abonent, Runnable {
    private final static Address address = new Address();
    private final QueryExecutor queryExecutor;
    private static final String UPLOAD_PATH = "C:/dev/pr/static/images/";
    private static final String DEFAULT_WORDSET_IMAGE = "C:/dev/pr/static/images/defaultWordSet.png";

    private static final int eng_rusId = 1;
    private static final int rus_engId = 2;
    private static final int writingId = 3;
    private static final int cardsId = 4;

    public DictionaryService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    private static final String WORD_ALREADY_EXISTS_QUERY = "SELECT COUNT(*) FROM wordSet_word WHERE wordSet_id = %d AND word_id = %d;";
    private static final String ADD_WORD_TO_WORDSET_QUERY = "INSERT INTO wordSet_word (wordSet_id, word_id) VALUES ('%d', '%d');";
    private static final String ADD_WORD_TO_WORDSET_QUERY_FOR_PREPAREDSTATEMENT = "INSERT INTO wordSet_word (wordSet_id, word_id) VALUES (?, ?);";
    private static final String ADD_TO_USER_WORD_TRANSLATION_QUERY = "INSERT INTO user_word_translation (user_id, word_id, translation_id) VALUES ('%d', '%d', '%d');";
    private static final String ADD_EXAMPLE_FOR_WORD_QUERY = "INSERT INTO user_word_example (user_id, word_id, example_id) VALUES ('%d', '%d', '%d');";
    private static final String ADD_WS_FOR_USER_QUERY = "INSERT INTO wordSets (name, user_id, img_path) VALUES ('%s', '%d', '%s') RETURNING id;";
    private static final String DELETE_TRANSLATION_FOR_WORD_QUERY = "DELETE FROM user_word_translation WHERE user_id = '%d' AND word_id = '%d' AND translation_id = '%d'";
    private static final String DELETE_EXAMPLE_FOR_WORD_QUERY = "DELETE FROM user_word_example WHERE user_id = '%d' AND word_id = '%d'";
    private static final String DELETE_FROM_WORDSET_WORD_BY_WS_ID_QUERY = "DELETE FROM wordSet_Word WHERE wordSet_id = '%d';";
    private static final String DELETE_FROM_WORDSET_WORD_QUERY = "DELETE FROM wordSet_word WHERE wordSet_id = ? AND word_id = ?;";
    private static final String DELETE_WORDSET_QUERY = "DELETE FROM wordSets WHERE id = '%d';";
    private static final String GET_NUMBER_OF_TRANSLATIONS_FOR_WORD_QUERY = "SELECT count(*) FROM user_word_translation WHERE word_id = '%d' AND user_id = '%d';";
    private static final String GET_WORDSETS_FOR_USER_QUERY = "SELECT id, name, isMain, size, img_path FROM wordSets WHERE user_id = '%d';";
    private static final String UPDATE_WS_NAME_QUERY = "UPDATE wordSets SET name = '%s' WHERE id = '%d';";
    private static final String UPDATE_WS_IMG_PATH_QUERY = "UPDATE wordSets SET img_path = '%s' WHERE id = '%d';";
    private static final String UPDATE_WS_IMG_PATH_AND_NAME_QUERY = "UPDATE wordSets SET name = '%s', img_path = '%s' WHERE id = '%d';";
    private static final String GET_WORD_ID_BY_WORD_QUERY = "SELECT id FROM words WHERE word = '%s';";
    private static final String ADD_NEW_WORD_QUERY = "INSERT INTO words (word) VALUES('%s') RETURNING id;";
    private static final String GET_TRANSLATION_ID_BY_TRANSLATION_QUERY = "SELECT id FROM translations WHERE translation = '%s';";
    private static final String ADD_NEW_TRANSLATION_QUERY = "INSERT INTO translations (translation) VALUES('%s') RETURNING id;";
    private static final String GET_EXAMPLE_ID_BY_EXAMPLE_QUERY = "SELECT id FROM examples WHERE example = '%s';";
    private static final String ADD_NEW_EXAMPLE_QUERY = "INSERT INTO examples (example) VALUES('%s') RETURNING id;";
    private static final String GET_MAINWS_ID_FOR_USER_QUERY = "SELECT id FROM wordSets WHERE user_id = '%d' AND isMain = true;";
    private static final String GET_WS_IMG_PATH_QUERY = "SELECT img_path FROM wordSets WHERE id = '%d';";
    private static final String GET_ALREADY_STORED_WORDS_QUERY = "SELECT word_id FROM wordSet_word WHERE wordSet_id = %d AND word_id IN (%s);";

    private static final String DELETE_WORD_FROM_USER_QUERY = "DELETE FROM user_word_translation WHERE user_id = ? AND word_id = ?;";
    private static final String DELETE_WORD_FROM_WORDSETS_QUERY = "DELETE FROM wordSet_word WHERE word_id = ? AND wordSet_id IN (SELECT id FROM wordSets WHERE user_id = ?);";
    private static final String DELETE_WORD_FROM_TRAININGS_QUERY = "DELETE FROM user_word_training WHERE user_id = ? AND word_id = ?;";
    private static final String GET_N_WORDS_FROM_WS_QUERY = "WITH wordId_index AS(SELECT word_id, index FROM wordSet_word WHERE wordSet_id = %d AND index > %d LIMIT %d),\n" +
            "wt_ids AS(SELECT word_id, translation_id FROM user_word_translation WHERE user_id = %d AND word_id IN\n" +
            "(SELECT word_id FROM wordId_index)),\n" +
            "we_ids AS(SELECT example_id, word_id FROM user_word_example WHERE user_id = %d AND word_id IN\n" +
            "(SELECT word_id FROM wordId_index))\n" +
            "SELECT wt_ids.word_id, words.word, wt_ids.translation_id, translations.translation, examples.example, wordId_index.index FROM\n" +
            "wordId_index INNER JOIN wt_ids ON wordId_index.word_id = wt_ids.word_id\n" +
            "INNER JOIN words ON words.id = wt_ids.word_id\n" +
            "INNER JOIN translations ON translations.id = wt_ids.translation_id\n" +
            "LEFT OUTER JOIN we_ids ON wt_ids.word_id = we_ids.word_id\n" +
            "LEFT OUTER JOIN examples ON examples.id = we_ids.example_id;";
    private static final String ADD_TO_USER_WORD_TRAINING_QUERY = "INSERT INTO user_word_training (user_id, word_id, training_id) VALUES('%d', '%d', '%d')," +
            "('%d', '%d', '%d')," +
            "('%d', '%d', '%d')," +
            "('%d', '%d', '%d');";

    /**
     * При первичном добавлении слова пользователю
     * Получаем translationId, если нет - addNewTranslation(translation);
     * Получаем wordId, если нет - addNewWord(word);
     * Добавляем запись в user_word_translation
     * Добавляем запись в wordSet_word
     *
     * @return id добавленного слова: 0 если слово уже существует; -1 в случае ошибки;
     */
    public int addWordForUser(String word, String translation, int userId){
        int translationId;
        int wordId;

        if ((translationId = getTranslationId(translation)) == -1)
            translationId = addNewTranslation(translation);

        if ((wordId = getWordId(word)) == -1)
            wordId = addNewWord(word);

        if (translationId == -1) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Didn't get translationId neither added new translation");
            return -1;
        }

        if (wordId == -1){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Didn't get wordId neither added new word");
            return -1;
        }

        int wordSetId = getMainWordSetIdForUser(userId);

        boolean wordAlreadyExists = queryExecutor.execQuery(String.format(WORD_ALREADY_EXISTS_QUERY,
                wordSetId, wordId), resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("COUNT") > 0;
            return false;
        });

        if (wordAlreadyExists)
            return 0;

        queryExecutor.execUpdate(String.format(ADD_WORD_TO_WORDSET_QUERY,
                wordSetId, wordId));
        queryExecutor.execUpdate(String.format(ADD_TO_USER_WORD_TRANSLATION_QUERY,
                userId, wordId, translationId));
        queryExecutor.execUpdate(String.format(ADD_TO_USER_WORD_TRAINING_QUERY, userId, wordId,
                eng_rusId, userId, wordId, rus_engId, userId, wordId, writingId, userId, wordId, cardsId));

        return wordId;
    }

    /**
     * Добавляет слова в набор слов
     * @param wordIds id'ники добавляемых слов
     * @param wordSetId id набора слов, в который добавляем
     */
    public void addWordsToWordSet(List<Integer> wordIds, int wordSetId){
        String str = wordIds.toString();
        List<Integer> alreadyStoredIds = new ArrayList<>();

        queryExecutor.execQuery(String.format(GET_ALREADY_STORED_WORDS_QUERY , wordSetId, str.substring(1,str.length() -1)),
                resultSet -> {

            if (resultSet.next())
                alreadyStoredIds.add(resultSet.getInt("word_id"));
            return alreadyStoredIds;
        });

        wordIds.removeAll(alreadyStoredIds);

        PreparedStatement stmt = queryExecutor.prepareStatement(ADD_WORD_TO_WORDSET_QUERY_FOR_PREPAREDSTATEMENT);
        for (Integer wordId : wordIds) {
            try{
                stmt.setInt(1, wordSetId);
                stmt.setInt(2, wordId);
                stmt.addBatch();
            }catch (SQLException e){
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            }
        }
        queryExecutor.executeBatch(stmt);
    }

    public Collection<Word> getNWordsForUser(int userId, int num, int index){
        int mainWordSetId = getMainWordSetIdForUser(userId);

        if(mainWordSetId != -1)
            return getNWordsFromWordSet(userId, mainWordSetId, num, index);

        return null;
    }

    public Collection<Word> getNWordsFromWordSet(int userId, int wordSetId, int num, int index) {
        return queryExecutor.execQuery(String.format(GET_N_WORDS_FROM_WS_QUERY, wordSetId, index, num, userId, userId), (resultSet) -> {
            Map<Integer, Word> words = new HashMap<>();
            while (resultSet.next()){
                Word word;
                if (words.containsKey(resultSet.getInt("word_id")))
                    word = words.get(resultSet.getInt("word_id"));
                else {
                    word = new Word();
                    word.setId(resultSet.getInt("word_id"));
                    word.setWord(resultSet.getString("word"));
                    word.setExample(resultSet.getString("example"));
                    word.setIndex(resultSet.getInt("index"));
                    words.put(word.getId(), word);
                }
                word.getTranslations().add(resultSet.getString("translation"));
            }
            return words.values();
        });
    }

    public void removeWordsFromWordSet(Integer wordIds[], int wordSetId){
        PreparedStatement stmt = queryExecutor.prepareStatement(DELETE_FROM_WORDSET_WORD_QUERY);
        for (Integer wordId : wordIds) {
            try {
                stmt.setInt(1, wordSetId);
                stmt.setInt(2, wordId);
                stmt.addBatch();
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            }
        }
        queryExecutor.executeBatch(stmt);
    }

    /**
     * Полностью удаляем слова для пользователя (из всех наборов слов)
     */
    public void removeWordsForUser(Integer wordIds[], int userId) {
        PreparedStatement stmt1 = queryExecutor.prepareStatement(DELETE_WORD_FROM_WORDSETS_QUERY);
        PreparedStatement stmt2 = queryExecutor.prepareStatement(DELETE_WORD_FROM_USER_QUERY);
        PreparedStatement stmt3 = queryExecutor.prepareStatement(DELETE_WORD_FROM_TRAININGS_QUERY);

        for (Integer wordId : wordIds) {
            try {
                stmt1.setInt(1, wordId);
                stmt1.setInt(2, userId);
                stmt1.addBatch();

                stmt2.setInt(1, userId);
                stmt2.setInt(2, wordId);
                stmt2.addBatch();

                stmt3.setInt(1, userId);
                stmt3.setInt(2, wordId);
                stmt3.addBatch();
            }catch (SQLException e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            }
            queryExecutor.executeBatch(stmt1);
            queryExecutor.executeBatch(stmt2);
            queryExecutor.executeBatch(stmt3);
        }
    }

    /**
     * Добавление перевода для слова.
     * Если количество переводов > 4, то не добавляем перевод
     */
    public void addTranslationForUser(int wordId, String translation, int userId){
        int count = queryExecutor.execQuery(String.format(GET_NUMBER_OF_TRANSLATIONS_FOR_WORD_QUERY, wordId, userId), resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("count");
            return -1;
        });

        if (count == -1 || count >= 4)
            return;

        int translationId;
        if ((translationId = getTranslationId(translation)) == -1)
            translationId = addNewTranslation(translation);

        if (translationId != -1) {
            queryExecutor.execUpdate(String.format(ADD_TO_USER_WORD_TRANSLATION_QUERY,
                    userId, wordId, translationId));
        }
    }

    public void removeTranslationForUser(int userId, int wordId, String translation){
        int translationId = getTranslationId(translation);

        if (translationId != -1)
            queryExecutor.execUpdate(String.format(DELETE_TRANSLATION_FOR_WORD_QUERY,
                    userId, wordId, translationId));
    }

    /**
     * Удаляем существующий пример (даже если нет)
     * Добавляем новый
     */
    public void addExampleForUser(int userId, int wordId, String example){
        int exampleId;

        if ((exampleId = getExampleId(example)) == -1)
            exampleId = addNewExample(example);

        if (exampleId == -1){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Didn't get exampleId neither added new example");
            return;
        }

        queryExecutor.execUpdate(String.format(DELETE_EXAMPLE_FOR_WORD_QUERY, userId, wordId));
        queryExecutor.execUpdate(String.format(ADD_EXAMPLE_FOR_WORD_QUERY,
                userId, wordId, exampleId));
    }

    /**
     * Добавялет новый набор слов
     * @return id нового wordSet'a или -1 если ошибка
     */
    public int addWordSetForUser(FileItem img, String name, int userId){
        String imgPath = img == null? DEFAULT_WORDSET_IMAGE : storeImage(img);
        return queryExecutor.execQuery(String.format(ADD_WS_FOR_USER_QUERY, name, userId, imgPath), resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        });
    }

    /**
     * Удаляем все записи для данного wordSetId из wordSet_word
     */
    public void removeWordSet(int wordSetId){
        queryExecutor.execUpdate(String.format(DELETE_FROM_WORDSET_WORD_BY_WS_ID_QUERY,  wordSetId));
        queryExecutor.execUpdate(String.format(DELETE_WORDSET_QUERY,  wordSetId));
    }

    /**
     * Обновление набора слов.
     * Обновлено может быть имя или обложка.
     */
    public void updateWordSet(int wordSetId, FileItem img, String name){
        String sql;
        if ((name == null || name.isBlank()) && img == null)
            return;
        else if (img == null)
            sql = String.format(UPDATE_WS_NAME_QUERY, name, wordSetId);
        else if (name == null || name.isBlank()) {
            removeWSImage(wordSetId);
            sql = String.format(UPDATE_WS_IMG_PATH_QUERY, storeImage(img), wordSetId);
        }
        else {
            removeWSImage(wordSetId);
            sql = String.format(UPDATE_WS_IMG_PATH_AND_NAME_QUERY, name, storeImage(img), wordSetId);
        }
        queryExecutor.execUpdate(sql);
    }

    /**
     * Получение всех наборов слов для пользователя
     */
    public List<WordSet> getWordSetsForUser(int userId){
        return queryExecutor.execQuery(String.format(GET_WORDSETS_FOR_USER_QUERY, userId), resultSet -> {
            WordSet wordSet;
            List<WordSet> wordSets = new ArrayList<>();
            while (resultSet.next()){
                wordSet = new WordSet();
                wordSet.setWordSetId(resultSet.getInt("id"));
                wordSet.setName(resultSet.getString("name"));
                wordSet.setMain(resultSet.getBoolean("isMain"));
                wordSet.setSize(resultSet.getInt("size"));
                try{
                    String path = resultSet.getString("img_path");
                    wordSet.setImage(Files.readAllBytes(Paths.get(path)));
                }catch (IOException e){
                    Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
                }

                wordSets.add(wordSet);
            }
            return wordSets;
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

    private int getExampleId(String example){
        return queryExecutor.execQuery(String.format(GET_EXAMPLE_ID_BY_EXAMPLE_QUERY, example), (rs) -> {
            if (rs.next())
                return rs.getInt("id");
            return -1;
        });
    }

    private int addNewExample(String example){
        return queryExecutor.execQuery(String.format(ADD_NEW_EXAMPLE_QUERY, example), (resultSet -> {
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

    private void removeWSImage(int wordSetId){
        String path = queryExecutor.execQuery(String.format(GET_WS_IMG_PATH_QUERY, wordSetId), resultSet -> {
            if (resultSet.next())
                return resultSet.getString("img_path");
            return null;
        });

        if (path != null) {
            try {
                Files.delete(Paths.get(path));
            } catch (IOException e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            }
        }
    }

    private String storeImage(FileItem img){
        String filePath = UPLOAD_PATH + System.currentTimeMillis() + ".jpg";
        File storeFile = new File(filePath);
        try{
            img.write(storeFile);
        }catch (Exception e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
        return filePath;
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
