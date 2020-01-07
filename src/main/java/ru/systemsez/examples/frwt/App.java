package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.systemsez.examples.frwt.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class App{

    private static final Logger logger = LogManager.getLogger( App.class);

    private static App instance;
    private AppManager appManager;

    private App(Config config) throws FileInReadException, FileOutException{
        appManager = new AppManager( config.getFileIn(), config.getFileOut(),
                                     config.getCountExecutors(), config.getAppBlockSize() );
        start();
    }

    private void start(){
        appManager.start();
    }

    public synchronized static App getInstance(){
        if( instance == null ){
            try{
                instance = new App( Config.getInstance() );
            }catch( Exception e ){
                System.out.println(e.getMessage());
                System.exit( -1 );
            }
        }
        return instance;
    }

    public static class Config{

        private static final int COUNT_EXECUTORS_DEFAULT = 1;
        private static final int READ_BLOCK_SIZE_DEFAULT = 50;

        private static Config instance;
        private static Properties properties;

        private Integer countExecutors;
        private Integer appBlockSize;
        private File fileIn;
        private File fileOut;


        private Config() throws InitConfigAppException{
            initConfigProperties();
        }

        public static Config getInstance() throws InitConfigAppException{
            if(instance == null){
                instance = new Config();
            }
            return instance;
        }


        /**
         * Возвращает количество исполнителей.
         * При первом обращении к методу, параметр берется из properties.
         */
        public Integer getCountExecutors(){
            if( countExecutors == null ){
                setCountExecutors();
            }
            return countExecutors;
        }


        /**
         * Берем значение свойства из файла конфигурации.
         * В случаях если:
         * <ul>
         *     <li>свойство не найдено файле конфигурации;</li>
         *     <li>значение свойства было задано, но его не удалось преобразовать к целому числу;</li>
         *     <li>если задано число меньше или равное нулю;</li>
         * </ul>
         * устанавливаем значение свойства в {@link #COUNT_EXECUTORS_DEFAULT}.
         */
        private void setCountExecutors(){

            String strVal = properties.getProperty( "countExecutors",
                                                    String.valueOf( COUNT_EXECUTORS_DEFAULT ) );
            int count;
            try{
                count = Integer.parseInt( strVal );
            }catch( Exception ex ){
                count = COUNT_EXECUTORS_DEFAULT;
            }

            this.countExecutors = count > 0 && count < 11  ? count : COUNT_EXECUTORS_DEFAULT;
        }


        public Integer getAppBlockSize(){
            if( appBlockSize == null){
                setAppBlockSize();
            }
            return appBlockSize;
        }


        public void setAppBlockSize(){
            String strVal = properties.getProperty( "blockSize",
                                                    String.valueOf( READ_BLOCK_SIZE_DEFAULT ));
            int count;
            try{
                count = Integer.parseInt( strVal );
            }catch( Exception ex ){
                count = READ_BLOCK_SIZE_DEFAULT;
            }

            this.appBlockSize = count > 19 && count < 201 ? count : READ_BLOCK_SIZE_DEFAULT;
        }


        /**
         * Возвращает путь к файлу с входящими данными,
         * @return путь к файлу с входящими данными
         * @throws FileInReadException путь к файлу с входящими данными не задан
         * или файл не может быть прочитан
         */
        public File getFileIn() throws FileInReadException{
            if(fileIn == null){
                setFileIn();
            }
            return fileIn;
        }


        private void setFileIn() throws FileInReadException{
            String strVal = properties.getProperty( "filePathIn","" );
            File fileIn = new File(strVal);
            if( !fileIn.exists() ){
                throw new FileInPathNotFoundException();
            }
            if( !fileIn.canRead() ){
                throw new FileInCanNotBeReadException();
            }
            this.fileIn = fileIn;
        }

        /**
         * Возвращает путь для файла результата
         * @return путь для файла результата
         * @throws FileOutException путь для файла результата не задан
         */
        public File getFileOut() throws FileOutException{
            if(fileOut == null){
                setFileOut();
            }
            return fileOut;
        }


        private void setFileOut() throws FileOutException{
            String strVal = properties.getProperty( "filePathOut","" );
            if(strVal == null || strVal.isEmpty() ){
                throw new FileOutPathNotFoundException();
            }
            File fileOut = new File(strVal);

            if( fileOut.exists() ){
                throw new FileOutAlreadyExistsException();
            }

            if( fileOut.canWrite() ){
                throw new FileOutCanNotBeWriting();
            }

            this.fileOut = fileOut;
        }


        /**
         * Загружает файл конфигурации в properties
         * @throws InitConfigAppException файл конфигурации не был найден
         */
        private static void initConfigProperties() throws InitConfigAppException{
            File managerConfigFile = new File( Main.getManagerConfigFilePath() );
            properties = new Properties();
            //Читаем файл конфигурации приложения
            try( FileReader fileReader = new FileReader( managerConfigFile ) ){
                properties.load( fileReader );
            }catch( FileNotFoundException e ){
                throw new AppConfigFileNotFoundException();
            }catch( IOException e ){
                throw new AppConfigFileReadException( e );
            }
        }
    }


}
