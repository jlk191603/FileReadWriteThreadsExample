package ru.systemsez.examples.frwt.exceptions;

public class FileOutPathNotFoundException extends FileOutException{

    public FileOutPathNotFoundException(){
        super("Путь для файла результата не задан");
    }

}
