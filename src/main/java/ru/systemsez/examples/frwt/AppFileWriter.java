package ru.systemsez.examples.frwt;

import java.io.*;

/**
 * Записывает данные в файл
 */
public class AppFileWriter extends Thread{

    private File fileOut;
    private Integer blockSize;
    private PoolForWriting poolForWriting;
    private int numNextBlock = 0;

    public AppFileWriter( File fileOut, Integer blockSize, PoolForWriting poolForWriting ){
        super( "AppFileWriter" );
        this.fileOut        = fileOut;
        this.blockSize      = blockSize;
        this.poolForWriting = poolForWriting;
    }

    @Override
    public void run(){
        try( FileOutputStream fileOutputStream = new FileOutputStream( fileOut );
            BufferedOutputStream bufferedOutputStream =
                new BufferedOutputStream(fileOutputStream, blockSize )){
            fileOutWrite(bufferedOutputStream);
        }catch( IOException e ){
            e.printStackTrace();
        }
    }


    private void fileOutWrite( BufferedOutputStream bufferedOutputStream )throws IOException{
        int numNextBlock = 0;
        while(!isInterrupted()){
            AppBlock appBlock = poolForWriting.getBlock( numNextBlock );
            if( appBlock != null ){
                bufferedOutputStream.write( appBlock.getData() );
                numNextBlock += 1;
            }else{
                try{
                    sleep(100);
                }catch( InterruptedException e ){
                    break;
                }
            }
        }
    }
}
