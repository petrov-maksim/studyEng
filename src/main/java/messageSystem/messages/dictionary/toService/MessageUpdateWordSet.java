package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import org.apache.commons.fileupload.FileItem;
import services.db.DictionaryService;

public class MessageUpdateWordSet extends MessageToDictionaryService {
    private final int wordSetId;
    private final FileItem img;
    private final String name;


    public MessageUpdateWordSet(Address from, Address to, int wordSetId, FileItem img, String name) {
        super(from, to);
        this.wordSetId = wordSetId;
        this.img = img;
        this.name = name;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.updateWordSet(wordSetId, img, name);
    }
}
