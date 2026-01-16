package Listas;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author ED_P1_Grupo05
 */

public class CircularLinkedList<E> implements List<E>, Serializable {
    private static final long serialVersionUID = 1L;
    private CircularNodeList<E> last;
    
    public CircularNodeList<E> getLast(){
        return last;
    }
    
     public CircularNodeList<E> getHeader(){
        return last.getNext();
    }
    
    public void setLast(CircularNodeList<E> last){
        this.last = last;
    }
    
    @Override
    public boolean addFirst(E e) {
        if(e == null){
            return false;
        }
        CircularNodeList<E> newNode = new CircularNodeList<>(e);
        if(isEmpty()){
            last = newNode;
            last.setNext(last);
        }else{
            newNode.setNext(last.getNext());
            last.setNext(newNode);
        }
        return true;
    }

    @Override
    public boolean addLast(E e) {
        if(e == null){
            return false;
        }
        CircularNodeList<E> newNode = new CircularNodeList<>(e);
        if(isEmpty()){
            last = newNode;
            last.setNext(last);
        }else{
            newNode.setNext(getHeader());
            last.setNext(newNode);
            last = newNode;
        }
        return true;
    }

    @Override
    public E removeFirst() {
        if(isEmpty()){
            return null;
        }
        CircularNodeList<E> header = last.getNext();
        E removed = header.getContent();
        if(header == last){
            last = null;
        }else{
            last.setNext(header.getNext());
        }
        return removed;
    }

    @Override
    public E removeLast() {
        if(isEmpty()){
            return null;
        }
        E removed = last.getContent();
        if(last.getNext() == last){
            last = null;
        }else{
            CircularNodeList<E> current = last.getNext();
            while(current.getNext() != last){
                current = current.getNext();
            }
            current.setNext(last.getNext());
            last = current;
        }
        return removed;
    }

    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        int size = 0;
        CircularNodeList<E> current = last.getNext();  

        do {
            size++;
            current = current.getNext();
        } while (current != last.getNext()); 

        return size;
    }

    @Override
    public boolean isEmpty() {
        return last == null;
    }

    @Override
    public void clear() {
        last = null;
    }

    @Override
    public boolean add(int index, E element) {
         if (index < 0 || index > size() || element == null) {
            return false;
        }
        
        if (index == 0) {
            return addFirst(element);
        }
        if (index == size()) {
            return addLast(element);
        }
        
        CircularNodeList<E> newNode = new CircularNodeList<>(element);
        CircularNodeList<E> current = getNodeAt(index - 1);
        
        newNode.setNext(current.getNext());
        current.setNext(newNode);
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        
        if (index == 0) {
            return removeFirst();
        }
        if (index == size() - 1) {
            return removeLast();
        }
        
        CircularNodeList<E> previous = getNodeAt(index - 1);
        CircularNodeList<E> toRemove = previous.getNext();
        E removedElement = toRemove.getContent();
        
        previous.setNext(toRemove.getNext());
        return removedElement;
    }
    
    private CircularNodeList<E> getNodeAt(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        
        CircularNodeList<E> current = last.getNext();
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }
    
    private CircularNodeList<E> getPrevious(CircularNodeList<E> node){
        if (last == null || node == null){
            return null;
        }
    
        CircularNodeList<E> current = last.getNext();
        CircularNodeList<E> previous = last;

        do {
            if (current == node) {
                return previous;
            }
            previous = current;
            current = current.getNext();
        } while (current != last.getNext());

        return null; // No encontrado
    }
    @Override
    public E get(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        return getNodeAt(index).getContent();
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size() || element == null) {
            return null;
        }
        
        CircularNodeList<E> node = getNodeAt(index);
        E oldElement = node.getContent();
        node.setContent(element);
        return oldElement;
    }
    
    @Override
    public String toString(){
        String s = "[";
        CircularNodeList<E> n = last.getNext();
        do{
            if(n.getNext()== last.getNext()){
                s+= n.getContent();
                n=n.getNext();
            }else{
                s += n.getContent() + ",";
                n = n.getNext();
            } 
            
        }while(n!=last.getNext());
        
        s+="]";
        return s;
    }
   
    @Override
    public Iterator<E> iterator(){
        Iterator<E> it=new Iterator<E>() {
                CircularNodeList<E> cursor = (last != null) ? last.getNext() : null;
                boolean firstIteration = true;
                
            @Override
            public boolean hasNext() {
                if(last == null){
                    return false;
                }
                return firstIteration || cursor != last.getNext();
            }

            @Override
            public E next() {
                
                E e = cursor.getContent();
                cursor = cursor.getNext();
                firstIteration = false;
                return e;
            }
        };
        return it;
    } 
    
    public Iterator<E> IteratorHaciaAtras(){
        return new Iterator<E>() {
            private CircularNodeList<E> current = last;
            private boolean started = false;

            @Override
            public boolean hasNext() {
                if (isEmpty()) return false;
                return !started || current != last;
            }

            @Override
            public E next() {
                if (!started) {
                    started = true;
                } else {
                    current = getPrevious(current);
                }
                return current.getContent();
            }
        };
    }
    
    @Override
     public void sort(Comparator<E> cmp){
        if (isEmpty() || size() == 1) return;

        boolean swapped;
        
        do {
            swapped = false;
            CircularNodeList<E> current = last.getNext(); 
            CircularNodeList<E> nextNode = current.getNext();

            for (int i = 0; i < size() - 1; i++) {
                if (cmp.compare(current.getContent(), nextNode.getContent()) > 0) {
                    // Intercambiar los datos
                    E temp = current.getContent();
                    current.setContent(nextNode.getContent());
                    nextNode.setContent(temp);
                    swapped = true;
                }
                current = nextNode;
                nextNode = nextNode.getNext();
            }
        } while (swapped);
    }
     
    public E buscar(E element, Comparator<E> cmp) {
        if (last == null) return null;
        
        CircularNodeList<E> actual = last.getNext();
        do {
            if(cmp.compare(actual.getContent(), element) == 0){
                return actual.getContent();
            }
            actual = actual.getNext();
        } while (actual != last.getNext());
        
        return null;
    }
    
    public int indexOf(E elemento) {
        if (last == null){
            return -1;
        } 

        CircularNodeList<E> actual = last.getNext(); 
        int index = 0;

        do {
            if ((actual.getContent() == null && elemento == null) || 
                (actual.getContent() != null && actual.getContent().equals(elemento))) {
                return index;
            }
            actual = actual.getNext();
            index++;
        } while (actual != last.getNext());

        return -1; 
    }
}
