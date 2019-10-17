import java.util.ArrayList;
import java.util.Scanner;

public class BridgeNum implements Runnable{
    public static void main(String[] args) {
        new Thread(null, new BridgeNum(), "whatever", 1<<26).start();
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Graph Gr = new Graph(n);
        int m = in.nextInt();
        for (int i = 0; i < m; i++){
            int vert1 = in.nextInt();
            int vert2 = in.nextInt();
            Gr.addEdge(vert1, vert2);
        }
        Gr.dfs();
        System.out.println(Gr.getCompnum());
    }
}

class Edge{
    public int num;
    public int def;

    public Edge(int num){
        this.num = num;
        this.def = 1;
    }
}

class Vertex{
    public ArrayList<Edge> smej;
    private int clr;

    public Vertex(){
        this.smej = new ArrayList<>();
        this.clr = 0;
    }

    public int getClr(){
        return this.clr;
    }

    public void setClr(int clr){
        this.clr = clr;
    }
}

class Graph {
    private ArrayList<Vertex> vertices;
    private int compnum;

    public int getCompnum(){
        return compnum;
    }

    public void changeCompnum(int a){
        this.compnum += a;
    }

    public Graph(int a) {
        this.vertices = new ArrayList<>();
        this.compnum = 0;
        for (int i = 0; i < a; i++){
            this.vertices.add(new Vertex());
        }
    }

    public void addEdge(int vert1, int vert2){
        int lst1 = this.vertices.get(vert1).smej.size() - 1;
        int lst2 = this.vertices.get(vert2).smej.size() - 1;
        if ((lst1 >= 0) && (this.vertices.get(vert1).smej.get(lst1).num == vert2)){
            if (vert1 != vert2) {
                this.vertices.get(vert1).smej.get(lst1).def++;
                this.vertices.get(vert2).smej.get(lst2).def++;
            }
            else this.vertices.get(vert1).smej.get(lst1).def++;
        }
        else {
            if (vert1 != vert2) {
                this.vertices.get(vert1).smej.add(new Edge(vert2));
                this.vertices.get(vert2).smej.add(new Edge(vert1));
            }
            else {
                this.vertices.get(vert1).smej.add(new Edge(vert2));
            }
        }
    }

    public void dfs() {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getClr() == 0) {
                this.changeCompnum(-1);
                ArrayList<Integer> qu = new ArrayList<>();
                this.visitVertex1(i, qu, i);
                for (int j = 0; j < qu.size(); j++){
                    int next = qu.get(j);
                    if (this.vertices.get(next).getClr() == 1){
                        this.visitVertex2(next);
                        this.changeCompnum(1);
                    }
                }
            }
        }
    }

    public void visitVertex1(int i, ArrayList<Integer> queue, int parent){
        this.vertices.get(i).setClr(1);
        queue.add(i);
        for (int j = 0; j < this.vertices.get(i).smej.size(); j++){
            int next = this.vertices.get(i).smej.get(j).num;
            if (this.vertices.get(next).getClr() == 0){
                this.vertices.get(i).smej.remove(j);
                j--;
                this.visitVertex1(next, queue, i);
            }
        }
    }

    public void visitVertex2(int i){
        this.vertices.get(i).setClr(2);
        for (int j = 0; j < this.vertices.get(i).smej.size(); j++){
            int next = this.vertices.get(i).smej.get(j).num;
            if (this.vertices.get(next).getClr() == 1){
                this.visitVertex2(next);
            }
        }
    }
}