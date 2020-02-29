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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class DictionaryService implements Abonent, Runnable {
    private final static Address address = new Address();
    private final QueryExecutor queryExecutor;
    //Пользователь, которому будут принадлежать init слова и переводы
    private static final int BASE_USER_ID = 1;
    private static final String UPLOAD_PATH = "C:/dev/pr/static/images/";
    private static final String DEFAULT_WORDSET_IMAGE = "C:/dev/pr/static/images/defaultWordSet.png";

    private static final int eng_rusId = 1;
    private static final int rus_engId = 2;
    private static final int writingId = 3;
    private static final int cardsId = 4;
    public DictionaryService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    /**
     * При первичном добавлении слова пользователю
     * Получаем translationId, если нет - addNewTranslation(translation);
     * Получаем wordId, если нет - addNewWord(word);
     * Добавляем запись в user_word_translation
     * Добавляем запись в wordSet_word
     * TODO: Инкрементируем size в таблице wordSets для данного wordSetId (ДОБАВИТЬ ТРИГЕР)
     *
     * @return :
     *  id добавленного слова
     *  0 если слово уже существует
     *  -1 в противном случае
     *
     */
    public int addWordForUser(String word, String translation, int userId){
        int translationId;
        int wordId;

        if ((translationId = getTranslationId(translation)) == -1)
            translationId = addNewTranslation(translation);

        if ((wordId = getWordId(word)) == -1)
            wordId = addNewWord(word);

        if (translationId == -1 || wordId == -1)
            return -1;

        int wordSetId = getMainWordSetIdForUser(userId);

        try{
            boolean wordAlreadyExists = queryExecutor.execQuery(String.format("SELECT COUNT(*) FROM wordSet_word WHERE wordSet_id = %d AND word_id = %d;",
                    wordSetId, wordId), resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("COUNT") > 0;
                return false;
            });

            if (wordAlreadyExists)
                return 0;
            queryExecutor.execUpdate(String.format("INSERT INTO wordSet_word (wordSet_id, word_id) VALUES ('%d', '%d');",
                    wordSetId, wordId));
            queryExecutor.execUpdate(String.format("INSERT INTO user_word_translation (user_id, word_id, translation_id) VALUES ('%d', '%d', '%d');",
                    userId, wordId, translationId));
            queryExecutor.execUpdate(String.format("INSERT INTO user_word_training (user_id, word_id, training_id) VALUES('%d', '%d', '%d')," +
                    "('%d', '%d', '%d')," +
                    "('%d', '%d', '%d')," +
                    "('%d', '%d', '%d');", userId, wordId, eng_rusId, userId, wordId, rus_engId, userId, wordId, writingId, userId, wordId, cardsId));
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }

        return wordId;
    }

    /**
     * Добавляем записи в wordSet_word
     * На фронтенде будет определенно максимальное количество слов, в сервлете, будет проверенно
     */
    public void addWordsToWordSet(List<Integer> wordIds, int wordSetId){
        String str = wordIds.toString();
        List<Integer> alreadyStoredIds = new ArrayList<>();
        try {
            queryExecutor.execQuery(String.format("SELECT word_id FROM wordSet_word WHERE wordSet_id = %d AND word_id IN ("
                    + str.substring(1,str.length() -1) +");", wordSetId), resultSet -> {

                    if (resultSet.next())
                        alreadyStoredIds.add(resultSet.getInt("word_id"));
                    return alreadyStoredIds;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        wordIds.removeAll(alreadyStoredIds);

        Connection con = queryExecutor.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO wordSet_word (wordSet_id, word_id) VALUES (?, ?);");
            for (Integer wordId : wordIds) {
                stmt.setInt(1, wordSetId);
                stmt.setInt(2, wordId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Word> getNWordsForUser(int userId, int num, int index){
        int mainWordSetId = getMainWordSetIdForUser(userId);

        if(mainWordSetId != -1)
            return getNWordsFromWordSet(userId, mainWordSetId, num, index);

        return null;
    }

    public Collection<Word> getNWordsFromWordSet(int userId, int wordSetId, int num, int index) {
        String testQuery = "WITH wordId_index AS(SELECT word_id, index FROM wordSet_word WHERE wordSet_id = %d AND index > %d LIMIT %d),\n" +
                "wt_ids AS(SELECT word_id, translation_id FROM user_word_translation WHERE user_id = %d AND word_id IN\n" +
                "(SELECT word_id FROM wordId_index)),\n" +
                "we_ids AS(SELECT example_id, word_id FROM user_word_example WHERE user_id = %d AND word_id IN\n" +
                "(SELECT word_id FROM wordId_index))\n" +
                "SELECT wt_ids.word_id, words.word, wt_ids.translation_id, translations.translation, examples.example, wordId_index.index FROM\n" +
                "\n" +
                "wordId_index INNER JOIN wt_ids ON wordId_index.word_id = wt_ids.word_id\n" +
                "INNER JOIN words ON words.id = wt_ids.word_id\n" +
                "INNER JOIN translations ON translations.id = wt_ids.translation_id\n" +
                "LEFT OUTER JOIN we_ids ON wt_ids.word_id = we_ids.word_id\n" +
                "LEFT OUTER JOIN examples ON examples.id = we_ids.example_id;";
        try {
            return queryExecutor.execQuery(String.format(testQuery, wordSetId, index, num, userId, userId), (resultSet) -> {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeWordsFromWordSet(Integer wordIds[], int wordSetId){
        Connection con = queryExecutor.getConnection();
        String query = "DELETE FROM wordSet_word WHERE wordSet_id = ? AND word_id = ?;";
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            for (Integer wordId : wordIds) {
                stmt.setInt(1, wordSetId);
                stmt.setInt(2, wordId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWordsForUser(Integer wordIds[], int userId) {
        Connection con = queryExecutor.getConnection();
        String query1 = "DELETE FROM wordSet_word WHERE word_id = ? AND wordSet_id IN (SELECT id FROM wordSets WHERE user_id = ?);";
        String query2 = "DELETE FROM user_word_translation WHERE user_id = ? AND word_id = ?;";
        String query3 = "DELETE FROM user_word_training WHERE user_id = ? AND word_id = ?;";
        try {
            PreparedStatement stmt1 = con.prepareStatement(query1);
            PreparedStatement stmt2 = con.prepareStatement(query2);
            PreparedStatement stmt3 = con.prepareStatement(query3);
            for (Integer wordId : wordIds) {
                stmt1.setInt(1, wordId);
                stmt1.setInt(2, userId);
                stmt1.addBatch();

                stmt2.setInt(1, userId);
                stmt2.setInt(2, wordId);
                stmt2.addBatch();

                stmt3.setInt(1, userId);
                stmt3.setInt(2, wordId);
                stmt3.addBatch();
            }
            stmt1.executeBatch();
            stmt2.executeBatch();
            stmt3.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTranslationForUser(int wordId, String translation, int userId){
        int count = 0;
        try {
            count = queryExecutor.execQuery(String.format("SELECT count(*) FROM user_word_translation WHERE word_id = '%d' AND user_id = '%d';", wordId, userId), resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("count");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count == -1 || count >= 4)
            return;

        int translationId;
        if ((translationId = getTranslationId(translation)) == -1)
            translationId = addNewTranslation(translation);

        if (translationId != -1) {
            try {
                queryExecutor.execUpdate(String.format("INSERT INTO user_word_translation (user_id, word_id, translation_id) VALUES ('%d','%d','%d');",
                        userId, wordId, translationId));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeTranslationForUser(int userId, int wordId, String translation){
        int translationId = getTranslationId(translation);

        if (translationId != -1) {
            try {
                queryExecutor.execUpdate(String.format("DELETE FROM user_word_translation WHERE user_id = '%d' AND word_id = '%d' AND translation_id = '%d'",
                        userId, wordId, translationId));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * При добавлении слова, пользователю предлагаются возможные переводы
     * Если переводов > 4, будет содержать null
     */
    public String[] getTranslationsForWord(int wordId){
        try {
            return queryExecutor.execQuery(String.format("SELECT translation FROM translations WHERE id IN " +
                    "(SELECT translation_id FROM user_word_translation WHERE word_id = '%d' AND user_id = '%d' LIMIT 4);", wordId, BASE_USER_ID), resultSet -> {
                        String translations[] = new String[4];
                        int count = 0;
                        while (resultSet.next())
                            translations[count++] = resultSet.getString("translation");
                        return translations;
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Логика работы = затирание текущего примера и запись пеерданного
     */
    public void addExampleForUser(int userId, int wordId, String example){
        int exampleId;

        if ((exampleId = getExampleId(example)) == -1)
            exampleId = addNewExample(example);

        if (exampleId == -1)
            return;

        try{
            queryExecutor.execUpdate(String.format("DELETE FROM user_word_example WHERE user_id = '%d' AND word_id = '%d'", userId, wordId));
            queryExecutor.execUpdate(String.format("INSERT INTO user_word_example (user_id, word_id, example_id) VALUES ('%d', '%d', '%d');",
                    userId, wordId, exampleId));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * Добавляем запись в word_sets
     * @return id нового wordSet'a или -1 если ошибка
     */
    public int addWordSetForUser(FileItem img, String name, int userId){
        String imgPath = img == null? DEFAULT_WORDSET_IMAGE : storeImage(img);
        try {
            return queryExecutor.execQuery(String.format("INSERT INTO wordSets (name, user_id, img_path) VALUES ('%s', '%d', '%s') RETURNING id;", name, userId, imgPath), resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Удаляем запись из wordSets
     * Удаляем все записи для данного wordSetId из wordSet_word
     * TODO: Добавить проверку на принадлежность wordSet'a
     */
    public void removeWordSet(int wordSetId){
        try {
            queryExecutor.execUpdate(String.format("DELETE FROM wordSet_Word WHERE wordSet_id = '%d';",  wordSetId));
            queryExecutor.execUpdate(String.format("DELETE FROM wordSets WHERE id = '%d';",  wordSetId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Изменяем название wordSet'a в word_sets
     * TODO: Добавить проверку на принадлежность wordSet'a
     */
    public void updateWordSet(int wordSetId, FileItem img, String name){
        String sql;
        if ((name == null || name.isBlank()) && img == null)
            return;
        else if (img == null)
            sql = String.format("UPDATE wordSets SET name = '%s' WHERE id = '%d';", name, wordSetId);
        else if (name == null || name.isBlank()) {
            removeWSImage(wordSetId);
            sql = String.format("UPDATE wordSets SET img_path = '%s' WHERE id = '%d';", storeImage(img), wordSetId);
        }
        else {
            removeWSImage(wordSetId);
            sql = String.format("UPDATE wordSets SET name = '%s', img_path = '%s' WHERE id = '%d';",name, storeImage(img), wordSetId);
        }
        try {
            queryExecutor.execUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<WordSet> getWordSetsForUser(int userId){
        try {
            return queryExecutor.execQuery(String.format("SELECT id, name, isMain, size, img_path FROM wordSets WHERE user_id = '%d';", userId), resultSet -> {
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
                        e.printStackTrace();
                    }

                    wordSets.add(wordSet);
                }
                return wordSets;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getWordId(String word){
        try {
            return queryExecutor.execQuery(String.format("SELECT id FROM words WHERE word = '%s';", word), (rs) -> {
                if (rs.next())
                    return rs.getInt("id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int addNewWord(String word) {
        try {
            return queryExecutor.execQuery(String.format("INSERT INTO words (word) VALUES('%s') RETURNING id;", word), (resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("id");
                return -1;
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getTranslationId(String translation){
        try {
            return queryExecutor.execQuery(String.format("SELECT id FROM translations WHERE translation = '%s';", translation), (rs) -> {
                if (rs.next())
                    return rs.getInt("id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int addNewTranslation(String translation) {
        try {
            return queryExecutor.execQuery(String.format("INSERT INTO translations (translation) VALUES('%s') RETURNING id;", translation), (resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("id");
                return -1;
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getExampleId(String example){
        try {
            return queryExecutor.execQuery(String.format("SELECT id FROM examples WHERE example = '%s';", example), (rs) -> {
                if (rs.next())
                    return rs.getInt("id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int addNewExample(String example){
        try {
            return queryExecutor.execQuery(String.format("INSERT INTO examples (example) VALUES('%s') RETURNING id;", example), (resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("id");
                return -1;
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getMainWordSetIdForUser(int userId){
        try {
            return queryExecutor.execQuery(String.format("SELECT id FROM wordSets WHERE user_id = '%d' AND isMain = true;", userId), resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void removeWSImage(int wordSetId){
        try {
            String path = queryExecutor.execQuery(String.format("SELECT img_path FROM wordSets WHERE id = '%d';",wordSetId), resultSet -> {
                if (resultSet.next())
                    return resultSet.getString("img_path");
                return null;
            });

            if (path != null)
                Files.delete(Paths.get(path));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private String storeImage(FileItem img){
        String filePath = UPLOAD_PATH + System.currentTimeMillis() + ".jpg";
        File storeFile = new File(filePath);
        try{
            img.write(storeFile);
//            InputStream is = img.getInputStream();
//            BufferedImage image = ImageIO.read(is);
//
//            ImageIO.write(image, "jpg", storeFile);
        }catch (Exception e){
            e.printStackTrace();
            return null;
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
                e.printStackTrace();
            }
        }
    }
}
