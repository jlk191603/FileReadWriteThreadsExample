package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppManager extends Thread{

    private static final Logger logger = LogManager.getLogger( AppManager.class);

    private AppFileReader appFileReader;
    private AppFileWriter appFileWriter;
    private List<AppExecutor> appExecutors;

    private PoolForProcessing poolForProcessing;
    private PoolForWriting poolForWriting;

    private Integer readsBlocks;

    public AppManager( File fileIn, File fileOut, int countExecutors, int appBlockSize){
        super( "AppManager" );
        logger.trace( "constructor IN" );
        poolForProcessing = new PoolForProcessing();
        logger.trace( "constructor создал очередь на обработку для блоков" );
        poolForWriting    = new PoolForWriting();
        logger.trace( "constructor создал очередь на запись файл для блоков" );
        appFileReader     = new AppFileReader( fileIn, appBlockSize, poolForProcessing );
        logger.trace( "constructor создал поток чтения файла" );
        appFileWriter     = new AppFileWriter( fileOut, appBlockSize, poolForWriting );
        logger.trace( "constructor создал поток записи в файл" );

        //Создаем список исполнителей
        appExecutors = new ArrayList<>();
        for( int i = 0; i < countExecutors; i++ ){
            appExecutors.add( new AppExecutor( i, poolForProcessing, poolForWriting ));
        }
        logger.trace( "constructor создал потоки исполнителей в количестве " + countExecutors );
        logger.trace( "constructor OUT" );
    }

    @Override
    public void run(){
        logger.trace( "менеджер запущен" );
        //Запускаем список работников
        for( Thread executor : appExecutors ){
            executor.start();
        }
        logger.debug( "потоки исполнителей запущены" );

        appFileReader.start(); //запускаем читателя
        logger.debug( "поток читателя файла запущен" );

        appFileWriter.start(); //запускаем писателя
        logger.debug( "поток писателя файла запущен" );

        try{
            monitorExecuting();
        }catch( InterruptedException e ){
            e.printStackTrace();
        }
        //выход
    }


    private void monitorExecuting() throws InterruptedException{
        logger.trace("monitorExecuting() IN");

        //дожидаемся пока читатель прочтет весь файл
        appFileReader.join();
        logger.debug( "поток читателя завершен" );

        //мониторим пул на обработку,когда он станет пустой
        while(!poolForProcessing.isEmpty()){
            sleep( 100 );
        }
        logger.debug( "пул блоков на обработку пустой" );

        //можно передавать сигналы на остановку обработчиков
        for( Thread executor : appExecutors ){
            executor.interrupt();
        }
        logger.debug( "остановка потоков исполнителей" );

        //дожидаемся остановки обработчиков
        for( Thread executor : appExecutors ){
            executor.join();
        }
        logger.debug( "потоки исполнителей остановлены" );

        //все обработчики остановлены - значит все блоки были переданы
        //в пул для записи в файл - как только он станет пустым
        while( !poolForWriting.isEmpty() ){
            sleep( 100 );
        }
        logger.debug( "пул блоков на запись пустой" );

        //все блоки записаны, останавливаем поток писателя
        appFileWriter.interrupt();
        logger.debug( "останавливаю поток записи в файл" );

        appFileWriter.join();
        logger.debug( " поток записи в файл остановлен" );

        //все дочерние потоки остановлены
        logger.trace("monitorExecuting() OUT");
    }

}
