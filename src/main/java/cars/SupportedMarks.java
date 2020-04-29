package cars;

public enum SupportedMarks {

    TOYOTA("drools.rules.mark.toyota"), MAZDA("drools.rules.mark.mazda");

    private String value;

    SupportedMarks(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
