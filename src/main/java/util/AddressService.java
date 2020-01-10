package util;

import messageSystem.Address;
import services.db.AccountService;
import services.db.ContentService;
import services.db.DictionaryService;
import servlets.account.SignInServlet;
import servlets.account.SignUpServlet;
import servlets.content.ContentByIdServlet;
import servlets.content.ContentServlet;
import servlets.dictionary.user.AddWordsForUserServlet;
import servlets.dictionary.wordSet.AddWordsToWordSetServlet;
import servlets.dictionary.user.GetWordForUserServlet;
import servlets.dictionary.user.RemoveWordForUserServlet;

public enum AddressService {
    INSTANCE();
    /**
     * All services
     */
    private static final Address accountService = new AccountService(1, null).getAddress();
    private static final Address dictionaryService = new DictionaryService(1, null).getAddress();
//    private static final Abonent grammarService = new GrammarService(1, null);
//    private static final Abonent trainingsService = new TrainingsService(1, null);
    private static final Address contentService = new ContentService(1, null).getAddress();

    /**
     * All Servlets
     */

    private static final Address signInServlet = SignInServlet.getAdr();
    private static final Address signUpServlet = SignUpServlet.getAdr();
    private static final Address contentServlet = ContentServlet.getAdr();
    private static final Address getWordServlet = GetWordForUserServlet.getAdr();
    private static final Address removeWordServlet = RemoveWordForUserServlet.getAdr();
    private static final Address addWordForUserServlet = AddWordsForUserServlet.getAdr();
    private static final Address addWordToWordSetServlet = AddWordsToWordSetServlet.getAdr();


    private static final Address contentByIdServlet = ContentByIdServlet.getAdr();

    public Address getAccountServiceAddress() {
        return accountService;
    }

    public Address getContentServiceAddress(){return contentService;}

    public Address getDictionaryService() {
        return dictionaryService;
    }

//    public Address getRegistryService() {
//        return registryService.getAddress();
//    }
//
//    public Address getDictionaryService() {
//        return dictionaryService.getAddress();
//    }
//
//    public Address getGrammarService() {
//        return grammarService.getAddress();
//    }
//
//    public Address getTrainingsService() {
//        return trainingsService.getAddress();
//    }
//
//    public Address getContentService() {
//        return contentService.getAddress();
//    }
//
//    public Address getAuthRH() {
//        return authRH.getAddress();
//    }
//
//    public Address getRegistryRH() {
//        return registryRH.getAddress();
//    }
//
//    public Address getContentRH() {
//        return contentRH.getAddress();
//    }
//
//    public Address getDictionaryRH() {
//        return dictionaryRH.getAddress();
//    }
//
//    public Address getGrammarRH() {
//        return grammarRH.getAddress();
//    }
//
//    public Address getTrainingsRH() {
//        return trainingsRH.getAddress();
//    }

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

    public Address getGetWordServletAddress() {
        return getWordServlet;
    }

    public Address getRemoveWordServletAddress() {
        return removeWordServlet;
    }

    public Address getAddWordForUserServletAddress() {
        return addWordForUserServlet;
    }

    public Address getAddWordToWordSetServletAddress() {
        return addWordToWordSetServlet;
    }
}
