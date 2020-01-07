package ru.systemsez.examples.frwt;

public class AppExecutor extends Thread{

    private final int numExecutor;
    private PoolForProcessing poolForProcessing;
    private PoolForWriting poolForWriting;

    public AppExecutor( int numExecutor, PoolForProcessing poolForProcessing,
                        PoolForWriting poolForWriting ){
        super( "AppExecutor" + numExecutor );
        this.numExecutor       = numExecutor;
        this.poolForProcessing = poolForProcessing;
        this.poolForWriting    = poolForWriting;
    }

    @Override
    public void run(){
        while( !isInterrupted() ){
            if(!poolForProcessing.isEmpty()){
                AppBlock block = poolForProcessing.poll();
                logicExecutor( block);
                poolForWriting.add( block );
            } else {
                try{
                    sleep( 500 );
                }catch( InterruptedException e ){
                    break;
                }
            }
        }
    }


    private void logicExecutor( AppBlock block ){

    }

}
