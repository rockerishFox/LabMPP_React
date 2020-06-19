package utils;

public class AppEvents<E> implements IEvent{
    private Events events;
    private E data, oldData;

    public AppEvents(Events a, E e){
        events=a;
        data=e;
    }

    public AppEvents(Events a, E e, E o){
        events=a;
        data=e;
        oldData=o;
    }

    public Events getEvent(){
        return events;
    }

    public E getData(){
        return data;
    }

    public E getOldData(){
        return oldData;
    }
}
