package cars;

public enum SupportedMarks {

    TOYOTA ( "toyota"), MAZDA ("mazda");

    private String value;

    SupportedMarks(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
