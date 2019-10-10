import java.util.ArrayList;
import java.util.Scanner;

public class Mars {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String lol = in.nextLine();
        Graph Gr = new Graph(n);
        for (int i = 0; i < n; i++){
            Gr.addEdge(in.nextLine(), i);
        }
        int l = Gr.dfs();
        if (l < 0){
            System.out.print("No solution");
        }
        else{
            System.out.println(Gr.sortForString(Gr.toStringB()));
        }
    }
}

class Vertex{
    public ArrayList<Integer> smej;
    private int clr;
    private int half;

    public Vertex(){
        this.smej = new ArrayList<>();
        this.clr = 0;
        this.half = 0;
    }

    public int getClr(){
        return this.clr;
    }

    public void setClr(int clr){
        this.clr = clr;
    }

    public void setHalf(int half){
        this.half = half;
    }

    public int getHalf(){
        return half;
    }
}

class Graph{
    private ArrayList<Vertex> vertices;
    public StringBuilder endgame;
    public StringBuilder[] parts;
    private ArrayList<StringBuilder> rez;

    public Graph(int a){
        this.vertices = new ArrayList<>();
        this.endgame = new StringBuilder();
        this.rez = new ArrayList<>();
        this.parts = new StringBuilder[2];
        for (int i = 0; i < 2; i++){
            this.parts[i] = new StringBuilder();
        }
        this.endgame.append("\nGraph {\n");
        for (int i = 0; i < a; i++){
            this.endgame.append(i);
            this.endgame.append("\n");
            this.vertices.add(new Vertex());
        }
    }

    public String sortForString(StringBuilder s){
        StringBuilder ss = new StringBuilder();
        int n = s.length();
        for (int i = 0; i < n; i++){
            int sznum = 0;
            int num = 0;
            int mn = 0;
            int mni = 0;
            int mnsz = 0;
            boolean boo = true;
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == ' '){
                    if ((num < mn) || (boo)){
                        mn = num;
                        mni = j;
                        mnsz = sznum;
                        boo = false;
                        num = 0;
                        sznum = 0;
                    }
                    else {
                        num = 0;
                        sznum = 0;
                    }
                }
                else {
                    num = num * 10 + ((int)s.charAt(j) - 48);
                    sznum++;
                }
            }

            int pos = mni - mnsz;
            for (int k = 0; k <= mnsz; k++){
                s.deleteCharAt(pos);
            }
            ss.append(mn);
            ss.append(" ");
            if (s.length() == 0) break;
        }
        return ss.toString();
    }

    public void addEdge(String s, int k){
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '-') count++;
            else if (s.charAt(i) == '+'){
                this.endgame.append(k);
                this.endgame.append(" -- ");
                this.endgame.append(count);
                this.endgame.append("\n");
                this.vertices.get(count).smej.add(k);
                this.vertices.get(k).smej.add(count);
                count++;
            }
        }
    }

    public StringBuilder toStringB(){
        StringBuilder mn = this.rez.get(0);
        int sz = 0;
        for (int i = 0; i < mn.length(); i++){
            if (mn.charAt(i) == ' ') sz++;
        }
        for (int i = 0; i < this.rez.size(); i++){
            int szss = 0;
            for (int j = 0; j < this.rez.get(i).length(); j++){
                if (this.rez.get(i).charAt(j) == ' ') szss++;
            }
            if (this.smartCompare(mn, this.rez.get(i), sz, szss) > 0) {
                mn = this.rez.get(i);
                sz = szss;
            }
        }
        return mn;
    }

    public int dfs(){
        for (int i = 0; i < this.vertices.size(); i++){
            if (this.vertices.get(i).getClr() == 0) {
                int l = this.visitVertex(i, 1);
                if (l < 0) return -1;
                int n = this.rez.size();
                if (n == 0){
                    this.rez.add(this.parts[0]);
                    this.rez.add(this.parts[1]);
                }
                for (int j = 0; j < n; j++){
                    String s = this.rez.get(j).toString();
                    this.rez.add(new StringBuilder(s).append(this.parts[0]));
                    this.rez.set(j, new StringBuilder(s).append(this.parts[1]));
                }
                for (int j = 0; j < 2; j++){
                    this.parts[j] = new StringBuilder();
                }
            }
        }
        return 1;
    }

    public int visitVertex(int i, int half){
        this.vertices.get(i).setClr(1);
        this.vertices.get(i).setHalf(half);
        this.smartAppend(i + 1, half);
        for (int j = 0; j < this.vertices.get(i).smej.size(); j++){
            if (this.vertices.get(this.vertices.get(i).smej.get(j)).getHalf() == this.vertices.get(i).getHalf()){
                return -1;
            }
            if (this.vertices.get(this.vertices.get(i).smej.get(j)).getClr() == 0){
                int nexthalf;
                if (half == 1) nexthalf = 2;
                else nexthalf = 1;
                int l = this.visitVertex(this.vertices.get(i).smej.get(j), nexthalf);
                if (l < 0) return -1;
            }
        }
        return 1;
    }

    public void smartAppend(int ss, int a){
        //System.out.println("int == " + ss);
        String s = this.parts[a - 1].toString();
        int pos = s.length();
        int num = 0;
        int tst = ss;
        int szss = 0;
        int sznum = 0;
        while (tst > 0){
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

        this.parts[a - 1].insert(pos - sznum, ss);
        this.parts[a - 1].insert(pos - sznum + szss, " ");
    }

    public int smartCompare(StringBuilder s, StringBuilder ss, int szs, int szss){
        int n = this.vertices.size();
        int a = n / 2 - szs;
        int aa = n / 2 - szss;
        if (a < 0) return 1;
        if (aa < 0) return -1;
        if (aa < a) return 1;
        if (aa == a){
            if (this.bitComp(ss.toString(), s.toString()) < 0) return 1;
            else return -1;
        }
        return 0;
    }

    public int bitComp(String s, String ss){
        int num1 = 0;
        int num2 = 0;
        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) == ' ') || (ss.charAt(i) == ' ')){
                if (s.charAt(i) == ' '){
                    int k = i;
                    for(;k < ss.length(); k++){
                        if (ss.charAt(k) == ' '){
                            break;
                        }
                        else{
                            num2 = num2 * 10 + ((int)ss.charAt(k) - 48);
                        }
                    }
                    if (num1 == num2){
                        num1 = 0;
                        num2 = 0;
                        s = s.substring(i);
                        ss = ss.substring(k);
                        i = 0;
                    }
                    else {
                        return num1 - num2;
                    }
                }
                else{
                    int k = i;
                    for(;k < s.length(); k++){
                        if (s.charAt(k) == ' '){
                            break;
                        }
                        else{
                            num1 = num1 * 10 + ((int)s.charAt(k) - 48);
                        }
                    }
                    if (num1 == num2){
                        num1 = 0;
                        num2 = 0;
                        s = s.substring(k);
                        ss = ss.substring(i);
                        i = 0;
                    }
                    else {
                        return num1 - num2;
                    }
                }
            }
            else {
                num1 = num1 * 10 + ((int)s.charAt(i) - 48);
                num2 = num2 * 10 + ((int)ss.charAt(i) - 48);
            }
        }
        return 0;
    }
}
