package third_party.org.sam.rosenthal.cssselectortoxpath.model;

import third_party.org.sam.rosenthal.cssselectortoxpath.utilities.CssElementAttributeParser;
import third_party.org.sam.rosenthal.cssselectortoxpath.utilities.CssSelectorToXPathConverterException;

public class CssElementCombinatorPair {
    private CssCombinatorType combinatorType;
    private CssElementAttributes cssElementAttributes;

    public CssElementCombinatorPair(CssCombinatorType combinatorTypeIn, String cssElementAttributesStringIn)
            throws CssSelectorToXPathConverterException {
        this.combinatorType = combinatorTypeIn;
        this.cssElementAttributes = new CssElementAttributeParser().createElementAttribute(cssElementAttributesStringIn);
    }

    public CssCombinatorType getCombinatorType() {
        return combinatorType;
    }

    public CssElementAttributes getCssElementAttributes() {
        return cssElementAttributes;
    }

    @Override
    public String toString() {
        return "(Combinator=" + this.getCombinatorType() + ", " + this.cssElementAttributes + ")";
    }

    @Override
    public boolean equals(Object cssElementCombinatorPair) {
        if (cssElementCombinatorPair == null) {
            return false;
        }
        return this.toString().equals(cssElementCombinatorPair.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
