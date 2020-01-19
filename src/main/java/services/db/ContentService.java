package services.db;

import services.AbstractService;
import util.QueryExecutor;



public class ContentService extends AbstractService {
    private final QueryExecutor queryExecutor;
    public ContentService(int threadsNum,  QueryExecutor queryExecutor) {
        super(threadsNum);
        this.queryExecutor = queryExecutor;
    }

    public String[] getNVideos(int num){
        return new String[num];
    }

    public String[] getNTexts(int num){
        return new String[num];
    }

    public String getContentById(int id, String type){return "content";}
}
