package ru.systemsez.examples.frwt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppManager extends Thread{

    private AppFileReader appFileReader;
    private AppFileWriter appFileWriter;
    private List<AppExecutor> appExecutors;

    private PoolForProcessing poolForProcessing;
    private PoolForWriting poolForWriting;

    private Integer readsBlocks;

    public AppManager( File fileIn, File fileOut, int countExecutors, int appBlockSize){
        super( "AppManager" );
        poolForProcessing = new PoolForProcessing();
        poolForWriting    = new PoolForWriting();
        appFileReader     = new AppFileReader( fileIn, appBlockSize, poolForProcessing );
        appFileWriter     = new AppFileWriter( fileOut, appBlockSize, poolForWriting );

        //Создаем список исполнителей
        appExecutors = new ArrayList<>();
        for( int i = 0; i < countExecutors; i++ ){
            appExecutors.add( new AppExecutor( i, poolForProcessing, poolForWriting ));
        }
    }

    @Override
    public void run(){

        //Запускаем список работников
        for( Thread executor : appExecutors ){
            executor.start();
        }
        appFileReader.start(); //запускаем читателя
        appFileWriter.start(); //запускаем писателя

        try{
            monitorExecuting();
        }catch( InterruptedException e ){
            e.printStackTrace();
        }
        //выход
    }


    private void monitorExecuting() throws InterruptedException{
        //дожидаемся пока читатель прочтет весь файл
        appFileReader.join();

        //мониторим пул на обработку,когда он станет пустой
        while(!poolForProcessing.isEmpty()){
            sleep( 100 );
        }

        //можно передавать сигналы на остановку обработчиков
        for( Thread executor : appExecutors ){
            executor.interrupt();
        }

        //дожидаемся остановки обработчиков
        for( Thread executor : appExecutors ){
            executor.join();
        }

        //все обработчики остановлены - значит все блоки были переданы
        //в пул для записи в файл - как только он станет пустым
        while( !poolForWriting.isEmpty() ){
            sleep( 100 );
        }

        //все блоки записаны, останавливаем поток писателя
        appFileWriter.interrupt();
        appFileWriter.join();

        //все дочерние потоки остановлены
    }

}
