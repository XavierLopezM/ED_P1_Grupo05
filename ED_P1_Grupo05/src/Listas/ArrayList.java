/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Listas;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author ED_P1_Grupo05
 */
public class ArrayList<E> implements List<E>, Serializable {
    private static final long serialVersionUID = 1L;
    private E[] elements = null; 
    private int capacity = 100;
    private int effectiveSize;
    
    public ArrayList (){
        
        elements = (E[])(new Object[capacity]); 
        effectiveSize = 0;
    }
    
    private boolean isFull(){
        return effectiveSize == capacity;
    }
   
    @Override
    public boolean addFirst(E e) {
        
        // no se insertan nulos
        if(e==null){
            return false;
        } else if (isEmpty()){
            elements[0] = e;
            effectiveSize++;
            
            return true;
        } else if (isFull()){
            addCapacity();
        }
        
        for (int i=effectiveSize-1; i >=0; i--){
            elements[i+1]=elements[i]; 
            
        }
        elements[0] = e;
        effectiveSize++;
        return true;
    }

    @Override
    public boolean addLast(E e) {
       if (e == null) {
            return false;
        }
        if (isFull()) {
            addCapacity();
        }
        elements[effectiveSize] = e;
        effectiveSize++;
        
        return true;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        E removed = elements[0];
        for (int i = 0; i < effectiveSize - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[effectiveSize - 1] = null;
        effectiveSize--;
        return removed;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            return null;
        }
        E removed = elements[effectiveSize - 1];
        elements[effectiveSize - 1] = null;
        effectiveSize--;
        return removed;
    }

    @Override
    public int size() {
        return effectiveSize;
    }

    @Override
    public boolean isEmpty() {
        return effectiveSize == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < effectiveSize; i++) {
            elements[i] = null;
        }
        effectiveSize = 0;
    }

    @Override
    public boolean add(int index, E element) {
        if (index < 0 || index > effectiveSize) {
            
            return false;
        }
        if (isFull()) {
            addCapacity();
        }
        for (int i = effectiveSize; i > index; i--) {
            elements[i] = elements[i - 1];
            
        }
        elements[index] = element;
        effectiveSize++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= effectiveSize) {
            return null;
        }
        E removed = elements[index];
        for (int i = index; i < effectiveSize - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[effectiveSize - 1] = null;
        effectiveSize--;
        return removed;
        }

    @Override
    public E get(int index) {
        if (index < 0 || index >= effectiveSize) {
            return null;
        }
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= effectiveSize || element == null) {
            return null;
        }
        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    private void addCapacity() {
        E[] tmp = (E[]) new Object[capacity * 2];
        for (int i = 0; i < capacity; i++){
            tmp[i] = elements[i];
        }
        elements = tmp;
        capacity = capacity * 2;
    }
    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i<effectiveSize;i++) {
            
            if(i!=effectiveSize-1){
                s += this.get(i) + ",";
            }else{
                s+= this.get(i);
            }
            
        }
        s += "]";
        return s;
    }
    @Override
    public Iterator<E> iterator(){
            Iterator<E> it=new Iterator<E>() {
                int cursor = 0;
                @Override
                public boolean hasNext() {
                    return cursor < effectiveSize;
                }

                @Override
                public E next() {
                    E e=elements[cursor];
                    cursor++;
                    return e;
                }
            };
        return it;   
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<E> cmp) {
    
     if (size() <= 1) {
         return;
        }

     boolean swapped;

     do {
         swapped = false;

            for (int i = 0; i < size() - 1; i++) {
                E a = elements[i];
                E b = elements[i + 1];

               int resultado;

                if (cmp != null) {
                   resultado = cmp.compare(a, b);
                } else {
                   Comparable<E> ca = (Comparable<E>) a;
                   resultado = ca.compareTo(b);
                }

                if (resultado > 0) {
                   elements[i] = b;
                  elements[i + 1] = a;
                  swapped = true;
                }
            }

        } while (swapped);
    }

    
    public void reverse() {
        for (int i = 0; i < effectiveSize / 2; i++) {
            E temp = elements[i];
            elements[i] = elements[effectiveSize - 1 - i];
            elements[effectiveSize - 1 - i] = temp;
        }
    }
    
}
