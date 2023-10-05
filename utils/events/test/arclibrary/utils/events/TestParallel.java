package arclibrary.utils.events;

import lombok.*;
import lombok.experimental.*;
import org.junit.*;

public class TestParallel{
    @Test
    public void notOverlaps(){
        boolean[] visited = {false, false};

        EventSender<EventData> senderA = new EventSender<>("methodA", EventData.signature);
        EventReceiver<EventData> receiverA = EventReceiver.from(senderA);
        EventSender<EventData> senderB = new EventSender<>("methodB", EventData.signature);
        EventReceiver<EventData> receiverB = EventReceiver.from(senderB);
        String expectedA = new EventData("a", 1).toString();
        String expectedB = new EventData("b", 2).toString();
        receiverA.postObject(data -> {
            Assert.assertEquals(expectedA, data.toString());
            Assert.assertFalse("methodA invokes twice", visited[0]);
            visited[0] = true;
        });
        receiverB.postObject(data -> {
            Assert.assertEquals(expectedB, data.toString());
            Assert.assertFalse("methodB invokes twice", visited[1]);
            visited[1] = true;
        });

        senderA.setParameters(new EventData("a",1));
        senderA.fire();

        senderB.setParameters(new EventData("b",2));
        senderB.fire();
        Assert.assertTrue("methodA not invoked",visited[0]);
        Assert.assertTrue("methodB not invoked",visited[1]);

        visited[0]=visited[1]=false;

        senderB.setParameters(new EventData("b",2));
        senderB.fire();
        senderA.setParameters(new EventData("a",1));
        senderA.fire();

        Assert.assertTrue("methodA not invoked",visited[0]);
        Assert.assertTrue("methodB not invoked",visited[1]);
    }

    @AllArgsConstructor
    @ToString
    @FieldDefaults(level = AccessLevel.PUBLIC)
    private static class EventData{
        static final EventSignature<EventData> signature = EventSignature.fromClass(EventData.class);
        String title;
        int id;
    }
}
