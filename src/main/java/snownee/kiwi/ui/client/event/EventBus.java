package snownee.kiwi.ui.client.event;

import java.util.Map;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

import snownee.kiwi.ui.client.widget.Widget;

public final class EventBus<T extends Widget<T>> {

    private T owner;
    private final Map<String, Predicate<T>> callbacks = Maps.newHashMap();

    public EventBus(T owner) {
        this.owner = owner;
    }

    public void bind(String eventName, Predicate<T> callback) {
        Predicate<T> oldCallback = callbacks.get(eventName);
        if (oldCallback == null) {
            callbacks.put(eventName, callback);
        } else {
            callbacks.put(eventName, oldCallback.or(callback));
        }
    }

    public boolean fire(String eventName) {
        return callbacks.getOrDefault(eventName, Predicates.alwaysFalse()).test(owner);
    }

    public void destroy() {
        owner = null;
    }

}
