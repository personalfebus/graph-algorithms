import java.util.ArrayList;
import java.util.Scanner;

public class Dividers {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        long num = in.nextLong();
        Graph Gr = new Graph(num);
        System.out.println(Gr.toString());
    }
}

class Vertex{
    public ArrayList<Integer> smej;
    private long num;
    private long prevdiv;
    boolean indicator;

    public Vertex(long a){
        this.smej = new ArrayList<>();
        this.num = a;
        this.prevdiv = 0;
    }

    public long getNum() {
        return this.num;
    }

    public long getPrevdiv() {
        return prevdiv;
    }

    public void setPrevdiv(long prevdiv) {
        this.prevdiv = prevdiv;
    }
}

class Graph{
    private ArrayList<Vertex> vertices;
    private StringBuilder vert;
    private StringBuilder edg;

    public Graph(long a){
        this.vertices = new ArrayList<>();
        this.vert = new StringBuilder("graph {\n");
        this.edg = new StringBuilder();

        ArrayList<Long> div = new ArrayList<>();
        long kor = (long)Math.sqrt(a);

        for (long j = 1; j <= kor; j++){
            if (a % j == 0) div.add(j);
        }

        int prsize = div.size() - 1;

        for (int i = prsize; i >= 0; i--){
            long k = a / div.get(i);
            if (k != kor) div.add(k);
        }

        for (int kk = div.size() - 1; kk >= 0; kk--){
            long i = div.get(kk);
            this.vertices.add(new Vertex(i));
            this.vert.append("    ");
            this.vert.append(i);
            this.vert.append("\n");
            int curr = this.vertices.size() - 1;
            for (int j = 0; j < curr; j++){
                long it = this.vertices.get(j).getNum();
                boolean wh = true;
                if (it % i == 0){
                    for (int jj = 0; jj < this.vertices.get(j).smej.size(); jj++){
                        long smthg = this.vertices.get(this.vertices.get(j).smej.get(jj)).getNum();
                        if ((smthg % i == 0) && (smthg < it)){
                            wh = false;
                            break;
                        }
                    }
                    if (wh){
                        this.vertices.get(j).smej.add(curr);
                        this.edg.append("    ");
                        this.edg.append(it);
                        this.edg.append(" -- ");
                        this.edg.append(i);
                        this.edg.append("\n");
                    }
                }
            }
        }
    }

    public int VisitVertex(int pos, long start, int startpos){
        this.vertices.get(pos).setPrevdiv(start);
        if (this.vertices.get(pos).getNum() % start != 0) {
            this.vertices.get(pos).indicator = false;
            return -1;
        }
        int index = pos;
        for (int j = 0; j < this.vertices.get(pos).smej.size(); j++){
            int nextpos = this.vertices.get(pos).smej.get(j);
            if (this.vertices.get(nextpos).getPrevdiv() != start){
                int curr = this.VisitVertex(nextpos, start, startpos);
                if (curr >= 0) {
                    index = curr;
                }
            }
           else {
                if ((this.vertices.get(nextpos).indicator) && (this.vertices.get(nextpos).getPrevdiv() == start)) {
                    this.vertices.get(pos).indicator = true;
                    index = -1;
                    return -1;
                }
            }
        }
        if (index == pos){
            this.vertices.get(index).smej.add(startpos);
            this.edg.append("    ");
            this.edg.append(this.vertices.get(index).getNum());
            this.edg.append(" -- ");
            this.edg.append(start);
            this.edg.append("\n");
            this.vertices.get(index).indicator = true;
        }
        return index;
    }

    public String toString(){
        return this.vert.append(this.edg.toString()).append("}").toString();
    }
}
