package database;

import com.google.gson.reflect.TypeToken;
import loaders.gson.GsonLoader;
import user.BotUser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.HashMap;

public class UserStorage implements UserBase {
    private Map<String, BotUser> usersBase;

    private static UserStorage userStorage = null;

    /**
     * Метод для добавления информации в базу данных
     *
     * @param userId  id юзера в телеграмме
     * @param botUser данные юзера
     * @return {@code true}
     */
    public boolean addInfoUser(String userId, BotUser botUser) {
        usersBase.put(userId, botUser);
        return true;
    }

    /**
     * Метод для сохранения базы данных в json файл
     */
    public void saveToJsonFile() {
        Type userStorageMapType = new TypeToken<Map<String, BotUser>>() {
        }.getType();
        GsonLoader<Map<String, BotUser>> loader = new GsonLoader<>(userStorageMapType);
        try {
            loader.loadToJson("src/main/resources/anonsrc/database_for_users.json", usersBase);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для получения информации с json файла
     */
    public void returnStorageFromDatabase() {
        Type userStorageMapType = new TypeToken<Map<String, BotUser>>() {
        }.getType();
        GsonLoader<Map<String, BotUser>> loader = new GsonLoader<>(userStorageMapType);
        try {
            usersBase = loader.loadFromJson("src/main/resources/anonsrc/database_for_users.json");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Инициализация бота
     *
     * @return Базу с юзерами
     */
    public static UserStorage getInstance() {
        if (userStorage == null) {
            userStorage = new UserStorage();
        }
        return userStorage;
    }

    /**
     * Метод для получения базы с юзерами
     */
    private UserStorage() {
        usersBase = new HashMap<>();
        returnStorageFromDatabase();
    }

    /**
     * Метод для проверки ключей в базе
     */
    public boolean contains(String userId) {
        return usersBase.containsKey(userId);
    }

    /**
     * Метод для оплучения значения по ключу
     *
     * @param userId ключ с id юзера в телеграмме
     * @return подписки на группы
     */
    public BotUser getUser(String userId) {
        return usersBase.get(userId);
    }
}
