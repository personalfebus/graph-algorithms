import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Kruskal {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Graph Gr = new Graph();

        for (int i = 0; i < n ; i++){
            int x = in.nextInt();
            int y = in.nextInt();
            Gr.addVertex(i, x, y);
        }

        System.out.println(String.format("%.2f", Gr.getMinTree()));
    }
}

class Vertex{
    private int parent;
    private int x;
    private int y;

    public Vertex(int i, int a, int b){
        this.parent = i;
        this.x = a;
        this.y = b;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}

class Edge implements Comparable<Edge>{
    private int vert1;
    private int vert2;
    private int length;

    public Edge(int a, int b, int len){
        this.vert1 = a;
        this.vert2 = b;
        this.length = len;
    }

    @Override
    public int compareTo(Edge b) {
        return this.length - b.getLength();
    }

    public int getLength() {
        return length;
    }

    public int getVert1() {
        return vert1;
    }

    public int getVert2() {
        return vert2;
    }
}

class Graph{
    private ArrayList<Vertex> vertices;
    private PriorityQueue<Edge> edges;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new PriorityQueue<>();
    }

    public void addVertex(int i, int a, int b){
        this.vertices.add(new Vertex(i, a, b));
        int n = this.vertices.size() - 1;
        for (int j = 0; j < n; j++){
            this.addEdge(j, n);
        }
    }

    public void addEdge(int a, int b){
        int xl = this.vertices.get(a).getX() - this.vertices.get(b).getX();
        int yl = this.vertices.get(a).getY() - this.vertices.get(b).getY();
        this.edges.add(new Edge(a, b, xl*xl + yl*yl));
    }

    public int getTrueParent(int i){
        if (this.vertices.get(i).getParent() == i) return i;
        else {
            int k = getTrueParent(this.vertices.get(i).getParent());
            this.vertices.get(i).setParent(k);
            return k;
        }
    }

    public double getMinTree(){
        int count = 0;
        double len = 0;
        while (count < this.vertices.size() - 1){
            Edge curr = this.edges.poll();
            int v1 = curr.getVert1();
            int v2 = curr.getVert2();
            if (this.getTrueParent(v1) != this.getTrueParent(v2)){
                len += Math.sqrt(curr.getLength());
                count++;
                this.vertices.get(this.getTrueParent(v2)).setParent(v1);
            }
        }
        return len;
    }
}