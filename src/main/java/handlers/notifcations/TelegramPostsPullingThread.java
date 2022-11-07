package handlers.notifcations;

import bots.telegram.TelegramBot;
import database.GroupsStorage;
import socialnetworks.socialnetwork.SocialNetworkException;
import socialnetworks.socialnetwork.SocialNetwork;
import socialnetworks.vk.Vk;

import java.util.List;
import java.util.Optional;

/**
 * Класс получающий новые посты для телеграма
 *
 * @author Кедровских Олег
 * @version 1.7
 * @see PostsPullingThread
 */
public class TelegramPostsPullingThread extends PostsPullingThread {
    /**
     * Поле телеграмм бота
     *
     * @see TelegramBot
     */
    private final TelegramBot telegramBot;

    /**
     * Конструктор - создает экземпляр класса
     *
     * @param telegramBot   телеграмм бот
     * @param groupsStorage база данных групп на которые оформлена подписка
     * @param socialNetwork социальная сети реализующая необходимые для работы методы
     */
    public TelegramPostsPullingThread(TelegramBot telegramBot, GroupsStorage groupsStorage, Vk socialNetwork) {
        super(groupsStorage, socialNetwork);
        this.telegramBot = telegramBot;
    }

    /**
     * Метод логики выполняемой внутри {@code TelegramPostsPullingThread}
     *
     * @see GroupsStorage#getGroups()
     * @see SocialNetwork#getNewPostsAsStrings(GroupsStorage, String)
     * @see GroupsStorage#getSubscribedToGroupUsersId(String)
     */
    @Override
    public void run() {
        while (working.get()) {
            for (String groupScreenName : groupsBase.getGroups()) {
                Optional<List<String>> threadFindNewPosts;
                try {
                    threadFindNewPosts = socialNetwork.getNewPostsAsStrings(groupsBase, groupScreenName);
                } catch (SocialNetworkException e) {
                    continue;
                }

                // проверяется наличие новых постов, могут отсутствовать по причине отсутствия новых постов или отсутствия группы в базе данных
                if (threadFindNewPosts.isPresent()) {
                    for (String newPostText : threadFindNewPosts.get()) {
                        for (String userSendNewPostId : groupsBase.getSubscribedToGroupUsersId(groupScreenName)) {
                            telegramBot.send(userSendNewPostId, newPostText);
                        }
                    }
                }

            }
            try {
                final int oneHourInMilliseconds = 3600000;
                Thread.sleep(oneHourInMilliseconds);
            } catch (InterruptedException e) {
                break;
            }
        }
        working.set(false);
    }

    /**
     * Метод проверяющий наличие новых постов
     *
     * @throws UnsupportedOperationException - возникает тк эта операция не нужна в этой реализации класса,
     *                                       поэтому он не реализован
     */
    @Override
    public boolean hasNewPosts() {
        throw new UnsupportedOperationException(
                "Метод не реализован, тк эта реализации сразу отправляет сообщения пользователям"
        );
    }

    /**
     * Метод получающий новые посты
     *
     * @throws UnsupportedOperationException - возникает тк эта операция не нужна в этой реализации класса,
     *                                       поэтому он не реализован
     */
    @Override
    public List<String> getNewPosts() {
        throw new UnsupportedOperationException(
                "Метод не реализован, тк реализация сразу отправляет сообщения пользователям сразу"
        );
    }
}
