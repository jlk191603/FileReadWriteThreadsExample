package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main{

    private static App app;
    private static String managerConfigFilePath;
    private static final Logger logger = LogManager.getLogger( Main.class);


    /**
     * Старт приложения и проверка входных параметров
     * @param args параметры запуска приложения,
     *             ожидается что нулевым параметром будет
     *             путь к файлу с настройками приложения
     */
    public static void main( String[] args ){
        logger.trace( "START" );

        //Проверяем что нам вообще передали аргумент
        if( args.length != 1 ){
            String errMsg = "Не указан путь к файлу конфигурации программы";
            logger.error( errMsg );
            throw new Error( errMsg );
        }

        //Предполагаем что нам передали путь к файлу с настройками
        managerConfigFilePath = args[0];
        app = App.getInstance();
    }


    /**
     * Возвращает путь к файлу конфигурации
     */
    public static String getManagerConfigFilePath(){
        logger.trace( "Main.getManagerConfigFilePath()" );
        return managerConfigFilePath;
    }

}
