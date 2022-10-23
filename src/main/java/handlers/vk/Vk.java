package handlers.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.Group;
import com.vk.api.sdk.objects.groups.GroupIsClosed;
import database.GroupsStorage;
import handlers.vk.groups.NoGroupException;
import handlers.vk.groups.VkGroups;
import handlers.vk.oAuth.VkAuth;
import handlers.vk.wall.VkWall;
import handlers.vk.groups.SubscribeStatus;
import httpserver.server.HttpServer;
import user.CreateUser;
import user.User;

import java.util.List;
import java.util.Optional;

/**
 * Класс обрабатывающий запросы пользователя к Vk API
 *
 * @author Кедровских Олег
 * @author Щеголев Андрей
 * @version 2.2
 * @see CreateUser
 */
public class Vk implements CreateUser {
    /**
     * Поле класса для взаимодействия с группами через vk api
     */
    private final VkGroups groups;
    /**
     * Поле класс для взаимодействия со стеной вк
     */
    private final VkWall wall;
    /**
     * Поле для аутентификации через vk
     */
    private final VkAuth oAuth;
    /**
     * Поле пользователя приложения в vk
     */
    private final ServiceActor vkApp;

    /**
     * Конструктор по пути до файла с конфигурацией приложения
     *
     * @param vkAppConfigurationJsonFilePath путь до json файла с конфигурацией
     */
    public Vk(HttpServer httpServer, String vkAppConfigurationJsonFilePath) {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vkApiClient = new VkApiClient(transportClient);
        groups = new VkGroups(vkApiClient);
        wall = new VkWall(vkApiClient);
        oAuth = new VkAuth(vkApiClient, httpServer, vkAppConfigurationJsonFilePath);
        vkApp = oAuth.createAppActor();
    }

    /**
     * Конструктор - создает экземпляр класса
     *
     * @param vkGroups экземпляр класса отвечающего за взаимодействие с группами vk
     * @param vkWall   экземпляр класса отвечающего за взаимодействие со стеной vk
     * @param vkAuth   экземпляр класса отвечающего за аутентификацию в vk
     */
    public Vk(VkGroups vkGroups, VkWall vkWall, VkAuth vkAuth) {
        this.groups = vkGroups;
        this.wall = vkWall;
        this.oAuth = vkAuth;
        vkApp = oAuth.createAppActor();
    }

    /**
     * Метод обертка возвращающий ссылку для аутентификации
     *
     * @return ссылку для аутентификации, если сервер недоступен, то это null
     * @see VkAuth#getAuthURL()
     */
    public String getAuthURL() {
        return oAuth.getAuthURL();
    }

    /**
     * Метод интерфейса {@link CreateUser} создающий пользователя
     *
     * @return нового пользователя
     * @see VkAuth#createUser(String)
     */
    @Override
    public User createUser(String systemUserId) {
        return oAuth.createUser(systemUserId);
    }

    /**
     * Метод получающий ссылку на группу в vk найденную по {@code userReceivedGroupName}
     *
     * @param userReceivedGroupName Название группы
     * @param userCallingMethod     пользователь вызвавший метод
     * @return возвращает ссылку на группу в vk
     * @throws ApiException     возникает при ошибке обращения к vk api со стороны vk
     * @throws NoGroupException возникает если не нашлась группа по заданной подстроке
     * @throws ClientException  возникает при ошибке обращения к vk api со стороны клиента
     * @see VkGroups#searchGroup(String, User)
     */
    public String getGroupURL(String userReceivedGroupName, User userCallingMethod)
            throws NoGroupException, ClientException, ApiException {
        return VkConstants.VK_ADDRESS + groups.searchGroup(userReceivedGroupName, userCallingMethod).getScreenName();
    }

    /**
     * Метод получающий id группы найденную по {@code userReceivedGroupName}
     *
     * @param userReceivedGroupName Название группы
     * @param userCallingMethod     пользователь вызвавший метод
     * @return возвращает id группы
     * @throws ApiException     возникает при ошибке обращения к vk api со стороны vk
     * @throws NoGroupException возникает если не нашлась группа по заданной подстроке
     * @throws ClientException  возникает при ошибке обращения к vk api со стороны клиента
     * @see VkGroups#searchGroup(String, User)
     */
    public String getGroupId(String userReceivedGroupName, User userCallingMethod)
            throws NoGroupException, ClientException, ApiException {
        return String.valueOf(groups.searchGroup(userReceivedGroupName, userCallingMethod).getId());
    }

