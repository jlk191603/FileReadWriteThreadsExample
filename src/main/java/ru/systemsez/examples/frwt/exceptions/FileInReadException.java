package ru.systemsez.examples.frwt.exceptions;

public class FileInReadException extends Exception{

    public FileInReadException(String info){
        super( "Ошибка чтения входящего файла: " );
    }

}
