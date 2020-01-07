package ru.systemsez.examples.frwt.exceptions;

public class FileInCanNotBeReadException extends FileInReadException{

    public FileInCanNotBeReadException(){
        super( "Файл не может быть прочитан - " +
               "не хватает прав доступа или файл занят другим приложением" );
    }

}
