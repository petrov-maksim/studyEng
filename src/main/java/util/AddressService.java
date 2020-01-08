package util;

import Test.TestServlet;
import messageSystem.Abonent;
import messageSystem.Address;
import services.db.AccountService;
import services.db.ContentService;
import servlets.account.SignInServlet;
import servlets.account.SignUpServlet;
import servlets.content.ContentByIdServlet;
import servlets.content.ContentServlet;

public enum AddressService {
    INSTANCE();
    /**
     * All services
     */
    private static final Address accountService = new AccountService(1, null).getAddress();
//    private static final Abonent dictionaryService = new DictionaryService(1, null);
//    private static final Abonent grammarService = new GrammarService(1, null);
//    private static final Abonent trainingsService = new TrainingsService(1, null);
    private static final Address contentService = new ContentService(1, null).getAddress();

    /**
     * All ResponseHandlers
     */
//    private static final Abonent authRH = new AuthRH(1);
//    private static final Abonent registryRH = new RegistrationRH(1);
//    private static final Abonent contentRH = new ContentRH(1);
//    private static final Abonent dictionaryRH = new DictionaryRH(1);
//    private static final Abonent grammarRH = new GrammarRH(1);
//    private static final Abonent trainingsRH = new TrainingsRH(1);
    private static final Address signInServlet = SignInServlet.getAdr();
    private static final Address signUpServlet = SignUpServlet.getAdr();
    private static final Address contentServlet = ContentServlet.getAdr();

    private static final Address contentByIdServlet = ContentByIdServlet.getAdr();

    public Address getAccountServiceAddress() {
        return accountService;
    }

    public Address getContentServiceAddress(){return contentService;}
    public static Address getContentService() {
        return contentService;
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
}
