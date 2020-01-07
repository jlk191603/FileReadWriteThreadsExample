package ru.systemsez.examples.frwt.exceptions;

public class AppConfigFileNotFoundException extends InitConfigAppException{

    public AppConfigFileNotFoundException(){
        super( "Файл конфигурации приложения не найден" );
    }

}
