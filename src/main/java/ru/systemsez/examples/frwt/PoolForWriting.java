package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class PoolForWriting{

    private final List<AppBlock> data;
    private static final Logger logger = LogManager.getLogger( PoolForWriting.class);
    public PoolForWriting(){
        this.data = new ArrayList<>();
    }


    public void add( AppBlock appBlock){
        if(appBlock == null){
            return;
        }
        synchronized( data ){
            data.add( appBlock );
        }
        logger.debug( "add( AppBlock ) в очереди на запись блоков "  + data.size());
    }


    public AppBlock getBlock( int nextBlock ){
        AppBlock block;
        synchronized( data ){
            block = findBlock( nextBlock );
            if( block != null ){
                data.remove( block );
                logger.debug( "getBlock( int ) в очереди на запись блоков "  + data.size());
            }
        }
        return block;
    }


    public Boolean isEmpty(){
        return data.isEmpty();
    }


    private AppBlock findBlock( int blockNumber ){
        if(data.isEmpty()){
            return null;
        }
        for( AppBlock block : data ){
            if(block!=null && block.getNumber() == blockNumber){
                return block;
            }
        }
        return null;
    }



}
