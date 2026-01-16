package Listas;

import java.io.Serializable;

/**
 *
 * @author ED_P1_Grupo05
 */
public class CircularNodeList<E> implements Serializable{
    private static final long serialVersionUID = 1L;
    private E content;
    private CircularNodeList<E> next;
    
    public CircularNodeList(E content){
        this.content=content;
        this.next=this; 
    }
    
    public E getContent(){
        return content;
    }
    
    public void setContent(E content){
        this.content = content;
    }
    
    public CircularNodeList<E> getNext(){
        return next;
    }
    
    public void setNext(CircularNodeList<E> next){
        this.next = next;
    }
}
