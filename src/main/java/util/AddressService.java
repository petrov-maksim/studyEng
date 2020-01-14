package util;

import messageSystem.Address;
import services.db.AccountService;
import services.db.ContentService;
import services.db.DictionaryService;
import servlets.account.SignInServlet;
import servlets.account.SignUpServlet;
import servlets.content.ContentByIdServlet;
import servlets.content.ContentServlet;
import servlets.dictionary.AddTranslationServlet;
import servlets.dictionary.GetTranslationsForWordServlet;
import servlets.dictionary.user.AddWordForUserServlet;
import servlets.dictionary.wordSet.*;
import servlets.dictionary.user.GetWordsForUserServlet;
import servlets.dictionary.user.RemoveWordsForUserServlet;

public enum AddressService {
    INSTANCE();
    /**
     * All services
     */
    private static final Address accountService = new AccountService(1, null).getAddress();
    private static final Address dictionaryService = new DictionaryService(1, null).getAddress();
    private static final Address contentService = new ContentService(1, null).getAddress();

    /**
     * All Servlets
     */

    private static final Address signInServlet = SignInServlet.getAdr();
    private static final Address signUpServlet = SignUpServlet.getAdr();

    private static final Address contentServlet = ContentServlet.getAdr();

    private static final Address getWordForUserServlet = GetWordsForUserServlet.getAdr();
    private static final Address getWordFromWordSetServlet = GetWordsFromWordSet.getAdr();

    private static final Address removeWordForUserServlet = RemoveWordsForUserServlet.getAdr();
    private static final Address removeWordFromWordSetServlet = RemoveWordFromWordSetServlet.getAdr();

    private static final Address addWordForUserServlet = AddWordForUserServlet.getAdr();
    private static final Address addWordToWordSetServlet = AddWordsToWordSetServlet.getAdr();

    private static final Address getWordSetsServlet = GetWordSetsServlet.getAdr();
    private static final Address removeWordSetServlet = RemoveWordSetServlet.getAdr();
    private static final Address updateWordSetServlet = UpdateWordSetServlet.getAdr();
    private static final Address addWordSetServlet = AddWordSetServlet.getAdr();

    private static final Address addTranslationServlet = AddTranslationServlet.getAdr();
    private static final Address getTranslationsForWordServlet = GetTranslationsForWordServlet.getAdr();




    private static final Address contentByIdServlet = ContentByIdServlet.getAdr();

    public Address getAccountServiceAddress() {
        return accountService;
    }

    public Address getContentServiceAddress(){return contentService;}

    public Address getDictionaryService() {
        return dictionaryService;
    }

    public Address getSignInServletAddress(){
        return signInServlet;
    }

    public Address getSignUpServletAddress(){
        return signUpServlet;
    }

    public Address getContentServletAddress() {
        return contentServlet;
    }

    public Address getContentByIdServletAddress() {
        return contentByIdServlet;
    }

    public Address getGetWordForUserServletAddress() {
        return getWordForUserServlet;
    }

    public Address getGetWordFromWordSetServletAddress() {
        return getWordFromWordSetServlet;
    }

    public Address getRemoveWordForUserServletAddress() {
        return removeWordForUserServlet;
    }

    public Address getRemoveWordFromWordSetServletAddress() {
        return removeWordFromWordSetServlet;
    }

    public Address getAddWordForUserServletAddress() {
        return addWordForUserServlet;
    }

    public Address getAddWordToWordSetServletAddress() {
        return addWordToWordSetServlet;
    }

    public Address getGetWordSetsServletAddress() {
        return getWordSetsServlet;
    }

    public Address getRemoveWordSetServletAddress() {
        return removeWordSetServlet;
    }

    public Address getUpdateWordSetServletAddress() {
        return updateWordSetServlet;
    }

    public Address getAddTranslationServletAddress() {
        return addTranslationServlet;
    }

    public Address getAddWordSetServletAddress() {
        return addWordSetServlet;
    }

    public Address getGetTranslationsForWordServletAddress() {
        return getTranslationsForWordServlet;
    }
}
