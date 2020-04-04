package third_party.org.sam.rosenthal.cssselectortoxpath.model;

import third_party.org.sam.rosenthal.cssselectortoxpath.utilities.CssSelectorToXPathConverterInvalidFirstLastOnlyOfType;

public enum CssPsuedoClassType {

    EMPTY(":empty", "[not(*) and .=\"\"]"),
    FIRST_OF_TYPE(":first-of-type", "_FIRST_OF_TYPE_PLACEHOLDER_"),
    LAST_OF_TYPE(":last-of-type", "_LAST_OF_TYPE_PLACEHOLDER_"),
    ONLY_OF_TYPE(":only-of-type", "_ONLY_OF_TYPE_PLACEHOLDER_"),
    FIRST_CHILD(":first-child", "[count(preceding-sibling::*)=0]"),
    LAST_CHILD(":last-child", "[count(following-sibling::*)=0]"),
    ONLY_CHILD(":only-child", FIRST_CHILD.getXpath("") + LAST_CHILD.getXpath(""));

    private String typeString;
    private String xpath;

    private CssPsuedoClassType(String typeStringIn, String xpathIn) {
        this.typeString = typeStringIn;
        this.xpath = xpathIn;
    }

    public String getPsuedoString() {
        return typeString;
    }

    public String getXpath(String element) {
        if (xpath.equals("_FIRST_OF_TYPE_PLACEHOLDER_")) {
            return "[count(preceding-sibling::" + element + ")=0]";
        } else if (xpath.equals("_LAST_OF_TYPE_PLACEHOLDER_")) {
            return "[count(following-sibling::" + element + ")=0]";
        } else if (xpath.equals("_ONLY_OF_TYPE_PLACEHOLDER_")) {
            return FIRST_OF_TYPE.getXpath(element) + LAST_OF_TYPE.getXpath(element);
        }
        return xpath;
    }

    public static CssPsuedoClassType psuedoClassTypeString(String unknownString, String element) throws CssSelectorToXPathConverterInvalidFirstLastOnlyOfType {
        if (unknownString == null) {
            return null;
        }
        switch (unknownString) {
        case ":empty":
            return EMPTY;
        case ":first-of-type":
            return getOfType(FIRST_OF_TYPE, element);
        case ":last-of-type":
            return getOfType(LAST_OF_TYPE, element);
        case ":only-of-type":
            return getOfType(ONLY_OF_TYPE, element);
        case ":first-child":
            return FIRST_CHILD;
        case ":last-child":
            return LAST_CHILD;
        case ":only-child":
            return ONLY_CHILD;
        default:
            throw new IllegalArgumentException(unknownString);
        }
    }

    private static CssPsuedoClassType getOfType(CssPsuedoClassType ofType, String element) throws CssSelectorToXPathConverterInvalidFirstLastOnlyOfType {
        if (element == null || element.equals("*")) {
            throw new CssSelectorToXPathConverterInvalidFirstLastOnlyOfType();
        } else {
            return ofType;
        }
    }
}
