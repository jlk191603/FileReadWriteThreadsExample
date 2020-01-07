package ru.systemsez.examples.frwt;

/**
 * Данные передаем блоками, из блока можно получить данные
 * о номере его чтения из файла. По данному номеру и исполнитель
 * и писатель смогут понимать в какую очередь был прочитан кусок
 * из файла.
 */
public class AppBlock{

    private int number;
    private byte[] data;


    public AppBlock( int number, byte[] data ){
        this.number = number;
        this.data   = data;
    }


    public int getNumber(){

        return number;
    }


    public void setNumber( int number ){

        this.number = number;
    }


    public byte[] getData(){

        return data;
    }


    public void setData( byte[] data ){

        this.data = data;
    }

}
