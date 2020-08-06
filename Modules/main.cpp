#include <iostream>
#include <unordered_map>
#include <vector>
#include <stack>

using namespace std;

int time = 1;
int count = 1;

class Vertex{
public:
    int comp;
    int t1;
    int low;
    Vertex();
};

Vertex::Vertex() {
    comp = 0;
    t1 = 0;
    low = 0;
}

class Graph{
public:
    vector<Vertex> vertecies;
    vector<vector<int>> smej;
    stack<int> st;

    Graph() {}
    void addVertex();
    void addEdge(int n1, int n2);
    void tarjan();
    void visit_vertex(int v);
};

void Graph::addEdge(int n1, int n2) {
    if (n1 != n2) {
        smej[n1].push_back(n2);
    }
}

void Graph::addVertex() {
    vertecies.push_back(Vertex());
    vector<int> vector;
    smej.push_back(vector);
}

void Graph::tarjan() {
    for (int i = 0; i < vertecies.size(); i++){
        if (vertecies[i].t1 == 0){
            visit_vertex(i);
        }
    }
}

void Graph::visit_vertex(int v) {
    vertecies[v].t1 = time;
    vertecies[v].low = time;
    time++;
    st.push(v);
    for (int i = 0; i < smej[v].size(); i++){
        int u = smej[v][i];
        if (vertecies[u].t1 == 0){
            visit_vertex(u);
        }
        if ((vertecies[u].comp == 0) && (vertecies[v].low > vertecies[u].low)){
            vertecies[v].low = vertecies[u].low;
        }
    }
    if (vertecies[v].t1 == vertecies[v].low){
        for (;;) {
            int u = st.top();
            st.pop();
            vertecies[u].comp = count;
            if (u == v) break;
        }
        count++;
    }
}

int parse_program(string str, Graph *graph);
int parse_function(string str, Graph *graph);
string parse_identif(string str, Graph *graph);
int parse_formal_args_list(string str, Graph *graph);
int parse_ident_list(string str, Graph *graph);
int parse_expr(string str, Graph *graph);
int parse_comparition_expr(string str, Graph *graph);
int parse_arith_expr(string str, Graph *graph);
int parse_term(string str, Graph *graph);
int parse_factor(string str, Graph *graph);
int parse_actual_args_list(string str, string func_name, Graph *graph);
int parse_expr_list(string str, string func_name, int args_num, Graph *graph);
bool is_latin(int code);
bool is_number(int code);

bool pr_error = false;
int current_position = 0;
int func_count = 0;
string current_function = "";
unordered_map<string, vector<string>> identif;
unordered_map<string, int> unconfiremed_func_call;
unordered_map<string, int> func_to_num;

int main() {
    string program;
    for (string line; getline(cin, line);){
        program.append(line);
    }
   
    Graph *graph = new Graph();
   
    parse_program(program, graph);
    if (pr_error || !unconfiremed_func_call.empty()){
        cout << "error\n";
    } else {
        graph->tarjan();
        cout << count - 1;
    }

    delete graph;
    return 0;
}

int parse_program(string str, Graph *graph){
    for (; current_position < str.length();) {
        parse_function(str, graph);
        if (pr_error) {
            return 0;
        }
    }
    return 0;
}

int parse_function(string str, Graph *graph){
    string function_name = parse_identif(str, graph);
    identif.insert(make_pair(function_name, vector<string>()));
    current_function = function_name;
    auto elem = func_to_num.find(function_name);
    if (elem == func_to_num.end()){
        func_to_num.insert(make_pair(function_name, func_count));
        graph->addVertex();
        func_count++;
    }

    for (int i = current_position; i < str.length(); i++){
        if (str[i] == ' '){
            continue;
        }
        if (str[i] == '('){
            current_position = i;
            break;
        } else {
            pr_error = true;
            current_position = str.length() - 1;
            return -1;
        }
    }

    parse_formal_args_list(str, graph);
    auto un_func_and_var = unconfiremed_func_call.find(current_function);
    auto func_and_var = identif.find(current_function);
    if (un_func_and_var != unconfiremed_func_call.end()){
        if (un_func_and_var->second != func_and_var->second.size()){
            pr_error = true;
            current_position = str.length() - 1;
            return -1;
        } else {
            unconfiremed_func_call.erase(current_function);
        }
    }
    for (int i = current_position; i < str.length(); i++) {
        if (str[i] == ' ') {
            continue;
        }
        if (str[i] == ':') {
            if (str[i + 1] == '='){
                current_position = i + 2;
                parse_expr(str, graph);
                for (int j = current_position; j < str.length(); j++){
                    if (str[j] == ' ') continue;
                    if (str[j] == ';'){
                        current_position = j + 1;
                        return 0;
                    } else {
                        break;
                    }
                }
                pr_error = true;
                current_position = str.length() - 1;
                return -1;
            }
            pr_error = true;
            current_position = str.length() - 1;
            return -1;
        }
    }
    return 0;
}

