package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class PoolForProcessing{

    private static final Logger logger = LogManager.getLogger( PoolForProcessing.class);

    private final Queue<AppBlock> data;

    public PoolForProcessing(){
        data = new LinkedTransferQueue<>();
    }

    public void add(AppBlock appBlock){
        synchronized( data ){
            data.add( appBlock );
        }
        logger.debug( "add(AppBlock) в очереди на обработку блоков" + data.size() );
    }

    public AppBlock poll(){
        synchronized( data ){
            return data.poll();
        }
    }

    public Boolean isEmpty(){
        synchronized( data ){
            return data.isEmpty();
        }
    }

}
