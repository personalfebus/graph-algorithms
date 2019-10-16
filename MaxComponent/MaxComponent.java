import java.util.ArrayList;
import java.util.Scanner;

public class MaxComponent implements Runnable {
    public static void main(String[] args) {
        new Thread(null, new MaxComponent(), "whatever", 1<<26).start();
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
        System.out.println(Gr.toString());
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
    public StringBuilder endgame;
    public StringBuilder edgeskeeper;
    private int mainparent;
    private int maxcomp;
    private int maxedges;
    private int currcomp;
    private int curredges;

    public Graph(int a) {
        this.currcomp = 0;
        this.mainparent = 0;
        this.maxcomp = 0;
        this.maxedges = 0;
        this.curredges = 0;
        this.vertices = new ArrayList<>();
        this.endgame = new StringBuilder();
        this.edgeskeeper = new StringBuilder();
        this.endgame.append("graph {\n");
        for (int i = 0; i < a; i++) {
            this.endgame.append("\t");
            this.endgame.append(i);
            this.endgame.append("\n");
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

    public String toString(){
        return this.endgame.append(this.edgeskeeper.toString()).append("}").toString();
    }

    public void dfs(){
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getClr() == 0) {
                this.visitVertex(i);
                if (this.currcomp >= this.maxcomp){
                    if (this.currcomp > this.maxcomp) {
                        this.mainparent = i;
                        this.maxcomp = this.currcomp;
                        this.maxedges = this.curredges;
                    }
                    else if (this.curredges > this.maxedges){
                        this.mainparent = i;
                        this.maxcomp = this.currcomp;
                        this.maxedges = this.curredges;
                    }
                }
                this.currcomp = 0;
                this.curredges = 0;
            }
        }

        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getClr() == 1) {
                this.coolKidsVisitVertex(i, i, i);
            }
        }
    }

    public void visitVertex(int i){
        this.currcomp++;
        this.vertices.get(i).setClr(1);
        for (int j = 0; j < this.vertices.get(i).smej.size(); j++){
            int next = this.vertices.get(i).smej.get(j).num;
            this.curredges += this.vertices.get(i).smej.get(j).def;
            if (this.vertices.get(next).getClr() == 0){
                this.visitVertex(next);
            }
        }
    }

    public void coolKidsVisitVertex(int par, int start, int prev){
        this.vertices.get(par).setClr(2);
        if (start == this.mainparent) {
            StringBuilder fv = new StringBuilder();
            fv.append(par);
            int pos = this.endgame.indexOf(fv.toString());
            this.endgame.insert(pos + fv.length(), " [color = red]");
        }
        for (int j = 0; j < this.vertices.get(par).smej.size(); j++) {
            int next = this.vertices.get(par).smej.get(j).num;
            if ((this.vertices.get(next).getClr() < 3) && ((next != prev) || (next == par))){
                StringBuilder s = new StringBuilder();
                s.append("\t");
                s.append(par);
                s.append(" -- ");
                s.append(next);
                if (start == this.mainparent) s.append(" [color = red]");
                s.append("\n");
                int tmp = this.vertices.get(par).smej.get(j).def;
                if (tmp > 1){
                    String def = s.toString();
                    while (tmp > 1){
                        s.append(def);
                        tmp--;
                    }
                }
                this.edgeskeeper.append(s.toString());
                if ((this.vertices.get(next).getClr() == 1) && (par != next)){
                    this.coolKidsVisitVertex(next, start, par);
                }
            }
        }
        this.vertices.get(par).setClr(3);
    }
}