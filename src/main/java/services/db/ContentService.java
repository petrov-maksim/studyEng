package services.db;

import util.QueryExecutor;



public class ContentService extends AbstractDBService{
    public ContentService(int threadsNum, QueryExecutor queryExecutor) {
        super(threadsNum, queryExecutor);
    }

    public String[] getNVideos(int num){
        return new String[num];
    }

    public String[] getNTexts(int num){
        return new String[num];
    }

    public String getContentById(int id, String type){return "content";}
}
