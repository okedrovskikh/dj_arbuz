<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/socialnetwork/groups/SubscribeStatus.java
package dj.arbuz.socialnetworks.socialnetwork.groups;
========
package socialnetworks.socialnetwork.groups;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/socialnetwork/groups/SubscribeStatus.java

import dj.arbuz.BotTextResponse;

/**
 * Енум результатов подписки на группу
 *
 * @author Кедровских Олег
 * @version 1.0
 */
public enum SubscribeStatus {
    /**
     * Значения енума
     */
    SUBSCRIBED(BotTextResponse.SUBSCRIBE),
    ALREADY_SUBSCRIBED(BotTextResponse.ALREADY_SUBSCRIBER),
    GROUP_IS_CLOSED(BotTextResponse.GROUP_IS_CLOSED);

    /**
     * Поле сообщения с результатом попытки подписаться
     */
    private final String subscribeMessage;

    /**
     * Конструктор - создает енум
     *
     * @param subscribeMessage - сообщение с результатом попытки подписаться
     */
    SubscribeStatus(String subscribeMessage) {
        this.subscribeMessage = subscribeMessage;
    }

    /**
     * Метод получающий сообщение с информацией о результате попытки подписаться
     *
     * @return сообщение с результатом попытки подписаться
     */
    public String getSubscribeMessage() {
        return subscribeMessage;
    }
}
