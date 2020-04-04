package third_party.org.sam.rosenthal.cssselectortoxpath.model;

public class CssAttributePsuedoClass extends CssAttribute {
    private CssPsuedoClassType psuedoClassType;
    private String element;

    public CssAttributePsuedoClass(CssPsuedoClassType psuedoClassTypeIn, String elementIn) {
        super(null, null, (CssAttributeValueType) null);
        psuedoClassType = psuedoClassTypeIn;
        element = elementIn;
    }

    public String getXPath() {
        return psuedoClassType.getXpath(element);
    }

    public CssPsuedoClassType getCssPsuedoClassType() {
        return psuedoClassType;
    }

    @Override
    public String toString() {
        String val = "Psuedo Class = " + psuedoClassType;
        //		switch(psuedoClassType) {
        //		case FIRST_OF_TYPE: 
        //		case LAST_OF_TYPE:
        //		case ONLY_OF_TYPE: 
        //			val += ", Element = " + element;
        //			break;
        //		default:
        //			//empty
        //			break;
        //		}
        return val;
    }

}
