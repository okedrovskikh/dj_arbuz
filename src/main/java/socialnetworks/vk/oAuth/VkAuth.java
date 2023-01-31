<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
package dj.arbuz.socialnetworks.vk.oAuth;
========
package socialnetworks.vk.oAuth;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.UserAuthResponse;
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
import dj.arbuz.BotTextResponse;
import dj.arbuz.socialnetworks.socialnetwork.SocialNetworkException;
import dj.arbuz.socialnetworks.vk.oAuth.OAuthCodeQueue.MessageQueuePuller;
import dj.arbuz.user.BotUser;

import java.nio.file.Path;
========
import httpserver.server.HttpServer;
import user.BotUser;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java

/**
 * Класс для аутентификации пользователей
 *
 * @author Кедровских Олег
 * @version 2.0
 * @see AbstractVkAuth
 */
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
public final class VkAuth extends AbstractVkAuth {
========
public class VkAuth extends AbstractVkAuth {
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java
    /**
     * Поле класс позволяющего работать с vk api
     *
     * @see VkApiClient
     */
    private final VkApiClient vkApiClient;
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
    private final OAuthCodeQueue oAuthCodeQueue;
========
    /**
     * Поле сервера получающего токены пользователя и переправляющего пользователей на tg бота
     *
     * @see HttpServer
     */
    private final HttpServer httpServer;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java
    /**
     * Поле с конфигурации данных для аутентификации пользователь и приложения
     *
     * @see VkAuthConfiguration
     */
    private final VkAuthConfiguration authConfiguration;

    /**
     * Конструктор - создает экземпляр класса
     *
     * @param vkApiClient                    клиент vk
     * @param vkAppConfigurationJsonFilePath путь до json файла с конфигурацией
     */
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
    public VkAuth(VkApiClient vkApiClient, OAuthCodeQueue oAuthCodeQueue, Path vkAppConfigurationJsonFilePath) {
        this.vkApiClient = vkApiClient;
        this.oAuthCodeQueue = oAuthCodeQueue;
========
    public VkAuth(VkApiClient vkApiClient, HttpServer httpServer, String vkAppConfigurationJsonFilePath) {
        this.vkApiClient = vkApiClient;
        this.httpServer = httpServer;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java
        this.authConfiguration = VkAuthConfiguration.loadVkAuthConfigurationFromJson(vkAppConfigurationJsonFilePath);
    }

    /**
     * Метод создающий пользователя приложения
     *
     * @return пользователя приложения
     * @see ServiceActor#ServiceActor(Integer, String, String)
     * @see VkAuthConfiguration#APP_ID
     * @see VkAuthConfiguration#CLIENT_SECRET
     * @see VkAuthConfiguration#SERVICE_CLIENT_SECRET
     */
    @Override
    public ServiceActor createAppActor() {
        return new ServiceActor(
                authConfiguration.APP_ID, authConfiguration.CLIENT_SECRET, authConfiguration.SERVICE_CLIENT_SECRET
        );
    }

    /**
     * Метод возвращающий ссылку для аутентификации
     *
     * @return ссылку для аутентификации, если сервер недоступен, то это null
     * @see VkAuthConfiguration#AUTH_URL
     */
    @Override
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
    public String getAuthUrl(String userTelegramId) {
        return authConfiguration.AUTH_URL + "&state=" + userTelegramId;
========
    public String getAuthUrl() {
        return authConfiguration.AUTH_URL;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java
    }

    /**
     * Метод создающий пользователя.
     * Создается с помощью Vk Java SDK, получая код с сервера
     *
     * @param userTelegramId id пользователя в системе
     * @return нового пользователя, null если возникли проблемы при обращении к серверу, при ошибках на сервере
     * или при ошибке обращения к vk api
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
========
     * @see HttpServer#getHttpRequestParameters()
     * @see VkAuth#getAuthCodeFromHttpRequest(String)
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java
     * @see VkAuthConfiguration#APP_ID
     * @see VkAuthConfiguration#CLIENT_SECRET
     * @see VkAuthConfiguration#REDIRECT_URL
     */
    @Override
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
    public BotUser createBotUser(String userTelegramId) throws SocialNetworkException {
        String oAuthCode;
        try (MessageQueuePuller oAuthCodeQueuePuller = oAuthCodeQueue.subscribe(userTelegramId)) {
            oAuthCode = oAuthCodeQueuePuller.pollMessage();
        }

        if (oAuthCode == null) {
            return null;
        }

========
    public BotUser createBotUser(String systemUserId) {
        String httpRequestGetParameters = httpServer.getHttpRequestParameters();

        if (httpRequestGetParameters == null) {
            return null;
        }

        String authCode = getAuthCodeFromHttpRequest(httpRequestGetParameters);
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java
        try {
            UserAuthResponse authResponse = vkApiClient.oAuth().userAuthorizationCodeFlow(
                            authConfiguration.APP_ID,
                            authConfiguration.CLIENT_SECRET,
                            authConfiguration.REDIRECT_URL,
                            oAuthCode)
                    .execute();
<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/vk/oAuth/VkAuth.java
            return new BotUser(authResponse.getUserId(), authResponse.getAccessToken(), userTelegramId);
========
            return new BotUser(authResponse.getUserId(), authResponse.getAccessToken(), systemUserId);
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/vk/oAuth/VkAuth.java
        } catch (ApiException | ClientException e) {
            System.err.println(e.getMessage());
            throw new SocialNetworkException(BotTextResponse.AUTH_ERROR);
        }
    }
}
