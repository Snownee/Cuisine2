package snownee.kiwi.ui.client.factory;

public enum ElementContentType {
    NONE,
    TEXT,
    ELEMENT;

    public boolean acceptText() {
        return this == TEXT;
    }

    public boolean acceptElement() {
        return this == ELEMENT;
    }
}
