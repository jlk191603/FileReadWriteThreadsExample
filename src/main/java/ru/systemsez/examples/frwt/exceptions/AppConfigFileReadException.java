package ru.systemsez.examples.frwt.exceptions;

public class AppConfigFileReadException extends InitConfigAppException{

    public AppConfigFileReadException( Throwable cause ){
        super( "Ошибка чтения файла конфигурации: " + cause.getMessage());
    }
}
