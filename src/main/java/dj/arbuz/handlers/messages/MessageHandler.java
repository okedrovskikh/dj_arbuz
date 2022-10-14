package dj.arbuz.handlers.messages;

import dj.arbuz.bots.BotTextResponse;
import dj.arbuz.stoppable.Stoppable;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiTokenExtensionRequiredException;
import com.vk.api.sdk.exceptions.ClientException;
import dj.arbuz.handlers.vk.groups.NoGroupException;
import dj.arbuz.handlers.vk.Vk;
import dj.arbuz.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import dj.arbuz.database.GroupsStorage;
import dj.arbuz.database.UserStorage;

/**
 * Класс утилитных методов создающий ответы на сообщения пользователя
 *
 * @author Кедровских Олег
 * @author Щеголев Андрей
 * @version 1.8
 */
public class MessageHandler {
    /**
     * Поле обработчика запросов к Vk API
     */
    private static Vk vk = new Vk("src/main/resources/anonsrc/vkconfig.properties");
    /**
     * Поле кол-ва запрашиваемых последних постов
     */
    private static final int DEFAULT_POST_NUMBER = 5;
    /**
     * Поле индекса команды
     */
    private static final int COMMAND_INDEX = 0;
    /**
     * Поле индекса аргумента
     */
    private static final int ARG_INDEX = 1;
    /**
     * Поле хранилища групп
     */
    private static GroupsStorage groupsBase = GroupsStorage.getInstance();
    /**
     * Поле хранилища пользователей
     */
    private static UserStorage usersBase = UserStorage.getInstance();

    /**
     * Метод определяющий команды в сообщении пользователя и возвращающий ответ
     *
     * @param message          - сообщение пользователя
     * @param callingBotThread - бот из которого был вызван метод
     * @return возвращает ответ на сообщение пользователя
     */
    public static MessageHandlerResponse executeMessage(String message, String telegramUserId,
                                                        Stoppable callingBotThread) {
        String[] commandAndArgs = message.split(" ", 2);

        if (groupsBase == null) {
            groupsBase = GroupsStorage.getInstance();
        }

        if (isItNoArgCommand(commandAndArgs)) {
            switch (commandAndArgs[COMMAND_INDEX]) {
                case "/help" -> {
                    return getHelpResponse();
                }
                case "/auth" -> {
                    return getAuthResponse();
                }
                case "/stop" -> {
                    return getStopResponse(callingBotThread);
                }
            }
        }

        if (!usersBase.contains(telegramUserId)) {
            return getNotAuthedResponse();
        }

        User user = usersBase.getUser(telegramUserId);

        if (isItSingleArgCommand(commandAndArgs)) {
            switch (commandAndArgs[COMMAND_INDEX]) {
                case "/link" -> {
                    return getGroupURL(commandAndArgs[ARG_INDEX], user);
                }
                case "/id" -> {
                    return getGroupId(commandAndArgs[ARG_INDEX], user);
                }
                case "/subscribe" -> {
                    return subscribeTo(commandAndArgs[ARG_INDEX], user);
                }
                case "/get_five_posts" -> {
                    return getFiveLastPosts(commandAndArgs[ARG_INDEX], user);
                }
            }
        }
        return getUnknownCommandResponse();
    }

    /**
     * Метод проверяет есть ли аргументы в полученной команде
     *
     * @param commandAndArgs - массив аргументов и комманд
     * @return true - если нет аргументов
     * false - если есть аргументы
     */
    private static boolean isItNoArgCommand(String[] commandAndArgs) {
        return commandAndArgs.length == 1;
    }

    /**
     * Метод проверяет есть ли аргументы в полученной команде
     *
     * @param commandAndArgs - массив аргументов и комманд
     * @return true - если есть только один аргумент
     * false - если нет аргументов или их больше одного
     */
    private static boolean isItSingleArgCommand(String[] commandAndArgs) {
        return commandAndArgs.length == 2;
    }

    /**
     * Метод формирующий ответ на команду /help
     *
     * @return ответ на команду /help содержит HELP_INFO
     */
    private static MessageHandlerResponse getHelpResponse() {
        return new MessageHandlerResponse(BotTextResponse.HELP_INFO);
    }

    /**
     * Метод формирующий ответ на команду /auth
     *
     * @return ответ на команду /auth
     */
    private static MessageHandlerResponse getAuthResponse() {
        String authURL = vk.getAuthURL();

        if (authURL == null) {
            return new MessageHandlerResponse(BotTextResponse.AUTH_ERROR);
        }

        return new MessageHandlerResponse(BotTextResponse.AUTH_GO_VIA_LINK + authURL, vk);
    }

