package ru.systemsez.examples.frwt.exceptions;

public class FileOutAlreadyExistsException extends FileOutException{

    public FileOutAlreadyExistsException(){
        super("Файл разультатов по заданному пути уже существует");
    }

}
