package nl.spelberg.util.event;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ListenersTest {

    private Listeners<MyListener> listeners;

    @Before
    public void setUp() throws Exception {
        listeners = Listeners.newInstanceFor(MyListener.class);
    }

    @Test
    public void testAddRemove() throws Exception {
        MyListener mockListener = mock(MyListener.class, RETURNS_SMART_NULLS);

        listeners.onAll().callMe();

        verify(mockListener, never()).callMe();

        listeners.add(mockListener);
        listeners.onAll().callMe();

        verify(mockListener, times(1)).callMe();

        listeners.remove(mockListener);
        listeners.onAll().callMe();

        verify(mockListener, times(1)).callMe();
    }

    private interface MyListener {

        public void callMe();

    }
}