    /**
     * Метод формирующий ответ на команду /stop
     *
     * @param callingBotThread - бот вызвавший метод
     * @return ответ на /stop содержит STOP_INFO
     */
    private static MessageHandlerResponse getStopResponse(Stoppable callingBotThread) {

        if (callingBotThread.isWorking()) {
            callingBotThread.stopWithInterrupt();
        }

        return new MessageHandlerResponse(BotTextResponse.STOP_INFO);
    }

    /**
     * Метод формирующий ответ если пользователь обращается к ф-циям требующим аутентификации
     *
     * @return ответ содержащий NOT_AUTHED_USER
     */
    private static MessageHandlerResponse getNotAuthedResponse() {
        return new MessageHandlerResponse(BotTextResponse.NOT_AUTHED_USER);
    }

    /**
     * Метод возвращающий ответ на /link
     *
     * @param groupName - имя группы
     * @param user      - пользователь отправивший сообщение
     * @return ссылку на верефицированную группу если такая нашлась
     */
    private static MessageHandlerResponse getGroupURL(String groupName, User user) {
        try {
            return new MessageHandlerResponse(vk.getGroupURL(groupName, user));
        } catch (ApiTokenExtensionRequiredException e) {
            return new MessageHandlerResponse(BotTextResponse.UPDATE_TOKEN);
        } catch (NoGroupException e) {
            return new MessageHandlerResponse(e.getMessage());
        } catch (ApiException | ClientException e) {
            return new MessageHandlerResponse(BotTextResponse.VK_API_ERROR);
        }
    }

    /**
     * Метод возвращающий ответ на /id
     *
     * @param groupName - имя группы
     * @param user      - пользователь отправивший сообщение
     * @return id верефицированной группы если такая нашлась
     */
    private static MessageHandlerResponse getGroupId(String groupName, User user) {
        try {
            return new MessageHandlerResponse(String.valueOf(vk.getGroupId(groupName, user)));
        } catch (ApiTokenExtensionRequiredException e) {
            return new MessageHandlerResponse(BotTextResponse.UPDATE_TOKEN);
        } catch (NoGroupException e) {
            return new MessageHandlerResponse(e.getMessage());
        } catch (ApiException | ClientException e) {
            return new MessageHandlerResponse(BotTextResponse.VK_API_ERROR);
        }
    }

    /**
     * Метод для подписки пользователя
     *
     * @param groupName - Название группы
     * @param user      - айди юзера
     * @return - возврат текста для сообщения
     */
    private static MessageHandlerResponse subscribeTo(String groupName, User user) {
        try {
            return new MessageHandlerResponse(vk.subscribeTo(groupName, user).getSubscribeMessage());
        } catch (ApiTokenExtensionRequiredException e) {
            return new MessageHandlerResponse(BotTextResponse.UPDATE_TOKEN);
        } catch (NoGroupException e) {
            return new MessageHandlerResponse(e.getMessage());
        } catch (ApiException | ClientException e) {
            return new MessageHandlerResponse(BotTextResponse.VK_API_ERROR);
        }
    }

    /**
     * Метод возвращающий ответ на ответ на /get_last_posts
     *
     * @param groupName - имя группы
     * @param user      - пользователь отправивший сообщение
     * @return текст постов, ссылки на изображения в них, а также ссылки
     */
    private static MessageHandlerResponse getFiveLastPosts(String groupName, User user) {
        try {
            return new MessageHandlerResponse(vk.getLastPosts(groupName, DEFAULT_POST_NUMBER, user).orElseThrow());
        } catch (ApiTokenExtensionRequiredException e) {
            return new MessageHandlerResponse(BotTextResponse.UPDATE_TOKEN);
        } catch (NoSuchElementException e) {
            return new MessageHandlerResponse(BotTextResponse.NO_POSTS_IN_GROUP);
        } catch (NoGroupException | IllegalArgumentException e) {
            return new MessageHandlerResponse(e.getMessage());
        } catch (ApiException | ClientException e) {
            return new MessageHandlerResponse(BotTextResponse.VK_API_ERROR);
        }
    }

    /**
     * Метод возвращающий ответ при получении неизвестной команды
     *
     * @return ответ содержащий UNKNOWN_COMMAND
     */
    private static MessageHandlerResponse getUnknownCommandResponse() {
        return new MessageHandlerResponse(BotTextResponse.UNKNOWN_COMMAND);
    }
}