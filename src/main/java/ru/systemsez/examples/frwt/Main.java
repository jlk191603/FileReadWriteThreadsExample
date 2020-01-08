package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main{

    private static App app;
    private static String configFilePath;
    private static final Logger logger = LogManager.getLogger( Main.class);


    /**
     * Старт приложения и проверка входных параметров
     * @param args параметры запуска приложения,
     *             ожидается что нулевым параметром будет
     *             путь к файлу с настройками приложения
     */
    public static void main( String[] args ){
        logger.trace( "main(String[]) IN" );

        //Проверяем что нам вообще передали аргумент
        if( args.length != 1 ){
            String errMsg = "main(String[]) Не указан путь к файлу конфигурации программы";
            logger.error( errMsg );
            throw new Error( errMsg );
        }

        //Предполагаем что нам передали путь к файлу с настройками
        configFilePath = args[0];
        app            = App.getInstance();
        logger.trace( "main(String[]) OUT" );
    }


    /**
     * Возвращает путь к файлу конфигурации
     */
    public static String getConfigFilePath(){
        logger.debug( "getConfigFilePath() путь к файлу конфигурации: " + configFilePath );
        return configFilePath;
    }

}
