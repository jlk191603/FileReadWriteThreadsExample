package ru.systemsez.examples.frwt;

import java.io.*;
import java.util.Arrays;

public class AppFileReader extends Thread{

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
            fileInRead( bufferedInputStream);
        }catch( IOException e ){
            e.printStackTrace();
        }
    }

    private void fileInRead( BufferedInputStream bufferedInputStream ) throws IOException{
        byte[] buffer;
        int countReadBytes;
        int blockNum = 0;
        AppBlock block;
        do{
            buffer = new byte[blockSize];
            countReadBytes = bufferedInputStream.read(buffer);
            if(countReadBytes < blockSize){
                buffer = Arrays.copyOfRange( buffer,0,countReadBytes);
            }
            block = new AppBlock( blockNum++, buffer  );
            poolForProcessing.add( block );
        }while( countReadBytes == blockSize );
    }

}
