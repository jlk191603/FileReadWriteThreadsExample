package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;

public class AppFileReader extends Thread{

    private static final Logger logger = LogManager.getLogger( AppFileReader.class);

    private File fileIn;
    private Integer blockSize;
    private PoolForProcessing poolForProcessing;

    public AppFileReader( File fileIn, Integer blockSize, PoolForProcessing poolForProcessing ){
        super( "AppFileReader" );
        this.fileIn = fileIn;
        this.blockSize = blockSize;
        this.poolForProcessing = poolForProcessing;
    }

    @Override
    public void run(){
        try( FileInputStream fileInputStream = new FileInputStream( fileIn );
             BufferedInputStream bufferedInputStream = new BufferedInputStream( fileInputStream, blockSize )){
            logger.info("читаю файл ...");
            fileInRead( bufferedInputStream);
            logger.info("чтение файла завершено");
        }catch( IOException e ){
            e.printStackTrace();
        }
    }

    private void fileInRead( BufferedInputStream bufferedInputStream ) throws IOException{
        logger.trace("fileInRead( BufferedInputStream ) IN"  );
        byte[] buffer;
        int countReadBytes;
        int blockNum = 0;
        AppBlock block;
        do{
            buffer = new byte[blockSize];
            logger.debug("fileInRead( BufferedInputStream ) читаю в buffer " + blockNum);
            countReadBytes = bufferedInputStream.read(buffer);
            logger.trace("fileInRead( BufferedInputStream ) прочитано байт " + countReadBytes);
            if(countReadBytes < blockSize){
                buffer = Arrays.copyOfRange( buffer,0,countReadBytes);
            }
            logger.trace("fileInRead( BufferedInputStream ) создаю новый блок " + blockNum);
            block = new AppBlock( blockNum++, buffer  );
            logger.trace("fileInRead( BufferedInputStream ) блок создан. " +
                         "Добавляю в очередь на обработку");
            poolForProcessing.add( block );
            logger.debug("fileInRead( BufferedInputStream ) блок добавлен в очередь обработки");

        }while( countReadBytes == blockSize );
        logger.trace("fileInRead( BufferedInputStream ) OUT"  );
    }

}
