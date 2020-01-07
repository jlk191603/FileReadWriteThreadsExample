package ru.systemsez.examples.frwt;

import java.util.ArrayList;
import java.util.List;


public class PoolForWriting{

    private final List<AppBlock> data;

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
    }


    public AppBlock getBlock( int nextBlock ){
        AppBlock block;
        synchronized( data ){
            block = findBlock( nextBlock );
            if( block != null ){
                data.remove( block );
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
