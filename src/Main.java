/** Assignment 2 - Radix Sort - 10/19/2021
 *  Sebastian Jones - Sacramento State University - CSC 130 - Professor Cooke
 */

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Radix test = new Radix();

        test.sort("zipcodes.txt");

    }
}

/** Entry Class
 *      Used to store data taken from test file of key-value pairs
 */
class Entry {
    public String key;
    public String value;

    public Entry() {
        this.key = "";
        this.value = "";
    }
    public Entry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

/** Node Class
 *      Stores an indiscriminate variable and the next node in the chain/list/array.
 */
class Node {
    private Object value;
    private Node next;

    //Constructors
    public Node(){
    }
    public Node(Object value) {
        this.value = value;
    }
    public Node(Object value, Node next) {
        this.value = value;
        this.next = next;
    }

    public Object getValue() {
        return this.value;
    }
    public Node getNext() {
        return this.next;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}

/** Doubly linked list
 *      Keeps track of beginning and end of list for O(1) efficiency
 */
class LinkedList {
    private Node head;
    private Node tail;
    private Node pool = new Node();
    private Integer poolSize = 1;
    final Integer poolMax = 9;

    //Constructors
    public LinkedList(){
        Node poolIndex = pool;
        for(int i = 1; i < 10; i++) {                       //starts at 1 because there is already a node made...
            poolIndex.setNext(new Node());
            poolIndex = poolIndex.getNext();
            poolSize++;
        }
    }
    public LinkedList(Object item) {
        Node poolIndex = pool;
        for(int i = 1; i <= 10; i++) {                       //starts at 1 because there is already a node made...
            poolIndex.setNext(new Node());
            poolIndex = poolIndex.getNext();
            poolSize = i;
        }
        this.addHead(item);
    }

    String about() {
        return "Author: Sebastian Jones";
    }
    void addHead(Object item) {
        Node node = removePool();
        node.setValue(item);

        if (peekHead() == null) {               //List is empty, make new head
            head = node;
            head.setNext(null);
        } else if (tail == null){               //Only one item in list
            tail = head;
            head = node;
            head.setNext(tail);
        } else {
            Node oldHead = head;                //Not needed but helps readability
            head = node;
            head.setNext(oldHead);
        }
    }
    void addTail(Object item) {
        Node node = removePool();
        node.setValue(item);
        node.setNext(null);

        if (peekHead() == null) {               //List is empty
            head = node;
        } else if (head.getNext() == null) {    //No tail (one item in list)
            tail = node;
            head.setNext(tail);
        } else {
            tail.setNext(node);
            tail = node;
        }
    }
    Object removeHead() {
        if(head == null) {
            return null;
        }
        Node oldHead = head;
        Object returnValue = oldHead.getValue();

        head = head.getNext();
        addPool(oldHead);

        return returnValue;
    }

    Object peekHead() {
        if(head == null) {
            return null;
        }
        return head.getValue();
    }

    void addPool(Node add) {
        if(poolSize <= poolMax) {
            add.setValue(null);
            add.setNext(pool);
            pool = add;
            poolSize++;
            return;
        }
        add.setNext(null);
        add.setValue(null);
    }

    Node removePool() {
        if(poolSize == 0) {
            return new Node();
        }
        else {
            Node returnNode = pool;
            pool = pool.getNext();
            poolSize--;
            return returnNode;
        }

    }

    String outputToString() {
        Node index = head;
        String output = "";
        StringBuilder sb = new StringBuilder();
        if(index == null) {
            return "List is empty\r\n";
        }
        System.out.print("Head -> ");
        while(index.getNext() != null) {
            output = sb.append(index.getValue()).append(", ").toString();
            index = index.getNext();
        }
        output += (index.getValue() + " <- Tail\r\n");

        return output;
    }
}
/** Queue
 *      Builds on tail, removes off head
 */
class Queue {
    LinkedList queue;

    //Constructors
    public Queue() {
        this.queue = new LinkedList();
    }
    public Queue(String item) {
        this.queue = new LinkedList(item);
    }

    String about() {
        return "Author: Sebastian Jones";
    }

    void enqueue(Entry entry) {
        queue.addTail(entry);
    }

    Entry dequeue() {
        return (Entry)(queue.removeHead());
    }

    Entry peek() {
        return (Entry)(queue.peekHead());
    }

    void outputToString() {
        String output = queue.outputToString();
        System.out.print(output);
    }

    boolean isEmpty() {
        return queue.peekHead() == null;
    }
}

/** Radix
 *      Takes in data from input file, performs Radix sort and outputs sorted data as String
 *      TODO: Clean up code. Make test ouput prettier and move to main.
 */
class Radix {
    Integer numKeyDigits = 0;
    Integer numItems = 0;
    Entry[] entries;
    Queue[] buckets = new Queue[10];

    Entry[] sort(String filename) throws FileNotFoundException {
        Entry[] sortedData = new Entry[numItems];
        Entry[] unsortedData = getData(filename);

        for(int i = 1; i <= numKeyDigits; i ++) {
            System.out.println("Pass " + i + ":");
            for(Entry entry : unsortedData) {
                Integer keyNum = Character.getNumericValue(entry.key.charAt(entry.key.length() - i));
                if(buckets[keyNum] == null) {
                    buckets[keyNum] = new Queue();
                }
                buckets[keyNum].enqueue(entry);
            }

            unsortedData = new Entry[numItems];                         //Get array ready to be populated again

            for(int j = 0; j < numItems; j++) {
                for(int k = 0; k < 10; k ++) {
                    while(buckets[k] == null) {                         //Throws error if it tries to access a null
                        k++;                                            //array, this will skip that bug if needed
                        if (k > 9) {
                            break;
                        }
                    }
                    while(!buckets[k].isEmpty()) {
                        unsortedData[j] = buckets[k].dequeue();
                        j++;
                    }
                }
            }
            for(Entry entry: unsortedData) {
                System.out.println(entry.key);
            }
        }




        return sortedData;
    }

    Entry[] getData(String fileName) throws FileNotFoundException{
        String sortedString = "";

        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        sc.useDelimiter(",");

        numKeyDigits = Integer.parseInt(sc.nextLine());
        System.out.println("Key Digit Length: " + numKeyDigits);

        numItems = Integer.parseInt(sc.nextLine());
        System.out.println("Number of items: " + numItems);
        Entry[] unsortedData = new Entry[numItems];

        for(int i = 0; i < numItems; i++) {
            String key = sc.next();
            String value = sc.nextLine().substring(1);

            unsortedData[i] = new Entry(key, value);
            System.out.print(unsortedData[i].key + " - " + unsortedData[i].value + "\n");
        }

        return unsortedData;
    }
}