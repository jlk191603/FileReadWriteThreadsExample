package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Исполнитель задания над блоком текста
 */
public class AppExecutor extends Thread{

    private final Logger logger;

    private final int numExecutor;
    private PoolForProcessing poolForProcessing;
    private PoolForWriting poolForWriting;

    public AppExecutor( int numExecutor, PoolForProcessing poolForProcessing,
                        PoolForWriting poolForWriting ){
        super( "AppExecutor" + numExecutor );
        logger = LogManager.getLogger( AppExecutor.class + "[ " + getName() + " ]");
        this.numExecutor       = numExecutor;
        this.poolForProcessing = poolForProcessing;
        this.poolForWriting    = poolForWriting;
    }

    @Override
    public void run(){
        logger.info( "started" );
        Integer blockNum;
        while( !isInterrupted() ){
            if(!poolForProcessing.isEmpty()){
                AppBlock block = poolForProcessing.poll();
                blockNum = block.getNumber();
                logger.debug( "для обработки получен блок " +  blockNum );
                logicExecutor( block);
                logger.debug( "блок " + blockNum + " успешно обработан"  );
                poolForWriting.add( block );
                logger.debug( "блок " + blockNum + " передан в очередь на запись"  );
            } else {
                try{
                    sleep( 500 );
                    logger.debug( "sleep" );
                }catch( InterruptedException e ){
                    break;
                }
            }
        }
        logger.info( "interrupting" );
    }


    private void logicExecutor( AppBlock block ){
        logger.trace( "logicExecutor( AppBlock) IN"  );
        logger.trace( "logicExecutor( AppBlock) OUT"  );
    }

}
