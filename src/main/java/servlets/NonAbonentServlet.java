package servlets;

/**
 * Интерфейс-маркер для сервлетов, которые выполняют псевдо-атомарные операции (односторонние)
 */
public interface NonAbonentServlet {
    void createMessage();
}
