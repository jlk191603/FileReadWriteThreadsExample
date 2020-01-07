package ru.systemsez.examples.frwt.exceptions;

public class FileOutAlreadyExistsException extends FileOutException{

    public FileOutAlreadyExistsException(){
        super("Файл уже существует");
    }

}
