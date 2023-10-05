package arclibrary.utils.events;

import arc.*;
import lombok.*;
import lombok.experimental.*;
import org.junit.*;

public class TestSignature{
    @Test
    public void testSignature(){
        val sender = new EventSender<>("hello",
            EventSignature.make("millis", long.class).
                with("name", String.class)
        );
        val receiver = new EventReceiver<>(sender);
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
        Assert.assertTrue("Event not received", received[0]);
    }

    @Before
    public void reset(){
        Events.clear();
    }

    @Test
    public void fromParamsToObject(){


        val sender = new EventSender<>("hello", EventSignature.fromClass(AllArgEventType.class));
        val receiver = new EventReceiver<>(sender);
        boolean[] received = {false};
        receiver.postObject(it -> {
            received[0] = true;
            Assert.assertEquals(1234, it.millis);
            Assert.assertEquals("123_123", it.name);
        });
        sender.setParameter("millis", 1234L);
        sender.setParameter("name", "123_123");
        sender.fire();
        Assert.assertTrue("Event not received", received[0]);
    }

    @Test
    public void fromObjectToObject(){
        val sender = new EventSender<>("hello", EventSignature.fromClass(AllArgEventType.class));
        val receiver = new EventReceiver<>(sender);
        boolean[] received = {false};
        receiver.postObject(it -> {
            received[0] = true;
            Assert.assertEquals(1234, it.millis);
            Assert.assertEquals("123_123", it.name);
        });
        sender.setParameters(new AllArgEventType(1234,"123_123"));
        sender.fire();
        Assert.assertTrue("Event not received", received[0]);
    }

    @Test
    public void fromObjectToParams(){

        @NoArgsConstructor
        @FieldDefaults(level = AccessLevel.PUBLIC)
        class EventType{
            long millis;
            String name;

        }
        val sender = new EventSender<>("hello", EventSignature.fromClass(EventType.class));
        val receiver = new EventReceiver<>(sender);
        boolean[] received = {false};
        receiver.post(it -> {
            long millis = it.getParameter("millis");
            String name = it.getParameter("name");
            received[0] = true;
            Assert.assertEquals(1234, millis);
            Assert.assertEquals("123_123", name);
        });
        EventType eventType = new EventType();
        eventType.millis = 1234;
        eventType.name = "123_123";
        sender.setParameters(eventType);
        sender.fire();
        Assert.assertTrue("Event not received", received[0]);
    }

    @Test
    public void testWithObjectCreationWithAnnotation(){

        val sender = new EventSender<>("hello", EventSignature.fromClass(AllArgEventType.class));
        val receiver = new EventReceiver<>(sender);
        boolean[] received = {false};
        receiver.postObject(it -> {
            received[0] = true;
            Assert.assertEquals(1234, it.millis);
            Assert.assertEquals("123_123", it.name);
        });

        sender.setParameters(new AllArgEventType(1234, "123_123"));
        sender.fire();
        Assert.assertTrue("Event not received", received[0]);
    }

    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PUBLIC)
    static class AllArgEventType{
        long millis;
        String name;
    }
}
