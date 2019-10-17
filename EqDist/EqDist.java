import java.util.ArrayList;
import java.util.Scanner;

public class EqDist {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Graph Gr = new Graph(n);
        int m = in.nextInt();
        for (int i = 0; i < m; i++) {
            int vert1 = in.nextInt();
            int vert2 = in.nextInt();
            Gr.addEdge(vert1, vert2);
        }
        int k = in.nextInt();
        for (int i = 0; i < k; i++){
            int vert = in.nextInt();
            Gr.addMain(vert);
        }
        Gr.findEq();
        System.out.println();
    }
}

class Dist{
    int num;
    int dist;

    public Dist(int num, int dist){
        this.dist = dist;
        this.num = num;
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
    private int distfrommain;
    public boolean equ;

    public Vertex(){
        this.smej = new ArrayList<>();
        this.clr = -1;
        this.distfrommain = 0;
        this.equ = true;
    }

    public int getClr(){
        return this.clr;
    }

    public void setClr(int clr){
        this.clr = clr;
    }

    public int getDistfrommain() {
        return distfrommain;
    }

    public void setDistfrommain(int distfrommain) {
        this.distfrommain = distfrommain;
    }
}

class Graph {
    private ArrayList<Vertex> vertices;
    private StringBuilder endgame;
    private StringBuilder goodvert;
    private ArrayList<Integer> mainvert;

    public Graph(int a) {
        this.vertices = new ArrayList<>();
        this.endgame = new StringBuilder();
        this.mainvert = new ArrayList<>();
        this.goodvert = new StringBuilder();
        this.endgame.append("graph {\n");
        for (int i = 0; i < a; i++) {
            this.endgame.append("\t");
            this.endgame.append(i);
            this.endgame.append("\n");
            this.vertices.add(new Vertex());
        }
    }

    public String toString(){
        return this.goodvert.toString();
    }

    public String getGraph(){
        return this.endgame.append("}\n").toString();
    }

    public void addEdge(int vert1, int vert2){
        this.endgame.append("\t");
        this.endgame.append(vert1);
        this.endgame.append(" -- ");
        this.endgame.append(vert2);
        this.endgame.append("\n");
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

    public void addMain(int vert){
        this.mainvert.add(vert);
        this.vertices.get(vert).setDistfrommain(-1);
    }

    public void findEq(){
        boolean boo = false;
        for (int i = 0; i < this.mainvert.size(); i++){
            boo = ((boo) || (bfs(this.mainvert.get(i), i)));
        }
        if (boo) System.out.println(this.toString());
        else System.out.println("-");
    }

    public boolean bfs(int currmain, int maincount){
        ArrayList<Dist> tmp = new ArrayList<>();
        boolean boo = false;
        if (this.vertices.get(currmain).getClr() < maincount){
            this.vertices.get(currmain).setClr(maincount);
            tmp.add(new Dist(currmain, 0));
            for (int j = 0; j < tmp.size(); j++){
                int k = tmp.get(j).num;
                int currdist = tmp.get(j).dist;
                tmp.remove(j);
                j--;
                if ((maincount == 0) && (this.vertices.get(k).getDistfrommain() >= 0)){
                    this.vertices.get(k).setDistfrommain(currdist);
                }
                else if (this.vertices.get(k).getDistfrommain() != currdist) {
                    this.vertices.get(k).equ = false;
                }
                else if ((maincount == this.mainvert.size() - 1) && (this.vertices.get(k).equ)){
                    this.smartAppend(k);
                    boo = true;
                }

                for (int jj = 0; jj < this.vertices.get(k).smej.size(); jj++){
                    int next = this.vertices.get(k).smej.get(jj).num;
                    if (this.vertices.get(next).getClr() < maincount){
                        this.vertices.get(next).setClr(maincount);
                        tmp.add(new Dist(next, currdist + 1));
                    }
                }
            }
        }
        for (int i = 0; i < this.vertices.size(); i++){
            if (this.vertices.get(i).getClr() < maincount){
                this.vertices.get(i).setDistfrommain(-1);
            }
        }
        return boo;
    }

    public void smartAppend(int ss){
        String s = this.goodvert.toString();
        int pos = s.length();
        int num = 0;
        int tst = ss;
        int szss = 1;
        int sznum = 0;
        while (tst > 9){
            szss++;
            tst /= 10;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' '){
                if (num > ss){
                    pos = i;
                    break;
                }
                else {
                    num = 0;
                    sznum = 0;
                }
            }
            else {
                num = num * 10 + ((int)s.charAt(i) - 48);
                sznum++;
            }
        }

        this.goodvert.insert(pos - sznum, ss);
        this.goodvert.insert(pos - sznum + szss, " ");
    }
}