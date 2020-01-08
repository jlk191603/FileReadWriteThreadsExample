package ru.systemsez.examples.frwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.systemsez.examples.frwt.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public class App{

    private static final Logger logger = LogManager.getLogger( App.class);

    private static App instance;
    private AppManager appManager;


    private App(Config config) throws FileInReadException, FileOutException{
        logger.trace( "constructor(Config) IN" );

        appManager = new AppManager( config.getFileIn(), config.getFileOut(),
                                     config.getCountExecutors(), config.getAppBlockSize() );

        logger.info( "constructor(Config) менеджер потоков сконфигурирован успешно" );
        start();
        logger.info( "constructor(Config) менеджер потоков запущен" );

        logger.trace( "constructor(Config) OUT" );
    }

    private void start(){
        logger.trace( "start() IN" );

        appManager.start();

        logger.trace( "start() OUT" );
    }

    public synchronized static App getInstance(){
        logger.trace( "getInstance() IN" );
        if( instance == null ){
            logger.trace( "getInstance() создать новый объект приложения" );
            try{
                instance = new App( Config.getInstance() );
            }catch( Exception e ){
                logger.error( "getInstance() ошибка при инициализации объекта приложения: " + e.getMessage() );
                System.exit( -1 );
            }
            logger.trace( "getInstance() новый объект приложения создан" );
        }
        logger.trace( "getInstance() OUT" );
        return instance;
    }

    public static class Config{

        private static final int COUNT_EXECUTORS_DEFAULT = 1;
        private static final int COUNT_EXECUTORS_MIN = 1;
        private static final int COUNT_EXECUTORS_MAX = 10;

        private static final int BLOCK_SIZE_DEFAULT = 50;
        private static final int BLOCK_SIZE_MIN = 20;
        private static final int BLOCK_SIZE_MAX = 200;


        private static Config instance;
        private static Properties properties;

        private Integer countExecutors;
        private Integer appBlockSize;
        private File fileIn;
        private File fileOut;


        private Config() throws InitConfigAppException{
            logger.trace( "Config constructor() IN" );

            initConfigProperties();

            logger.trace( "Config constructor() OUT" );
        }

        public static Config getInstance() throws InitConfigAppException{
            logger.trace( "Config.getInstance() IN" );
            if(instance == null){
                logger.trace( "Config.getInstance() создать новый объект конфигурации" );
                instance = new Config();
            }
            logger.trace( "Config.getInstance() OUT" );
            return instance;
        }


        /**
         * Возвращает количество исполнителей.
         * При первом обращении к методу, параметр берется из properties.
         */
        public Integer getCountExecutors(){
            logger.trace( "Config.getCountExecutors() IN" );
            if( countExecutors == null ){
                setCountExecutors();
            }
            logger.info( "countExecutors=" + countExecutors );
            logger.trace( "Config.getCountExecutors() OUT" );
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
            logger.trace( "Config.setCountExecutors() IN" );
            String strVal = properties.getProperty( "countExecutors",
                                                    String.valueOf( COUNT_EXECUTORS_DEFAULT ) );
            logger.debug( "Config.setCountExecutors() значение свойства 'countExecutors' = " + strVal );
            int count;
            try{
                logger.debug( MessageFormat.format(
                    "Config.setCountExecutors() преобразование значения {0} в int", strVal ) );

                count = Integer.parseInt( strVal );

                logger.debug( "Config.setCountExecutors() преобразование значения успешно" );
            }catch( Exception ex ){
                count = COUNT_EXECUTORS_DEFAULT;

                logger.warn( MessageFormat.format(
                    "Config.setCountExecutors() ошибка преобразования значения {0} в int " +
                    "присвоено значение по умолчанию {1}", strVal, COUNT_EXECUTORS_DEFAULT ));
            }

            if( count < COUNT_EXECUTORS_MIN || count > COUNT_EXECUTORS_MAX ){
                this.countExecutors = COUNT_EXECUTORS_DEFAULT;
                logger.warn( MessageFormat.format(
                    "Config.setCountExecutors() полученное значение {0} выходит за рамки " +
                    "предельно допустимых значений (min = {1}, max = {2} ) - " +
                    "присвоено значение по умолчанию {3}",
                    count, COUNT_EXECUTORS_MIN, COUNT_EXECUTORS_MAX, COUNT_EXECUTORS_DEFAULT ));
            }else{
                this.countExecutors = count;
                logger.debug( "Config.setCountExecutors() присвоено значение " + count );
            }
            logger.trace( "Config.setCountExecutors() OUT" );
        }


        public Integer getAppBlockSize(){
            logger.trace( "Config.getAppBlockSize() IN" );
            if( appBlockSize == null){
                setAppBlockSize();
            }
            logger.debug( "Config.getAppBlockSize() appBlockSize = " + appBlockSize );
            logger.trace( "Config.getAppBlockSize() OUT" );
            return appBlockSize;
        }


        private void setAppBlockSize(){
            logger.trace( "Config.setAppBlockSize() IN" );
            String strVal = properties.getProperty( "blockSize",
                                                    String.valueOf( BLOCK_SIZE_DEFAULT ));
            logger.debug( "Config.setAppBlockSize() значение свойства 'blockSize' = " + strVal );
            int blockSize;
            try{
                logger.debug( MessageFormat.format(
                    "Config.setAppBlockSize() преобразование значения {0} в int", strVal ) );

                blockSize = Integer.parseInt( strVal );

                logger.debug( "Config.setAppBlockSize() преобразование значения успешно" );
            }catch( Exception ex ){
                blockSize = BLOCK_SIZE_DEFAULT;

                logger.warn( MessageFormat.format(
                    "Config.setAppBlockSize() ошибка преобразования значения {0} в int " +
                    "присвоено значение по умолчанию {1}", strVal, COUNT_EXECUTORS_DEFAULT ));
            }

            if( blockSize < BLOCK_SIZE_MIN || blockSize > BLOCK_SIZE_MAX ){
                this.appBlockSize = BLOCK_SIZE_DEFAULT;
                logger.warn( MessageFormat.format(
                    "Config.setAppBlockSize() полученное значение {0} выходит за рамки " +
                    "предельно допустимых значений (min = {1}, max = {2} ) - " +
                    "присвоено значение по умолчанию {3}",
                    blockSize, BLOCK_SIZE_MIN, BLOCK_SIZE_MAX, BLOCK_SIZE_DEFAULT ));
            }else{
                this.appBlockSize = blockSize;
                logger.debug( "Config.setAppBlockSize() присвоено значение " + blockSize );
            }

            logger.trace( "Config.setAppBlockSize() OUT" );
        }


        /**
         * Возвращает путь к файлу с входящими данными,
         * @return путь к файлу с входящими данными
         * @throws FileInReadException путь к файлу с входящими данными не задан
         * или файл не может быть прочитан
         */
        public File getFileIn() throws FileInReadException{
            logger.trace( "Config.getFileIn() IN" );
            if(fileIn == null){
                setFileIn();
            }
            logger.debug( "Config.getFileIn() fileIn = " + fileIn  );
            logger.trace( "Config.getFileIn() OUT" );
            return fileIn;
        }


        private void setFileIn() throws FileInReadException{
            logger.trace( "Config.setFileIn() IN" );
            String strVal = properties.getProperty( "filePathIn","" );
            logger.debug( "Config.setFileIn() получено значение " + strVal );

            File fileIn = new File(strVal);
            if( !fileIn.exists() ){
                throw new FileInPathNotFoundException();
            }
            if( !fileIn.canRead() ){
                throw new FileInCanNotBeReadException();
            }
            this.fileIn = fileIn;
            logger.trace( "Config.setFileIn() OUT" );
        }

        /**
         * Возвращает путь для файла результата
         * @return путь для файла результата
         * @throws FileOutException путь для файла результата не задан
         */
        public File getFileOut() throws FileOutException{
            logger.trace( "Config.getFileOut() IN" );
            if(fileOut == null){
                setFileOut();
            }
            logger.debug( "Config.getFileOut() fileOut = " + fileOut );
            logger.trace( "Config.getFileOut() OUT" );
            return fileOut;
        }


        private void setFileOut() throws FileOutException{
            logger.trace( "Config.setFileOut() IN" );
            String strVal = properties.getProperty( "filePathOut","" );
            logger.debug( "Config.setFileOut() получено значение " + strVal );

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
            logger.trace( "Config.setFileOut() OUT" );
        }


        /**
         * Загружает файл конфигурации в properties
         * @throws InitConfigAppException файл конфигурации не был найден
         */
        private static void initConfigProperties() throws InitConfigAppException{
            logger.trace( "Config.initConfigProperties() IN" );

            File managerConfigFile = new File( Main.getConfigFilePath() );
            properties = new Properties();
            //Читаем файл конфигурации приложения
            try( FileReader fileReader = new FileReader( managerConfigFile ) ){
                logger.debug( "Config.initConfigProperties() загрузка конфигурационного файла" );
                properties.load( fileReader );
                logger.debug( "Config.initConfigProperties() свойства из конфионного файла загружены успешно" );
            }catch( FileNotFoundException e ){
                throw new AppConfigFileNotFoundException();
            }catch( IOException e ){
                throw new AppConfigFileReadException( e );
            }
            logger.trace( "Config.initConfigProperties() OUT" );
        }
    }


}
