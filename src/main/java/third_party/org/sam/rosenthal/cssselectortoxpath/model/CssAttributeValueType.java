package third_party.org.sam.rosenthal.cssselectortoxpath.model;

public enum CssAttributeValueType {
    EQUAL("="),
    TILDA_EQUAL("~="),
    PIPE_EQUAL("|="),
    CARROT_EQUAL("^="),
    DOLLAR_SIGN_EQUAL("$="),
    STAR_EQUAL("*=");

    private String equalString;

    private CssAttributeValueType(String nameIn) {
        this.equalString = nameIn;
    }

    public String getEqualStringName() {
        return equalString;
    }

    public static CssAttributeValueType valueTypeString(String unknownString) {
        if (unknownString == null) {
            return null;
        }

        switch (unknownString) {
        case "=":
            return EQUAL;
        case "~=":
            return TILDA_EQUAL;
        case "|=":
            return PIPE_EQUAL;
        case "$=":
            return DOLLAR_SIGN_EQUAL;
        case "^=":
            return CARROT_EQUAL;
        case "*=":
            return STAR_EQUAL;
        default:
            throw new IllegalArgumentException(unknownString);
        }
    }
}
