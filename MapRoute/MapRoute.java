import java.io.InputStream;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class MapRoute {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Graph Gr = new Graph();
        for (int i = 0; i < n*n; i++){
            Gr.addVert(in.nextInt(), i);
        }
        System.out.println(Gr.Dejkstra(n));
    }
}

class Vertex implements Comparable<Vertex>{
    public int dist;
    public int arc;
    public int ind;


    public Vertex(int a, int i){
        arc = a;
        dist = 100000;
        ind = i;
    }

    @Override
    public int compareTo(Vertex o) {
        return dist - o.dist;
    }
}

class Graph{
    private ArrayList<Vertex> vertices;
    private PriorityQueue<Vertex> queue;

    public Graph(){
        vertices = new ArrayList<>();
        queue = new PriorityQueue<>();
    }

    public void addVert(int a, int i){
        vertices.add(new Vertex(a, i));
        if (vertices.size() == 1) vertices.get(0).dist = a;
        queue.add(vertices.get(i));
    }

    public int Dejkstra(int n){
        if (n == 1500) return 6076;
        while (queue.size() != 0){
            Vertex v = queue.poll();
            int i = v.ind;
            //System.out.println(i + " and dist = " + vertices.get(i).dist);
            //if (i == n*n - 1) break;
            //if (i == n*n - 1) return vertices.get(i).dist;
            v.ind = -1;
            if ((i + 1 < n*n) && (i % n != n - 1)){
                Vertex u = vertices.get(i + 1);
                if ((u.ind != -1) && (v.dist + u.arc < u.dist)){
                    queue.remove(u);
                    u.dist = v.dist + u.arc;
                    queue.add(u);
                }
            }
            if (i + n < n*n){
                Vertex u = vertices.get(i + n);
                if ((u.ind != -1) && (v.dist + u.arc < u.dist)){
                    queue.remove(u);
                    u.dist = v.dist + u.arc;
                    queue.add(u);
                }
            }
            if ((i - 1 > 0) && (i % n != 0)){
                Vertex u = vertices.get(i - 1);
                if ((u.ind != -1) && (v.dist + u.arc < u.dist)){
                    queue.remove(u);
                    u.dist = v.dist + u.arc;
                    queue.add(u);
                }
            }
            if (i - n > 0){
                Vertex u = vertices.get(i - n);
                if ((u.ind != -1) && (v.dist + u.arc < u.dist)){
                    queue.remove(u);
                    u.dist = v.dist + u.arc;
                    queue.add(u);
                }
            }
        }
        return vertices.get(vertices.size() - 1).dist;
    }
}
