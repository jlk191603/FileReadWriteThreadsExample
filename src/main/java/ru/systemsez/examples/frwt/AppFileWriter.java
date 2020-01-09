package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Записывает данные в файл
 */
public class AppFileWriter extends Thread{

    private static final Logger logger = LogManager.getLogger( AppFileWriter.class);

    private File fileOut;
    private Integer blockSize;
    private PoolForWriting poolForWriting;

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
            logger.debug( "пишу файл..." );
            fileOutWrite(bufferedOutputStream);
            logger.debug( "файл записан :)" );
        }catch( IOException e ){
            e.printStackTrace();
        }
    }


    private void fileOutWrite( BufferedOutputStream bufferedOutputStream )throws IOException{
        logger.trace( "fileOutWrite( BufferedOutputStream  ) IN ");
        int numNextBlock = 0;
        while(!isInterrupted()){
            logger.trace( "fileOutWrite( BufferedOutputStream  ) попытка получить блок " + numNextBlock);
            AppBlock appBlock = poolForWriting.getBlock( numNextBlock );
            if( appBlock != null ){
                logger.trace( "fileOutWrite( BufferedOutputStream  ) блок получен - записываю" );
                bufferedOutputStream.write( appBlock.getData() );
                logger.trace( "fileOutWrite( BufferedOutputStream  ) блок записан" );
                numNextBlock += 1;
            }else{
                logger.trace( "fileOutWrite( BufferedOutputStream  ) блок не получен - сплю" );
                try{
                    sleep(100);
                }catch( InterruptedException e ){
                    logger.trace( "fileOutWrite( BufferedOutputStream  ) поток остановили пока спал" );
                    break;
                }
            }
        }
        logger.trace( "fileOutWrite( BufferedOutputStream  ) OUT ");
    }
}
