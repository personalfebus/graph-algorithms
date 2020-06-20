import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class GraphBase {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Graph Gr = new Graph(n);
        for (int i = 0; i < m; i++){
            Gr.addEdge(in.nextInt(), in.nextInt());
        }
        Gr.Tarjan();
        System.out.println(Gr.condece());
    }
}

class Vertex{
    public ArrayList<Integer> smej;
    private int comp;
    private int t1;
    private int t2;
    private int low;
    public int previz;
    public int clr;

    public Vertex(){
        smej = new ArrayList<>();
        comp = 0;
        clr = 0;
        t1 = 0;
        previz = 0;
    }

    public int getT1() {
        return t1;
    }

    public int getComp() {
        return comp;
    }

    public int getLow() {
        return low;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public void setComp(int comp) {
        this.comp = comp;
    }

    public void setLow(int low) {
        this.low = low;
    }
}


class Graph{
    private ArrayList<Vertex> vertices;
    public int time;
    public int count;
    Stack<Integer> stack;
    private ArrayList<Integer> base;
    private ArrayList<Integer> bverts;

    public Graph(int n){
        vertices = new ArrayList<>();
        base = new ArrayList<>();
        bverts = new ArrayList<>();
        stack = new Stack<>();
        time = 1;
        count = 1;
        for (int i = 0; i < n; i++){
            vertices.add(new Vertex());
        }
    }

    public void addEdge(int vert1, int vert2){
        vertices.get(vert1).smej.add(vert2);
    }

    public void Tarjan(){
        for (int i = 0; i < vertices.size(); i++){
            if (vertices.get(i).getT1() == 0) {
                VisitVertex(i);
            }
        }
    }

    public void VisitVertex(int v){
        vertices.get(v).setT1(time);
        vertices.get(v).setLow(time);
        time++;
        stack.push(v);
        for (int i = 0; i < vertices.get(v).smej.size(); i++){
            int u = vertices.get(v).smej.get(i);
            if (vertices.get(u).getT1() == 0){
                VisitVertex(u);
            }
            if ((vertices.get(u).getComp() == 0) && (vertices.get(v).getLow() > vertices.get(u).getLow())){
                vertices.get(v).setLow(vertices.get(u).getLow());
            }
        }
        if (vertices.get(v).getT1() == vertices.get(v).getLow()){
            for (;;) {
                int u = stack.pop();
                vertices.get(u).setComp(count);
                if (u == v) break;
            }
            count++;
        }
    }

    public StringBuilder condece(){
        for (int i = 0; i < count - 1; i++){
            base.add(i + 1);
        }

        for (int i = 0; i < vertices.size(); i++){
            for (int j = 0; j < vertices.get(i).smej.size(); j++){
                int u = vertices.get(i).smej.get(j);
                if (vertices.get(i).getComp() != vertices.get(u).getComp()){
                    base.set(vertices.get(u).getComp() - 1, -1);
                }
            }
        }

        for (int i = 0; i < base.size(); i++){
            if (base.get(i) != -1){
                for (int j = 0; j < vertices.size(); j++){
                    if (vertices.get(j).getComp() == base.get(i)){
                        int k = 0;
                        for (k = 0; k < bverts.size(); k++){
                            if (bverts.get(k) > j) break;
                        }
                        bverts.add(k, j);
                        break;
                    }
                }
            }
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bverts.size(); i++){
            s.append(bverts.get(i));
            s.append(' ');
        }
        return s;
    }
}
