package util;

import messageSystem.Address;
import services.db.AccountService;
import services.db.ContentService;
import services.db.DictionaryService;
import services.db.TrainingService;
import servlets.account.SignInServlet;
import servlets.account.SignUpServlet;
import servlets.content.GetContentByIdServlet;
import servlets.content.GetAllContentVideosServlet;
import servlets.dictionary.GetTranslationsForWordServlet;
import servlets.dictionary.wordSet.GetWordsFromWordSetServlet;
import servlets.dictionary.user.AddWordForUserServlet;
import servlets.dictionary.user.GetWordsForUserServlet;
import servlets.dictionary.wordSet.*;
import servlets.trainings.GetAmountOfUnlearnedWordsServlet;
import servlets.trainings.GetRandomTranslationsServlet;
import servlets.trainings.GetRandomWordsServlet;
import servlets.trainings.GetUnlearnedWordsServlet;

public enum AddressService {
    INSTANCE();
    /**
     * All services
     */

    private static final Address accountService = AccountService.getAdr();
    private static final Address dictionaryService = DictionaryService.getAdr();
    private static final Address contentService = ContentService.getAdr();
    private static final Address trainingService = TrainingService.getAdr();


    /**
     * All Servlets
     */

    private static final Address signInServlet = SignInServlet.getAdr();
    private static final Address signUpServlet = SignUpServlet.getAdr();

    private static final Address allContentVideosServlet = GetAllContentVideosServlet.getAdr();

    private static final Address getWordForUserServlet = GetWordsForUserServlet.getAdr();
    private static final Address getWordFromWordSetServlet = GetWordsFromWordSetServlet.getAdr();

    private static final Address addWordForUserServlet = AddWordForUserServlet.getAdr();

    private static final Address getWordSetsServlet = GetWordSetsServlet.getAdr();
    private static final Address addWordSetServlet = AddWordSetServlet.getAdr();

    private static final Address getTranslationsForWordServlet = GetTranslationsForWordServlet.getAdr();


    private static final Address contentByIdServlet = GetContentByIdServlet.getAdr();


    private static final Address getAmountOfUnlearnedWordsServlet = GetAmountOfUnlearnedWordsServlet.getAdr();
    private static final Address getRandomTranslationsServlet = GetRandomTranslationsServlet.getAdr();
    private static final Address getRandomWordsServlet = GetRandomWordsServlet.getAdr();
    private static final Address getUnlearnedWordsServlet = GetUnlearnedWordsServlet.getAdr();

    public Address getAccountServiceAddress() {
        return accountService;
    }

    public Address getContentServiceAddress(){return contentService;}

    public Address getDictionaryServiceAddress() {
        return dictionaryService;
    }

    public Address getTrainingServiceAddress() {
        return trainingService;
    }

    public Address getSignInServletAddress(){
        return signInServlet;
    }

    public Address getSignUpServletAddress(){
        return signUpServlet;
    }

    public Address getAllContentVideosServletAddress() {
        return allContentVideosServlet;
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

    public Address getAddWordForUserServletAddress() {
        return addWordForUserServlet;
    }

    public Address getGetWordSetsServletAddress() {
        return getWordSetsServlet;
    }

    public Address getAddWordSetServletAddress() {
        return addWordSetServlet;
    }

    public Address getGetTranslationsForWordServletAddress() {
        return getTranslationsForWordServlet;
    }

    public Address getGetAmountOfUnlearnedWordsServletAddress() {
        return getAmountOfUnlearnedWordsServlet;
    }

    public Address getGetRandomTranslationsServletAddress() {
        return getRandomTranslationsServlet;
    }

    public Address getGetRandomWordsServletAddress() {
        return getRandomWordsServlet;
    }

    public Address getGetUnlearnedWordsServletAddress() {
        return getUnlearnedWordsServlet;
    }
}
