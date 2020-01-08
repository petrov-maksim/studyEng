package services.db;


import services.AbstractService;
import util.QueryExecutor;

public abstract class AbstractDBService extends AbstractService {
    protected final QueryExecutor queryExecutor;
    public AbstractDBService(int threadsNum, QueryExecutor queryExecutor) {
        super(threadsNum);
        this.queryExecutor = queryExecutor;
    }
}
