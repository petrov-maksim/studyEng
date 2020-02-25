package services.db;

import entities.Training;
import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import util.QueryExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class TrainingService implements Abonent, Runnable  {
    private final QueryExecutor queryExecutor;

    private static int WORDS_LIMIT = 10;
    private static int RANDOM_LIMIT = 50;

    private static final int rus_engId = 1;
    private static final int eng_rusId = 2;
    private static final int writingId = 3;
    private static final int cardsId = 4;
    private final static Address address = new Address();

    public TrainingService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public Training[] getAmountOfUnlearnedWords(int userId){
        Training rus_eng = new Training(rus_engId);
        Training eng_rus = new Training(eng_rusId);
        Training writing = new Training(writingId);
        Training cards = new Training(cardsId);

        try {
            queryExecutor.execQuery(String.format("SELECT COUNT('word_id') FROM user_word_training WHERE user_id = %d AND training_id = %d AND isLearned = false;",
                    userId, eng_rusId), resultSet -> {
                if (resultSet.next())
                    eng_rus.setNumOfUnlearnedWords(resultSet.getInt("COUNT"));
                return null;
            });

            queryExecutor.execQuery(String.format("SELECT COUNT('word_id') FROM user_word_training WHERE user_id = %d AND training_id = %d AND isLearned = false;",
                    userId, rus_engId), resultSet -> {
                if (resultSet.next())
                    rus_eng.setNumOfUnlearnedWords(resultSet.getInt("COUNT"));
                return null;
            });

            queryExecutor.execQuery(String.format("SELECT COUNT('word_id') FROM user_word_training WHERE user_id = %d AND training_id = %d AND isLearned = false;",
                    userId, writingId), resultSet -> {
                if (resultSet.next())
                    writing.setNumOfUnlearnedWords(resultSet.getInt("COUNT"));
                return null;
            });

            queryExecutor.execQuery(String.format("SELECT COUNT('word_id') FROM user_word_training WHERE user_id = %d AND training_id = %d AND isLearned = false;",
                    userId, cardsId), resultSet -> {
                if (resultSet.next())
                    cards.setNumOfUnlearnedWords(resultSet.getInt("COUNT"));
                return null;
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Training[] {eng_rus, rus_eng, writing, cards};
    }

    public Collection<Word> getUnlearnedWordsForTraining(int trainingId, int userId){
        String query = "WITH wordIds AS(SELECT word_id FROM user_word_training WHERE user_id = %d AND training_id = %d AND isLearned = 'false' LIMIT %d),\n" +
                "translationIds AS(SELECT translation_id, word_id FROM user_word_translation WHERE user_id = %d AND word_id IN\n" +
                "(SELECT word_id FROM wordIds))\n" +

                "SELECT wordIds.word_id, words.word, translations.translation FROM\n" +

                "wordIds INNER JOIN translationIds ON wordIds.word_id = translationIds.word_id\n" +

                "INNER JOIN words ON wordIds.word_id = words.id\n" +
                "INNER JOIN translations ON translations.id = translationIds.translation_id;";

        try {
            return queryExecutor.execQuery(String.format(query, userId, trainingId, WORDS_LIMIT, userId), resultSet -> {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getRandomTranslations(){
        List<String> translations = new ArrayList<>(RANDOM_LIMIT);
        try {
            return queryExecutor.execQuery(String.format("SELECT translation FROM translations ORDER BY RANDOM() LIMIT %d;", RANDOM_LIMIT), resultSet -> {
                while(resultSet.next())
                    translations.add(resultSet.getString("translation"));
                return translations;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getRandomWords(){
        try {
            return queryExecutor.execQuery(String.format("SELECT word FROM words ORDER BY RANDOM() LIMIT %d;", RANDOM_LIMIT), resultSet -> {
                List<String> words = new ArrayList<>();

                while (resultSet.next())
                    words.add(resultSet.getString("word"));
                return words;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void moveWordsToLearned(int userId, List<Integer> wordIds, int trainingId){
        Connection con = queryExecutor.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement("UPDATE user_word_training SET isLearned = 'true' WHERE user_id = ? AND word_id = ? AND training_id = ?;");
            for (Integer wordId : wordIds) {
                stmt.setInt(1, userId);
                stmt.setInt(2, wordId);
                stmt.setInt(3, trainingId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveWordsToLearning(int userId, List<Integer> wordIds, int trainingId){
        Connection con = queryExecutor.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement("UPDATE user_word_training SET isLearned = 'false' WHERE user_id = ? AND word_Id = ? AND training_Id = ?;");
            for (Integer wordId : wordIds) {
                stmt.setInt(1, userId);
                stmt.setInt(2, wordId);
                stmt.setInt(3, trainingId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
