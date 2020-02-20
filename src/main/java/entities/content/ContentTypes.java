package entities.content;

public enum ContentTypes {
    VIDEO, TEXT;
    public static ContentTypes getType(String type) throws Exception {
        ContentTypes t = null;
        if (type != null && !type.isBlank())
            t = type.equalsIgnoreCase("v") ? VIDEO : type.equalsIgnoreCase("t") ? TEXT : null;

        if (t == null)
            throw new Exception("BadType");

        return t;
    }
}
