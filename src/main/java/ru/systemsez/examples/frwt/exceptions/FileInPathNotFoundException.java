package ru.systemsez.examples.frwt.exceptions;

public class FileInPathNotFoundException extends FileInReadException{

    public FileInPathNotFoundException(){
        super("Путь к входящему файлу не задан");
    }

}
