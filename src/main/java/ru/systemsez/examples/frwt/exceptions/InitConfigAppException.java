package ru.systemsez.examples.frwt.exceptions;

public class InitConfigAppException extends Exception{

    public InitConfigAppException(String info){
        super( "Ошибка инициализации параметров конфигурации приложения: " + info);
    }

}
