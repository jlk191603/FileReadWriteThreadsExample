package ru.systemsez.examples.frwt;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class PoolForProcessing{

    private final Queue<AppBlock> data;

    public PoolForProcessing(){
        data = new LinkedTransferQueue<>();
    }

    public void add(AppBlock appBlock){
        synchronized( data ){
            data.add( appBlock );
        }
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
