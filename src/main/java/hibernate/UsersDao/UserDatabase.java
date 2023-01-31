package hibernate.UsersDao;

import database.UserBase;
import hibernate.entity.UserData;
import user.BotUser;

/**
 * Класс для работы с таблицей юзеров в бд
 */
public class UserDatabase implements UserBase {
    /** Репозиторий для взаимодействия с таблицей юзеров **/
    private final UserDao userDao = new UserDao();

    /**
     * Метод для добавления новой информации юзера
     * @param userId Айди юзера
     * @param botUser Данные юзера
     */
    public boolean addInfoUser(String userId, BotUser botUser) {
        UserData tempUser = new UserData();
        tempUser.setTelegramId(userId);
        tempUser.setUserId(botUser.getId());
        tempUser.setAccessToken(botUser.getAccessToken());
        userDao.updateUser(tempUser);
        return true;
    }

    /**
     * Метод для проверки наличия юзера в бд
     */
    public boolean contains(String userId) {
        return userDao.getUser(userId) != null;
    }

    /**
     * Метод для получения юзера
     * @param userId айди юзера в телеграме
     * @return Entity класс юзера взятый из бд
     */
    public BotUser getUser(String userId) {
        return new BotUser(userDao.getUser(userId));
    }

}