int parse_expr(string str, Graph *graph){
    parse_comparition_expr(str, graph);
    for (int i = current_position; i < str.length(); i++){
        if (str[i] == ' ') continue;
        if (str[i] == '?'){
            current_position = i + 1;
            parse_comparition_expr(str, graph);

            for (int j = current_position; j < str.length(); j++){
                if (str[j] == ' ') continue;
                if (str[j] == ':'){
                    current_position = j + 1;
                    parse_expr(str, graph);
                    return 0;
                } else {
                    break;
                }
            }
            pr_error = true;
            current_position = str.length() - 1;
            return -1;
        } else {
            current_position = i;
            return 0;
        }
    }
    pr_error = true;
    current_position = str.length() - 1;
    return -1;
}

int parse_comparition_expr(string str, Graph *graph){
    parse_arith_expr(str, graph);
    for (int i = current_position; i < str.length(); i++){
        if (str[i] == ' ') continue;
        if (str[i] == '='){
            current_position = i + 1;
            parse_arith_expr(str, graph);
            return 0;
        } else if (str[i] == '<'){
            if (str[i + 1] == '>' || str[i + 1] == '='){
                current_position = i + 2;
                parse_arith_expr(str, graph);
                return 0;
            } else {
                current_position = i + 1;
                parse_arith_expr(str, graph);
                return 0;
            }
        } else if (str[i] == '>'){
            if (str[i + 1] == '='){
                current_position = i + 2;
                parse_arith_expr(str, graph);
                return 0;
            } else {
                current_position = i + 1;
                parse_arith_expr(str, graph);
                return 0;
            }
        } else {
            current_position = i;
            return 0;
        }
    }
    pr_error = true;
    current_position = str.length() - 1;
    return -1;
}

int parse_arith_expr(string str, Graph *graph){
    parse_term(str, graph);
    for (int i = current_position; i < str.length(); i++){
        if (str[i] == ' ') continue;
        if (str[i] == '+' || str[i] == '-'){
            current_position = i + 1;
            parse_term(str, graph);
            return 0;
        } else {
            current_position = i;
            return 0;
        }
    }
    pr_error = true;
    current_position = str.length() - 1;
    return -1;
}

int parse_term(string str, Graph *graph){
    parse_factor(str, graph);
    for (int i = current_position; i < str.length(); i++){
        if (str[i] == ' ') continue;
        if (str[i] == '*' || str[i] == '/'){
            current_position = i + 1;
            parse_term(str, graph);
            return 0;
        } else {
            current_position = i;
            return 0;
        }
    }
    pr_error = true;
    current_position = str.length() - 1;
    return -1;
}

int parse_factor(string str, Graph *graph){

    for (int i = current_position; i < str.length(); i++){
        int ac_code = (int)str[i];
        if (str[i] == ' '){
            continue;
        }
        if (is_number(ac_code)){
            for (int j = i + 1; j < str.length(); j++){
                if (!is_number((int)str[j])){
                    current_position = j;
                    return 0;
                }
            }
            pr_error = true;
            current_position = str.length() - 1;
            return -1;
        } else if (str[i] == '-'){
            current_position = i + 1;
            parse_factor(str, graph);
            return 0;
        } else if (str[i] == '('){
            current_position = i + 1;
            parse_expr(str, graph);
            for (int j = current_position; j < str.length(); j++){
                if (str[j] == ' ') continue;
                if (str[j] == ')'){
                    current_position = j + 1;
                    return 0;
                } else {
                    current_position = str.length() - 1;
                    pr_error = true;
                    return -1;
                }
            }
            current_position = str.length() - 1;
            pr_error = true;
            return -1;
        } else {
            string name = parse_identif(str, graph);
            for (int j = current_position; j < str.length(); j++){
                if (str[j] == ' ') continue;
                if (str[j] == '('){
                    current_position = j + 1;
                    int args_num = parse_actual_args_list(str, name, graph);
                    if (args_num == -1){
                        pr_error = true;
                        current_position = str.length() - 1;
                        return -1;
                    }
                    auto func_and_var = identif.find(name);
                    if (func_and_var == identif.end()){
                        unconfiremed_func_call.insert(make_pair(name, args_num));
                        auto elem = func_to_num.find(name);
                        if (elem == func_to_num.end()){
                            func_to_num.insert(make_pair(name, func_count));
                            graph->addVertex();
                            func_count++;
                        }
                    } else {
                        if (func_and_var->second.size() == args_num){

                        } else {
                            pr_error = true;
                            current_position = str.length() - 1;
                            return -1;
                        }
                    }
                    auto el1 = func_to_num.find(name);
                    auto el2 = func_to_num.find(current_function);
                    bool need = true;
                    for (int kk : graph->smej[el2->second]){
                        if (kk == el1->second){
                            need = false;
                            break;
                        }
                    }
                    if (need){
                        graph->addEdge(el2->second, el1->second);
                    }
                    return 0;
                } else {
                    auto func_and_var = identif.find(current_function);
                    for (int k = 0; k < func_and_var->second.size(); k++){
                        if (name == func_and_var->second[k]){
                            current_position = j;
                            return 0;
                        }
                    }
                    pr_error = true;
                    current_position = str.length() - 1;
                    return -1;
                }
            }
        }
    }
    pr_error = true;
    current_position = str.length() - 1;
    return -1;
}

