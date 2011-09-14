package nl.spelberg.util.event;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.util.Assert;

public class Listeners<I> implements Serializable {

    private static final long serialVersionUID = 42L;

    private final Set<I> listeners;
    private final Class<I> listenerClass;

    public Listeners(Class<I> listenerInterface) {
        Assert.notNull(listenerInterface, "listenerInterface is null");
        Assert.isTrue(listenerInterface.isInterface(), "listenerInterface is not an interface: " + listenerInterface);
        this.listeners = new LinkedHashSet<I>();
        this.listenerClass = listenerInterface;
    }

    public void add(I listener) {
        listeners.add(listener);
    }

    public void remove(I listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings({"unchecked"})
    public I onAll() {
        return (I) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{listenerClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                for (I listener : listeners) {
                    method.invoke(listener, args);
                }
                return null;
            }
        });
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Listeners<T> newInstanceFor(Class<?> listenerClass) {
        return new Listeners<T>((Class<T>) listenerClass);
    }
}
