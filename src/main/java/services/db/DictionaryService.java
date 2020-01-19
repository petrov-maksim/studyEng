package services.db;

import entities.Word;
import entities.WordSet;
import services.AbstractService;
import util.QueryExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class DictionaryService extends AbstractService {
    private final QueryExecutor queryExecutor;
    //Пользователь, которому будут принадлежать init слова и переводы
    private static final int BASE_USER_ID = 1;
    public DictionaryService(int threadsNum, QueryExecutor queryExecutor) {
        super(threadsNum);
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
     * @return id добавленного слова или -1
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

        queryExecutor.execUpdate(String.format("INSERT INTO user_word_translation (user_id, word_id, translation_id) VALUES ('%d', '%d', '%d');",
                userId, wordId, translationId));
        queryExecutor.execUpdate(String.format("INSERT INTO wordSet_word (wordSet_id, word_id) VALUES ('%d', '%d');",
                wordSetId, wordId));

        return wordId;
    }

    /**
     * Добавляем записи в wordSet_word
     * На фронтенде будет определенно максимальное количество слов, в сервлете, будет проверенно
     */
    public void addWordsToWordSet(Integer wordId[], int wordSetId){
        Connection con = queryExecutor.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO wordSet_word (wordSet_id, word_id) VALUES (?, ?);");
            for (Integer integer : wordId) {
                stmt.setInt(1, wordSetId);
                stmt.setInt(2, integer);
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
        String query = "WITH ids AS(SELECT word_id, translation_id FROM user_word_translation WHERE user_id = '%d' AND word_id IN " +
                "(SELECT word_id FROM wordSet_word WHERE wordSet_id = '%d' AND index >= '%d'))" +
                "SELECT ids.word_id, words.word, ids.translation_id, translations.translation FROM ids INNER JOIN words ON words.id = ids.word_id " +
                "INNER JOIN translations ON translations.id = ids.translation_id LIMIT '%d';";

        // "WITH words_id AS (SELECT word_id FROM wordSet_word WHERE wordSet_id = '%d' AND index >= '%d'), " +

        String testQuery =
                "WITH wt_ids AS(SELECT word_id, translation_id FROM user_word_translation WHERE user_id = '%d' AND word_id IN " +
                        "(SELECT word_id FROM wordSet_word WHERE wordSet_id = '%d' AND index >= '%d')), " +

                "we_ids AS(SELECT example_id, word_id FROM user_word_example WHERE user_id = '%d' AND word_id IN " +
                    "(SELECT word_id FROM wordSet_word WHERE wordSet_id = '%d' AND index >= '%d')) " +

                "SELECT wt_ids.word_id, words.word, wt_ids.translation_id, translations.translation, examples.example FROM " +

                "wt_ids INNER JOIN words ON words.id = wt_ids.word_id " +
                "INNER JOIN translations ON translations.id = wt_ids.translation_id " +
                "LEFT OUTER JOIN we_ids ON wt_ids.word_id = we_ids.word_id " +
                "LEFT OUTER JOIN examples ON examples.id = we_ids.example_id " +
                "LIMIT '%d';";

        return queryExecutor.execQuery(String.format(testQuery, userId, wordSetId, index, userId, wordSetId, index, num), (resultSet) -> {
            Map<Integer, Word> words = new HashMap<>();
            while (resultSet.next()){
                Word word;
                if (words.containsKey(resultSet.getInt("word_id")))
                    word = words.get(resultSet.getInt("word_id"));
                else {
                    word = new Word();
                    words.put(resultSet.getInt("word_id"), word);

                    word.setId(resultSet.getInt("word_id"));
                    word.setWord(resultSet.getString("word"));
                    word.setExample(resultSet.getString("example"));
                }

                word.getTranslations().add(resultSet.getString("translation"));
            }

            return words.values();
        });
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

    public void removeWordsForUser(Integer wordIds[], int userId){
        Connection con = queryExecutor.getConnection();
        String query1 = "DELETE FROM wordSet_word WHERE word_id = ? AND wordSet_id IN (SELECT id FROM wordSets WHERE user_id = ?);";
        String query2 = "DELETE FROM user_word_translation WHERE user_id = ? AND word_id = ?;";
        try {
            PreparedStatement stmt1 = con.prepareStatement(query1);
            PreparedStatement stmt2 = con.prepareStatement(query2);
            for (Integer wordId : wordIds) {
                stmt1.setInt(1, wordId);
                stmt1.setInt(2, userId);
                stmt1.addBatch();

                stmt2.setInt(1, userId);
                stmt2.setInt(2, wordId);
                stmt2.addBatch();
            }
            stmt1.executeBatch();
            stmt2.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTranslationForUser(int wordId, String translation, int userId){
        int count = queryExecutor.execQuery(String.format("SELECT count(*) FROM user_word_translation WHERE word_id = '%d' AND user_id = '%d';", wordId, userId), resultSet -> {
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
            queryExecutor.execUpdate(String.format("INSERT INTO user_word_translation (user_id, word_id, translation_id) VALUES ('%d','%d','%d');",
                    userId, wordId, translationId));
        }
    }

    public void removeTranslationForUser(int userId, int wordId, String translation){
        int translationId = getTranslationId(translation);

        if (translationId != -1)
            queryExecutor.execUpdate(String.format("DELETE FROM user_word_translation WHERE user_id = '%d' AND word_id = '%d' AND translation_id = '%d'",
                    userId, wordId, translationId));
    }

    /**
     * При добавлении слова, пользователю предлагаются возможные переводы
     * Если переводов > 4, будет содержать null
     */
    public String[] getTranslationsForWord(int wordId){
        return queryExecutor.execQuery(String.format("SELECT translation FROM translations WHERE id IN " +
                "(SELECT translation_id FROM user_word_translation WHERE word_id = '%d' AND user_id = '%d' LIMIT 4);", wordId, BASE_USER_ID), resultSet -> {
                    String translations[] = new String[4];
                    int count = 0;
                    while (resultSet.next())
                        translations[count++] = resultSet.getString("translation");
                    return translations;
                });
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

        queryExecutor.execUpdate(String.format("DELETE FROM user_word_example WHERE user_id = '%d' AND word_id = '%d'", userId, wordId));
        queryExecutor.execUpdate(String.format("INSERT INTO user_word_example (user_id, word_id, example_id) VALUES ('%d', '%d', '%d');",
                userId, wordId, exampleId));
    }


    /**
     * Добавляем запись в word_sets
     * @return id нового wordSet'a или -1 если ошибка
     */
    public int addWordSetForUser(String name, int userId){
        return queryExecutor.execQuery(String.format("INSERT INTO wordSets (name, user_id) VALUES ('%s', '%d') RETURNING id;", name, userId), resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        });
    }

    /**
     * Удаляем запись из wordSets
     * Удаляем все записи для данного wordSetId из wordSet_word
     * TODO: Добавить проверку на принадлежность wordSet'a
     */
    public void removeWordSet(int wordSetId){
        queryExecutor.execUpdate(String.format("DELETE FROM wordSets WHERE id = '%d';",  wordSetId));
    }

    /**
     * Изменяем название wordSet'a в word_sets
     * TODO: Добавить проверку на принадлежность wordSet'a
     */
    public void updateWordSetName(String newName, int wordSetId){
        queryExecutor.execUpdate(String.format("UPDATE wordSets SET name = '%s' WHERE id = '%d'", newName, wordSetId));
    }

    public List<WordSet> getWordSetsForUser(int userId){
        return queryExecutor.execQuery(String.format("SELECT id, name, size FROM wordSets WHERE user_id = '%d';", userId), resultSet -> {
            WordSet wordSet;
            List<WordSet> wordSets = new ArrayList<>();
            while (resultSet.next()){
                wordSet = new WordSet();
                wordSet.setName(resultSet.getString("name"));
                wordSet.setWordSetId(resultSet.getInt("id"));
                wordSet.setSize(resultSet.getInt("size"));

                wordSets.add(wordSet);
            }
            return wordSets;
        });
    }

    private int getWordId(String word){
        return queryExecutor.execQuery(String.format("SELECT id FROM words WHERE word = '%s';", word), (rs) -> {
            if (rs.next())
                return rs.getInt("id");
            return -1;
        });
    }

    private int addNewWord(String word) {
        return queryExecutor.execQuery(String.format("INSERT INTO words (word) VALUES('%s') RETURNING id;", word), (resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        }));
    }

    private int getTranslationId(String translation){
        return queryExecutor.execQuery(String.format("SELECT id FROM translations WHERE translation = '%s';", translation), (rs) -> {
            if (rs.next())
                return rs.getInt("id");
            return -1;
        });
    }

    private int addNewTranslation(String translation) {
        return queryExecutor.execQuery(String.format("INSERT INTO translations (translation) VALUES('%s') RETURNING id;", translation), (resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        }));
    }

    private int getExampleId(String example){
        return queryExecutor.execQuery(String.format("SELECT id FROM examples WHERE example = '%s';", example), (rs) -> {
            if (rs.next())
                return rs.getInt("id");
            return -1;
        });
    }

    private int addNewExample(String example){
        return queryExecutor.execQuery(String.format("INSERT INTO examples (example) VALUES('%s') RETURNING id;", example), (resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        }));
    }

    private int getMainWordSetIdForUser(int userId){
        return queryExecutor.execQuery(String.format("SELECT id FROM wordSets WHERE user_id = '%d' AND isMain = true;", userId), resultSet -> {
            if (resultSet.next())
                return resultSet.getInt("id");
            return -1;
        });
    }
}
