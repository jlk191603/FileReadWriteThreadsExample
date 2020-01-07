package ru.systemsez.examples.frwt.exceptions;

public class FileOutCanNotBeWriting extends FileOutException{

    public FileOutCanNotBeWriting(){
        super( "Файл не может быть записан - " +
               "не хватает прав доступа или файл занят другим приложением" );
    }

}
