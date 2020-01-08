package services.db;

import entities.Word;
import entities.WordSet;
import util.QueryExecutor;

public class DictionaryService extends AbstractDBService {
    public DictionaryService(int threadsNum, QueryExecutor queryExecutor) {
        super(threadsNum, queryExecutor);
    }

    /**
     * В случае, если данного слова нет в базе
     */
    private void addNewWord(String word, String translation){

    }

    /**
     * При первичном добавлении слова
     * Получаем word's id, если нет, addNewWord() и addNewTranslation()
     * Получаем translation's id, если нет addNewTranslation()
     * Кладем запись в wordSet_word
     * Увеличиваем частоту использования translation
     */
    public void addWordForUser(String word, String translation, String sessionId){

    }

    /**
     * Для добавления слова в wordSet (из всех слов)
     * Получаем word's id
     * Получаем translation's id
     * Кладем запись в wordSet_word
     */
    public void addWordToWordSet(String word, String translation, int wordSetId){

    }

    /**
     * Для данного wordSetId получаем num слов, где индекс > index
     */
    public Word[] getNWordsFromWordSet(int wordSetId, int num, int index){
        return null;
    }

    /**
     * Получаем user's id
     * Для данного user's id получаем wordSet's id, где main_wordSet == true
     * Для данного wordSetId получаем num слов, где индекс > index
     */
    public Word[] getNWordsForUser(String sessionId, int num, int index){
        return null;
    }

    /**
     * Получаем translation's id, если нет, то addNewTranslation()
     * Получаем word's id
     * Получаем user's id
     * По user's id получаем список всех wordSet'ов
     * Для всех wordSet'ов и word's id добавляем translation's id
     */
    public void addTranslation(String word, String translation, String sessionId){

    }

    /**
     * В случае, если для данного слова нет перевода в базе
     */
    private void addNewTranslation(String word, String translation){

    }

    /**
     * Получаем translation's id, если нет, то addNewTranslation()
     * Получаем word's id
     * Получаем user's id
     * По user's id получаем список всех wordSet'ов
     * Для всех wordSet'ов и word's id удаляем translation's id из переводов
     */
    public void removeTranslationForUser(String word, String translation, String sessionId){

    }

    /**
     * Получаем user's id
     * По user's id получаем список всех wordSet's id
     * Для всех wordSet'ов, для данного word's id добавляем, изменяем example
     */
    public void addExample(String word, String example, String sessionId){

    }


    /**
     * Удаление из набора
     * Получаем word's id
     * Удаляем запись для данного wordSetId и word's id из wordSet_word
     */
    public void removeWordFromWordSet(String word, int wordSetId){

    }

    /**
     * Удаление из всех слов
     * Получаем user's id
     * По user's id получаем список всех wordSet's id
     * Получаем word's id
     * В wordSet_word удаляем все записи для данного word's id
     */
    public void removeWordForUser(String word, String sessionId){

    }

    /**
     * При добавлении слова, пользователю предлагаются возможные переводы
     * Получаем word's id
     * По word's id получаем переводы, если есть
     * Отправляем с учетом частотности
     */
    public String[] getTranslationsForWord(String word){
        return new String[4];
    }

    /**
     * Добавляем запись в word_sets
     * @return id нового wordSet'a
     */
    public int addWordSet(String name){
        return 1;
    }

    /**
     * Удаляем запись из wordSets
     * Удаляем все записи для данного wordSetId из wordSet_word
     */
    public void removeWordSet(int wordSetId){

    }

    /**
     * Изменяем название wordSet'a в word_sets
     */
    public void changeWordSetName(String name, int wordSetId){

    }

    /**
     * Получаем user's id
     * Для user's id получаем список всех wordSet'ов
     * И возвращаем
     */
    public WordSet[] getWordSetsForUser(String sessionId){
        return null;
    }
}
