package bots.console;

import bots.BotMessageExecutable;
import handlers.messages.MessageHandler;
import handlers.notifcations.ConsolePostsPullingThread;
import bots.StoppableByUser;
import httpserver.server.HttpServer;
import stoppable.StoppableThread;

import java.util.Scanner;

/**
 * Класс потока обрабатывающего сообщения пользователя с консоли
 *
 * @author Кедровских Олег
 * @version 1.2
 * @see StoppableThread
 * @see StoppableByUser
 */
public class ConsoleBot extends StoppableThread implements StoppableByUser, BotMessageExecutable {
    /**
     * Поле id пользователя консольной версии бота
     */
    private static final String defaultConsoleUserId = "consoleUser";
    /**
     * Поле класса исполняющего команды полученные от пользователя
     *
     * @see ConsoleMessageExecutor
     */
    private final ConsoleMessageExecutor messageExecutor;

    /**
     * Конструктор - создает экземпляр класса
     */
    public ConsoleBot() {
        super(defaultConsoleUserId);
        messageExecutor = new ConsoleMessageExecutor(this);
    }

    public static void main(String[] args) {
        HttpServer httpServer = HttpServer.getInstance();

        if (httpServer == null) {
            throw new RuntimeException("Не удалось настроить сервер");
        }

        httpServer.start();

        ConsoleBot consoleBot = new ConsoleBot();
        consoleBot.start();
        while (consoleBot.isWorking()) {
            Thread.onSpinWait();
        }
        consoleBot.stopWithInterrupt();
        httpServer.stop();
    }

    /**
     * Метод с логикой выполняемой внутри потока
     *
     * @see StoppableThread#run()
     * @see MessageHandler#handleMessage(String, String, StoppableByUser)
     * @see ConsolePostsPullingThread#start()
     * @see ConsolePostsPullingThread#stopWithInterrupt()
     */
    @Override
    public void run() {
        messageExecutor.start();
        Scanner userInput = new Scanner(System.in);
        while (working.get()) {

            if (userInput.hasNextLine()) {
                messageExecutor.executeTextMessage(userInput.nextLine(), defaultConsoleUserId, this);
            }

        }
        working.set(false);
        userInput.close();
        messageExecutor.stop();
    }

    /**
     * Реализация интерфейса для отправки сообщения пользователю, выводит сообщение в консоль
     *
     * @param userSendResponseId  id пользователя, которому необходимо отправить сообщение
     * @param responseSendMessage сообщение, которое будет отправлено пользователю
     */
    @Override
    public void send(String userSendResponseId, String responseSendMessage) {

        if (!userSendResponseId.equals(defaultConsoleUserId)) {
            return;
        }

        System.out.println(responseSendMessage);
    }

    /**
     * Метод реализующий интерфейс для остановки бота пользователем
     */
    @Override
    public void stopByUser() {
        stopWithInterrupt();
    }
}