int parse_actual_args_list(string str, string func_name, Graph *graph){

    for (int i = current_position; i < str.length(); i++){
        if (str[i] == ' ') continue;
        if (str[i] == ')'){
            current_position = i + 1;
            return 0;
        } else {
            current_position = i;
            int args_num = parse_expr_list(str, func_name, 0, graph);

            for (int j = current_position; j < str.length(); j++){
                if (str[j] == ' ') continue;
                if (str[j] == ')'){
                    current_position = j + 1;
                    return args_num;
                } else {
                    current_position = str.length() - 1;
                    pr_error = true;
                    return -1;
                }
            }
            current_position = str.length() - 1;
            pr_error = true;
            return -1;
        }
    }
    current_position = str.length() - 1;
    pr_error = true;
    return -1;
}

int parse_expr_list(string str, string func_name, int args_num, Graph *graph){
    args_num++;
    parse_expr(str, graph);
    for (int i = current_position; i < str.length(); i++){
        if (str[i] == ' ') continue;
        if (str[i] == ',') {
            current_position = i + 1;
            args_num = parse_expr_list(str, func_name, args_num, graph);
            i = current_position - 1;
        } else {
            current_position = i;
            return args_num;
        }
    }
    pr_error = true;
    current_position = str.length() - 1;
    return -1;
}

int parse_formal_args_list(string str, Graph *graph){
    for (int i = current_position + 1; i < str.length(); i++){
        if (str[i] == ' '){
            continue;
        } else if (str[i] == ')'){
            current_position = i + 1;
            return 0;
        } else {
            current_position = i;
            parse_ident_list(str, graph);
            return 0;
        }
    }
    pr_error = true;
    return -1;
}

int parse_ident_list(string str, Graph *graph){
    string variable_name = parse_identif(str, graph);
    auto func_and_var = identif.find(current_function);
    func_and_var->second.push_back(variable_name);

    for(;;) {
        for (int i = current_position; i < str.length(); i++) {
            if (i >= str.length() - 1){
                current_position = i;
                pr_error = true;
                return -1;
            }
            if (str[i] == ' ') {
                continue;
            } else if (str[i] == ')') {
                current_position = i + 1;
                return 0;
            } else if (str[i] == ',') {
                current_position = i + 1;
                string variable_name1 = parse_identif(str, graph);
                func_and_var->second.push_back(variable_name1);
                break;
            }
        }

        if (current_position >= str.length() - 1){
            break;
        }
    }

    pr_error = true;
    return -1;
}

string parse_identif(string str, Graph *graph){
    bool need_latin = true;

    for (int i = current_position; i < str.length(); i++) {
        if (str[i] != ' ') {
            current_position = i;
            break;
        }
    }

    for (int i = current_position; i < str.length(); i++){
        int ac_code = (int)str[i];
        if (need_latin){
            if (is_latin(ac_code)){
                need_latin = false;
            } else {
                pr_error = true;
                return "";
            }
        } else if (!is_latin(ac_code) && !is_number(ac_code)){
            string name = str.substr(current_position, i - current_position);
            current_position = i;
            return name;
        }
    }
    pr_error = true;
    return "";
}

bool is_latin(int code){
    return ((code > 64) && (code < 91)) || ((code > 96) && (code < 123));
}

bool is_number(int code){
    return (code > 47) && (code < 58);
}