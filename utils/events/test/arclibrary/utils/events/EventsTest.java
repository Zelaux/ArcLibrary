package arclibrary.utils.events;

import org.junit.*;

public class EventsTest{
    @Test
    public void testSignature(){
        EventSender sender = new EventSender("hello",
            EventSignature.make("millis", long.class).
                with("name", String.class)
        );
        EventReceiver receiver = new EventReceiver(sender);
        boolean[] received = {false};
        receiver.post(it -> {
            long millis = it.getParameter("millis");
            String name = it.getParameter("name");
            received[0] = true;
            Assert.assertEquals(1234, millis);
            Assert.assertEquals("123_123", name);
        });

        sender.setParameter("millis", 1234L);
        sender.setParameter("name", "123_123");
        sender.fire();
        Assert.assertTrue("Event not received",received[0]);
    }
}
