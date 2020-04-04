package third_party.org.sam.rosenthal.cssselectortoxpath.model;

public enum CssCombinatorType {
    SPACE(' ', "//"),
    PLUS('+', "/following-sibling::*[1]/self::"),
    GREATER_THAN('>', "/"),
    TILDA('~', "/following-sibling::");

    private char typeChar;
    private String xpath;

    private CssCombinatorType(char typeCharIn, String xpathIn) {
        this.typeChar = typeCharIn;
        this.xpath = xpathIn;
    }

    public char getCombinatorChar() {
        return typeChar;
    }

    public String getXpath() {
        return xpath;
    }

    public static CssCombinatorType combinatorTypeChar(String unknownString) {
        if (unknownString == null) {
            return null;
        }

        switch (unknownString) {
        case " ":
            return SPACE;
        case "+":
            return PLUS;
        case ">":
            return GREATER_THAN;
        case "~":
            return TILDA;
        default:
            throw new IllegalArgumentException(unknownString);
        }
    }

}
