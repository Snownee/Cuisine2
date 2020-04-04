package third_party.org.sam.rosenthal.cssselectortoxpath.utilities;

public class CssSelectorToXPathConverterInvalidFirstLastOnlyOfType extends CssSelectorToXPathConverterException {

    private static final long serialVersionUID = 1L;
    public static final String FIRST_LAST_ONLY_OF_TYPE_UNSUPPORTED_ERROR_FORMAT = "Unable to convert a CSS Selector with \"*\" or \"\" before psuedo class :first-of-type/:last-of-type/:only-of-type to a XPath";

    public CssSelectorToXPathConverterInvalidFirstLastOnlyOfType() {
        super(FIRST_LAST_ONLY_OF_TYPE_UNSUPPORTED_ERROR_FORMAT);
    }

}