    /**
     * Метод для подписки пользователя(сохранение в базу данных id пользователя в телеграмме и группы)
     *
     * @param userReceivedGroupName строка по которой будет искаться группа, полученная от пользователя
     * @param userCallingMethod     пользователь вызвавший метод
     * @return статус подписки на группу, {@link SubscribeStatus#SUBSCRIBED} - означает что пользователь успешно подписан,
     * {@link SubscribeStatus#ALREADY_SUBSCRIBED} - сообщает, что пользователь уже подписан на эту группу,
     * {@link SubscribeStatus#GROUP_IS_CLOSED} - сообщает, что невозможно подписаться, тк группа закрыта
     * @throws ApiException     возникает при ошибке обращения к vk api со стороны vk
     * @throws NoGroupException возникает если не нашлась группа по заданной подстроке
     * @throws ClientException  возникает при ошибке обращения к vk api со стороны клиента
     * @see VkGroups#searchGroup(String, User)
     * @see GroupsStorage#addInfoToGroup(String, String)
     */
    public SubscribeStatus subscribeTo(GroupsStorage groupBase, String userReceivedGroupName, User userCallingMethod)
            throws ApiException, NoGroupException, ClientException {
        Group userFindGroup = groups.searchGroup(userReceivedGroupName, userCallingMethod);

        if (userFindGroup.getIsClosed() == GroupIsClosed.CLOSED) {
            return SubscribeStatus.GROUP_IS_CLOSED;
        }

        //TODO synchronize working with subscribers
        boolean isSubscribed = groupBase.addInfoToGroup(userFindGroup.getScreenName(), userCallingMethod.getTelegramId());
        return isSubscribed ? SubscribeStatus.SUBSCRIBED : SubscribeStatus.ALREADY_SUBSCRIBED;
    }

    /**
     * Метод для получения последних {@code amountOfPosts} постов со стены по {@code userReceivedGroupName}
     *
     * @param amountOfPosts         - кол-во постов
     * @param userReceivedGroupName - имя группы
     * @param userCallingMethod     - пользователь вызвавший метод
     * @return возвращает последние amountOfPosts постов
     * @throws ApiException             - возникает при ошибке обращения к vk api со стороны vk
     * @throws NoGroupException         - возникает если не нашлась группа по заданной подстроке
     * @throws ClientException          - возникает при ошибке обращения к vk api со стороны клиента
     * @throws IllegalArgumentException - возникает при передаче кол-ва постов большего, чем можно получить(max 100).
     *                                  Возникает при вызове пользователем не имеющем доступа к этому методу(пример из vk sdk GroupActor)
     * @see VkGroups#searchGroup(String, User)
     * @see VkWall#getLastPostsStrings(String, int, User)
     */
    public Optional<List<String>> getLastPosts(String userReceivedGroupName, int amountOfPosts, User userCallingMethod)
            throws NoGroupException, ClientException, ApiException {
        Group userFindGroup = groups.searchGroup(userReceivedGroupName, userCallingMethod);
        return wall.getLastPostsStrings(userFindGroup.getScreenName(), amountOfPosts, userCallingMethod);
    }

    /**
     * Метод обертка для получения новых постов из группы в базе данных
     *
     * @param groupsStorage   - база данных
     * @param groupScreenName - название группы в базе данных
     * @return список постов в группе в виде строк
     * @throws ApiException    - возникает при ошибке обращения к vk api со стороны vk
     * @throws ClientException - возникает при ошибке обращения к vk api со стороны клиента
     * @see VkWall#getLastPostsStrings(String, int, User)
     */
    public Optional<List<String>> getNewPosts(GroupsStorage groupsStorage, String groupScreenName)
            throws ClientException, ApiException {
        return wall.getNewPostsStrings(groupsStorage, groupScreenName, vkApp);
    }
}
