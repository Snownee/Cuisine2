package third_party.org.sam.rosenthal.cssselectortoxpath.model;

import java.util.ArrayList;
import java.util.List;

import third_party.org.sam.rosenthal.cssselectortoxpath.utilities.CssSelectorToXPathConverterException;

//Take a string and the format of the string is...
//xxx[yyy]...[zzz]
//-->break it up into a list = xxx,[yyy],..,[zzz]
//if xxx DNE==>break into list=*,[yyy],...,[zzz]
//
//create new class
//make into css element attribute
//fills this stuff,
//only has constructor and getters
public class CssElementAttributes {
    private String element;
    private List<CssAttribute> cssAttributeList;

    public CssElementAttributes(String elementIn, List<CssAttribute> cssAttributeListIn)
            throws CssSelectorToXPathConverterException {
        this.element = elementIn;
        this.cssAttributeList = new ArrayList<>(cssAttributeListIn);
    }

    public String getElement() {
        return element;
    }

    public List<CssAttribute> getCssAttributeList() {
        return cssAttributeList;
    }

    @Override
    public String toString() {
        return "Element=" + this.element + ", CssAttributeList=" + this.cssAttributeList;
    }

    @Override
    public boolean equals(Object cssElementAttributes) {
        if (cssElementAttributes == null) {
            return false;
        }
        return this.toString().equals(cssElementAttributes.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
