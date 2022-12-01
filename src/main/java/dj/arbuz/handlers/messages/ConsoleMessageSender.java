<<<<<<<< HEAD:console/src/main/java/dj/arbuz/console/ConsoleMessageSender.java
package dj.arbuz.console;

import dj.arbuz.handlers.messages.AbstractMessageSender;
import dj.arbuz.handlers.messages.MessageHandlerResponse;

import java.util.List;
========
package dj.arbuz.handlers.messages;

import dj.arbuz.bots.console.ConsoleBot;
import dj.arbuz.database.local.UserStorage;
import dj.arbuz.handlers.notifcations.ConsolePostsPullingThread;
>>>>>>>> developTaskFour:src/main/java/dj/arbuz/handlers/messages/ConsoleMessageSender.java

/**
 * Класс-отправитель сообщений консольного бота
 *
 * @author Кедровских Олег
 * @version 1.0
 * @see AbstractMessageSender
 */
public final class ConsoleMessageSender extends AbstractMessageSender {
    /**
     * Поле потока, получающего новые посты для консольного бота
     *
     * @see ConsolePostsPullingThread
     */
    private final ConsolePostsPullingThread notificationPullingThread;

    /**
     * Конструктор - создает экземпляр класса
     *
     * @param consoleBot                консольный бот
     * @param notificationPullingThread поток получающий новые посты в группах
     */
    public ConsoleMessageSender(ConsoleBot consoleBot, ConsolePostsPullingThread notificationPullingThread) {
        super(consoleBot);
        this.notificationPullingThread = notificationPullingThread;
    }

    /**
     * Метод для отправки сообщений пользователю
     *
     * @param userSendResponse ответ бота, который необходимо отправить пользователю
     */
    @Override
    public void sendResponse(MessageHandlerResponse userSendResponse) {
        super.sendResponse(userSendResponse);
        List<String> usersSendResponseId = userSendResponse.getUsersSendResponseId();

        if (notificationPullingThread.hasNewPosts()) {
            for (String newPostText : notificationPullingThread.getNewPosts()) {
                for (String userSendResponseId : usersSendResponseId) {
                    this.sendSingleMessage(userSendResponseId, newPostText);
                }
            }
        }

    }
}
