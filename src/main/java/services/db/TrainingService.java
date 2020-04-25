package services.db;

import entities.Training;
import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import util.QueryExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис, осуществляющий работу, связанную с разделом тренировок
 */
public class TrainingService implements Abonent, Runnable  {
    private final static Address address = new Address();
    private final QueryExecutor queryExecutor;

    public TrainingService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    private static int WORDS_LIMIT = 10;
    private static int RANDOM_LIMIT = 50;
    private static final int rus_engId = 1;
    private static final int eng_rusId = 2;
    private static final int writingId = 3;
    private static final int cardsId = 4;

    private static final String GET_NUM_OF_UNLEARNED_WORDS_QUERY = "SELECT training_id, COUNT('word_id') FROM user_word_training WHERE user_id = %d AND isLearned = false group by training_id;";
    private static final String GET_UNLEARNED_WORDS_FOR_TRAINING_QUERY =
            "WITH wordIds AS(SELECT word_id FROM user_word_training WHERE user_id = %d AND training_id = %d AND isLearned = 'false' LIMIT %d),\n" +
            "translationIds AS(SELECT translation_id, word_id FROM user_word_translation WHERE user_id = %d AND word_id IN\n" +
            "(SELECT word_id FROM wordIds))\n" +
            "SELECT wordIds.word_id, words.word, translations.translation FROM\n" +
            "wordIds INNER JOIN translationIds ON wordIds.word_id = translationIds.word_id\n" +
            "INNER JOIN words ON wordIds.word_id = words.id\n" +
            "INNER JOIN translations ON translations.id = translationIds.translation_id;";
    private static final String GET_RANDOM_WORDS_QUERY = "SELECT word FROM words ORDER BY RANDOM() LIMIT %d;";
    private static final String GET_RANDOM_TRANSLATIONS_QUERY = "SELECT translation FROM translations ORDER BY RANDOM() LIMIT %d;";
    private static final String MOVE_WORDS_TO_LEARNED_QUERY = "UPDATE user_word_training SET isLearned = 'true' WHERE user_id = ? AND word_id = ? AND training_id = ?;";
    private static final String MOVE_WORDS_TO_LEARNING_QUERY = "UPDATE user_word_training SET isLearned = 'false' WHERE user_id = ? AND word_Id = ? AND training_Id = ?;";

    /**
     * Получение количества неизученных слов
     */
    public Collection<Training> getAmountOfUnlearnedWords(int userId){
        HashMap<Integer, Training> trainingsMap = new HashMap<>();
        trainingsMap.put(rus_engId, new Training(rus_engId));
        trainingsMap.put(eng_rusId, new Training(eng_rusId));
        trainingsMap.put(writingId, new Training(writingId));
        trainingsMap.put(cardsId, new Training(cardsId));

        queryExecutor.execQuery(String.format(GET_NUM_OF_UNLEARNED_WORDS_QUERY,
                userId), resultSet -> {
            while (resultSet.next())
                trainingsMap.get(resultSet.getInt("training_id")).setNumOfUnlearnedWords(resultSet.getInt("COUNT"));
            return null;
        });
        return trainingsMap.values();
    }

    /**
     * Получение неизученных слов для конкретной тренировки
     */
    public Collection<Word> getUnlearnedWordsForTraining(int trainingId, int userId){
        return queryExecutor.execQuery(String.format(GET_UNLEARNED_WORDS_FOR_TRAINING_QUERY, userId, trainingId, WORDS_LIMIT, userId), resultSet -> {
            Map<Integer, Word> words = new HashMap<>(WORDS_LIMIT);
            while (resultSet.next()){
                Word word;
                if (words.containsKey(resultSet.getInt("word_id")))
                    word = words.get(resultSet.getInt("word_id"));
                else {
                    word = new Word();
                    word.setId(resultSet.getInt("word_id"));
                    word.setWord(resultSet.getString("word"));

                    words.put(word.getId(), word);
                }
                word.getTranslations().add(resultSet.getString("translation"));
            }

            return words.values();
        });
    }

    /**
     * Получение случайных переводов
     */
    public List<String> getRandomTranslations(){
        List<String> translations = new ArrayList<>(RANDOM_LIMIT);
        return queryExecutor.execQuery(String.format(GET_RANDOM_TRANSLATIONS_QUERY, RANDOM_LIMIT), resultSet -> {
            while(resultSet.next())
                translations.add(resultSet.getString("translation"));
            return translations;
        });
    }

    /**
     * Получение случайных слов
     */
    public List<String> getRandomWords(){
        return queryExecutor.execQuery(String.format(GET_RANDOM_WORDS_QUERY, RANDOM_LIMIT), resultSet -> {
            List<String> words = new ArrayList<>();

            while (resultSet.next())
                words.add(resultSet.getString("word"));
            return words;
        });
    }

    /**
     * перевод слов в состояние "изученно", для конкретной тренировки
     */
    public void moveWordsToLearned(int userId, List<Integer> wordIds, int trainingId){
        PreparedStatement stmt = queryExecutor.prepareStatement(MOVE_WORDS_TO_LEARNED_QUERY);

        for (Integer wordId : wordIds) {
            try {
                stmt.setInt(1, userId);
                stmt.setInt(2, wordId);
                stmt.setInt(3, trainingId);
                stmt.addBatch();
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            }
        }
        queryExecutor.executeBatch(stmt);
    }

    /**
     * перевод слов в состояние "на изучении", для конкретной тренировки
     */
    public void moveWordsToLearning(int userId, List<Integer> wordIds, int trainingId){
        PreparedStatement stmt = queryExecutor.prepareStatement(MOVE_WORDS_TO_LEARNING_QUERY);
        for (Integer wordId : wordIds) {
            try {
                stmt.setInt(1, userId);
                stmt.setInt(2, wordId);
                stmt.setInt(3, trainingId);
                stmt.addBatch();
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            }
        }
        queryExecutor.executeBatch(stmt);
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
