import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Prim{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        MyPriorityQueue queue = new MyPriorityQueue(n);
        Vertex[] T = new Vertex[n];

        for (int i = 0; i < n; i++){
            T[i] = new Vertex();
        }

        for (int i = 0; i < m; i++) {
            int vert1 = in.nextInt();
            int vert2 = in.nextInt();
            int len = in.nextInt();
            T[vert1].addEdge(new Edge(vert2, len));
            T[vert2].addEdge(new Edge(vert1, len));
        }
        System.out.println(queue.primWiki(T));
    }
}

class Edge{
    private int vert;
    private int dist;

    public Edge(int a, int b){
        vert = a;
        dist = b;
    }

    public int getDist() {
        return dist;
    }

    public int getVert() {
        return vert;
    }
}

class Vertex implements Comparable<Vertex>{
    private int index;
    private int key;
    private int value;
    public int truind;
    public boolean vis;
    public ArrayList<Edge> smej;

    public Vertex(){
        index =  -1;
        key = Integer.MAX_VALUE;
        value = 0;
        vis = false;
        this.smej = new ArrayList<>();
    }

    public void addEdge(Edge A){
        this.smej.add(A);
    }

    @Override
    public int compareTo(Vertex o){
        return this.key - o.key;
    }

    public int getKey() {
        return key;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class MyPriorityQueue{
    private Vertex[] heap;
    private int cap;
    private int count;

    public MyPriorityQueue(int m){
        count = 0;
        cap = m;
        heap = new Vertex[m];
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < count; i++){
            s.append(this.heap[i].getKey());
            s.append(" ");
        }
        s.append("\n");
        return s.toString();
    }

    public void swap(int i, int j){
        Vertex Tmp = this.heap[i];
        this.heap[i] = this.heap[j];
        this.heap[j] = Tmp;
        this.heap[i].setIndex(i);
        this.heap[j].setIndex(j);
    }

    public void insert(Vertex A){
        int i = this.count;
        this.count++;
        this.heap[i] = A;
        while ((i > 0) && (this.heap[(i - 1) / 2].getKey() > this.heap[i].getKey())){
            this.swap((i - 1) / 2, i);
            this.heap[i].setIndex(i);
            i = (i - 1) / 2;
        }
        this.heap[i].setIndex(i);
        System.out.print(this.toString());
    }

    public Vertex extractMin(){
        Vertex A = this.heap[0];
        this.count--;
        if (this.count > 0){
            this.heap[0] = this.heap[this.count];
            this.heap[0].setIndex(0);
            this.heapify(0, this.count);
        }
        System.out.println("Min " + A.getKey());
        return A;
    }

    public void heapify(int i, int n){
        for(;;){
            int l = 2 * i + 1;
            int r = l + 1;
            int j = i;
            if ((l < n) && (this.heap[i].getKey() > this.heap[l].getKey())) i = l;
            if ((r < n) && (this.heap[i].getKey() > this.heap[r].getKey())) i = r;
            if (i == j) break;
            swap(i ,j);
        }
        System.out.print(this.toString());
    }

    public void decreaseKey(int ind, int newkey){
        int i = this.heap[ind].getIndex();
        this.heap[ind].setKey(newkey);
        while ((i > 0) && (this.heap[(i - 1) / 2].getKey() > newkey)){
            this.swap((i - 1) / 2, i);
            this.heap[i].setIndex(i);
            i = (i - 1) / 2;
        }
        this.heap[ind].setIndex(i);
    }

    public int prmSkorb(Vertex[] T){
        int min = 0;
        int i = 0;
        int cnt = 0;

        for(;;){
            T[i].setIndex(-2);
            for (int j = 0; j < T[i].smej.size(); j++){
                int u = T[i].smej.get(j).getVert();
                int a = T[i].smej.get(j).getDist();
                System.out.println("vert " + i + " and smej " + u + " with dist = " + a);
                if (T[u].getIndex() == -1){
                    T[u].setKey(a);
                    T[u].setValue(i);
                    this.insert(T[u]);
                    T[u].truind = u;
                }
                else if ((T[u].getIndex() != -2) && (a < T[u].getKey())){
                    System.out.println("Deq");
                    T[u].setValue(i);
                    this.decreaseKey(T[u].getIndex(), a);
                }
            }
            if (this.count == 0) break;
            Vertex A = this.extractMin();
            min += A.getKey();
            i = A.truind;
            cnt++;
        }
        System.out.println(cnt);
        return min;
    }

    public int primWiki(Vertex[] T){
        int minum = 0;
        T[0].setKey(0);
        PriorityQueue<Vertex> op = new PriorityQueue<>();
        for (int i = 0; i < T.length; i++){
            op.add(T[i]);
            T[i].truind = i;
            T[i].vis = false;
        }

        Vertex A = op.poll();
        int i = A.truind;

        while (op.size() > 0){
            T[i].vis = true;
            for (int j = 0; j < T[i].smej.size(); j++){
                int u = T[i].smej.get(j).getVert();
                int a = T[i].smej.get(j).getDist();
                if ((!T[u].vis) && (a < T[u].getKey())){
                    T[u].setValue(i);
                    T[u].setKey(a);
                    op.remove(T[u]);
                    op.add(T[u]);
                }
            }
            Vertex B = op.poll();
            i = B.truind;
            minum += B.getKey();
        }
        return minum;
    }
}