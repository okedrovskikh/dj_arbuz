<<<<<<<< HEAD:common/src/test/java/dj/arbuz/socialnetworks/vk/groups/SubscribeStatusTests.java
package dj.arbuz.socialnetworks.vk.groups;
========
package socialnetworks.vk.groups;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/test/java/socialnetworks/vk/groups/SubscribeStatusTests.java

import dj.arbuz.BotTextResponse;
import org.junit.jupiter.api.Test;
<<<<<<<< HEAD:common/src/test/java/dj/arbuz/socialnetworks/vk/groups/SubscribeStatusTests.java
import dj.arbuz.socialnetworks.socialnetwork.groups.SubscribeStatus;
========
import socialnetworks.socialnetwork.groups.SubscribeStatus;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/test/java/socialnetworks/vk/groups/SubscribeStatusTests.java

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс тестирующий значения сообщений в енуме SubscribeStatus
 *
 * @author Кедровских Олег
 * @version 1.0
 */
public class SubscribeStatusTests {
    /**
     * Метод тестирующий сообщение при удачной подписке на группу
     */
    @Test
    public void testSubscribeSubscribeStatusMessage() {
        assertEquals(BotTextResponse.SUBSCRIBE, SubscribeStatus.SUBSCRIBED.getSubscribeMessage());
    }

    /**
     * Метод тестирующий сообщение при условии, что пользователь уже подписан на эту группу
     */
    @Test
    public void testAlreadySubscribeStatusMessage() {
        assertEquals(BotTextResponse.ALREADY_SUBSCRIBER, SubscribeStatus.ALREADY_SUBSCRIBED.getSubscribeMessage());
    }

    /**
     * Метод тестирующий сообщение, сообщающее о том что запрашиваемое для подписки сообщество закрыто
     */
    @Test
    public void testGroupIsClosedSubscribeStatusMessage() {
        assertEquals(BotTextResponse.GROUP_IS_CLOSED, SubscribeStatus.GROUP_IS_CLOSED.getSubscribeMessage());
    }
}
