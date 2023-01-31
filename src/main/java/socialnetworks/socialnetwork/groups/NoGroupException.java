<<<<<<<< HEAD:common/src/main/java/dj/arbuz/socialnetworks/socialnetwork/groups/NoGroupException.java
package dj.arbuz.socialnetworks.socialnetwork.groups;
========
package socialnetworks.socialnetwork.groups;
>>>>>>>> 71a290e7ae7d585b86849c65deeead77413261ce:src/main/java/socialnetworks/socialnetwork/groups/NoGroupException.java

/**
 * Класс ошибки при ненахождении группы
 *
 * @author Кедровских Олег
 * @version 2.0
 */
public class NoGroupException extends Exception {
    /**
     * Конструктор - унаследованный от родительского класса
     *
     * @param groupName - имя группы
     */
    public NoGroupException(String groupName) {
        super("Группы с названием " + groupName + " не существует");
    }

    /**
     * Конструктор - унаследованный от родительского класса
     *
     * @param groupId - id группы
     */

    public NoGroupException(int groupId) {
        super("Группы с id " + groupId + " не существует");
    }
}
