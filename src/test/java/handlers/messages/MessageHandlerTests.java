package handlers.messages;

import handlers.messages.HandlerResponse;
import handlers.messages.MessageHandler;
import handlers.messages.TextResponse;
import org.junit.jupiter.api.Test;
import user.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс тестирующий работу класса MessageHandler
 */
public class MessageHandlerTests {
    /**
     * Метод проверяющий корректность работы всех команд бота
     */
    @Test
    public void testAllCommands(){
        Thread testThread = new Thread(() -> {
            User user = null;
            HandlerResponse response;
            response = MessageHandler.executeMessage("/help", user, null);
            assertEquals(response.getTextMessage(), TextResponse.HELP_INFO);
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/abrakadabra", user, null);
            assertEquals(response.getTextMessage(), TextResponse.NOT_AUTHED_USER);
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/start", user, null);
            assertNotEquals(response.getTextMessage(), TextResponse.AUTH_ERROR);
            System.out.println(response.getTextMessage());
            assertNotNull(response.getUpdateUser());
            user = response.getUpdateUser().createUser();
            assertNotNull(user);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/link lida", user, null);
            assertEquals(response.getTextMessage(), "https://vk.com/lidamudota");
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/id lida", user, null);
            assertEquals(response.getTextMessage(), "147725517");
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/subscribe triplesixdelete", user, null);
            assertEquals(response.getTextMessage(), TextResponse.SUBSCRIBE);
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/subscribe triplesixdelete", user, null);
            assertEquals(response.getTextMessage(), TextResponse.ALREADY_SUBSCRIBER);
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/abrakadabra", user, null);
            assertEquals(response.getTextMessage(), TextResponse.UNKNOWN_COMMAND);
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/subscribe triplesixdelete", user, null);
            assertEquals(response.getTextMessage(), TextResponse.SUBSCRIBE);
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = MessageHandler.executeMessage("/subscribe triplesixdelete", user, null);
            assertEquals(response.getTextMessage(), TextResponse.SUBSCRIBE);
            assertNull(response.getUpdateUser());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
