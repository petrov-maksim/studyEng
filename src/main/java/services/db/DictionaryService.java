package services.db;

import entities.Word;
import entities.WordSet;
import util.QueryExecutor;

import java.util.Map;

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
     * Узнаем main_wordSet's id для данного пользователя
     * Для этого wordSet'а кладем запись в wordSet_word
     * Увеличиваем частоту использования translation
     */
    public int addWordForUser(String word, String translation, int userId){
        return 1;
    }

    /**
     * Проверить, что для данного wordId и wordSetId уже нет записей в wordSet_word
     *
     * Узнаем main_wordSet's id для данного пользователя (в word_sets)
     * Для полученного main_wordSet's id берем все данные (кроме wordSet_id и isLearned) из wordSet_word, где wordId == word_id
     * Все эти данные кладем в wordSet_word с переданным wordSetId
     * Должен получиться дубликат, в котором отличаются лишь wordSet_id и isLearned
     */
    public void addWordToWordSet(Integer wordId[], int userId, int wordSetId){
        System.out.println("adding words to wordSet: " + wordSetId);
    }

    /**
     * Для данного wordSetId получаем num слов, где индекс > index
     */
    public Word[] getNWordsFromWordSet(int wordSetId, int num, int index){
        return null;
    }

    /**
     * Для данного user's id получаем wordSet's id, где main_wordSet == true
     * Для данного wordSetId получаем num слов, где индекс > index
     */
    public Word[] getNWordsForUser(int userId, int num, int index){
        return null;
    }

    /**
     * Удаление из набора
     * Получаем word's id
     * Удаляем запись для данного wordSetId и word's id из wordSet_word
     */
    public boolean removeWordFromWordSet(Integer wordIds[], int wordSetId){
        System.out.println("Remove from wordSet");
        return false;
    }

    /**
     * Удаление из всех слов
     * Получаем user's id
     * По user's id получаем список всех wordSet's id
     * Получаем word's id
     * В wordSet_word удаляем все записи для данного word's id
     */
    public boolean removeWordForUser(Integer wordIds[], int userId){
        System.out.println("Remove for user");
        return false;
    }

    /**
     * Получаем translation's id, если нет, то addNewTranslation()
     * Получаем word's id
     * Получаем user's id
     * По user's id получаем список всех wordSet'ов
     * Для всех wordSet'ов и word's id добавляем translation's id
     * @return id добавленного перевода или -1, если не смог добавить
     */
    public int addTranslation(int word, String translation, int userId){
        return -1;
    }

    /**
     * Получаем translation's id, если нет, то addNewTranslation()
     * Получаем word's id
     * Получаем user's id
     * По user's id получаем список всех wordSet'ов
     * Для всех wordSet'ов и word's id удаляем translation's id из переводов
     */
    public void removeTranslation(int userId, int wordId, int translationId){

    }

    public void updateTranslation(int userId, int wordId, int oldTranslationId, String newTranslation){

    }

    /**
     * При добавлении слова, пользователю предлагаются возможные переводы
     * Получаем word's id
     * По word's id получаем переводы, если есть
     * Отправляем с учетом частотности
     */
    public Map<Integer, String> getTranslationsForWord(int wordId){
        return null;
    }

    /**
     * Логика работы = затирание текущего примера и запись пеерданного
     * По user's id получаем список всех wordSet's id
     * Для всех wordSet'ов, для данного word's id добавляем, изменяем example
     */
    public void addExample(int userId, int wordId, String example){

    }

    /**
     * Добавляем запись в word_sets
     * @return id нового wordSet'a или -1 если ошибка
     */
    public int addWordSet(String name, int userId){
        return -1;
    }

    /**
     * Удаляем запись из wordSets
     * Удаляем все записи для данного wordSetId из wordSet_word
     */
    public boolean removeWordSet(int userId, int wordSetId){
        return true;
    }

    /**
     * Изменяем название wordSet'a в word_sets
     */
    public boolean updateWordSetName(String newName, int userId, int wordSetId){
        return true;
    }

    /**
     * Получаем user's id
     * Для user's id получаем список всех wordSet'ов
     * И возвращаем
     */
    public WordSet[] getWordSetsForUser(int userId){
        return null;
    }



    /**
     * В случае, если для данного слова нет перевода в базе
     */
    private void addNewTranslation(String word, String translation){

    }
}
