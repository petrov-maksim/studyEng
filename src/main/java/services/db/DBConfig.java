package services.db;

public enum DBConfig {
    DB_DRIVER("org.postgresql.Driver"),
    DB_URL("jdbc:postgresql://localhost:5432/v2"),
    DB_LOGIN("postgres"),
    DB_PASSWORD("0000");

    DBConfig(String value) {
        this.value = value;
    }

    private String value;

    public String getValue(){
        return value;
    }
}
