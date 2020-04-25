package servlets;

/**
 * Интерфейс для сервлетов, которые выполняют односторонние операции (Не ожидают результат на запрос)
 */
public interface NonAbonentServlet {
    void createMessage();
}
