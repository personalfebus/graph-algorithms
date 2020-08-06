# GraphBase

Пусть S1, S2, ... , Sk – компоненты сильной связности орграфа G. Конденсацией орграфа G называется орграф G* = ⟨V*,E*⟩, множеством вершин V* которого служит множество {S1, S2, ... , Sk}, а дуга ⟨Si, Sj⟩ является элементом множества E*, если в орграфе G есть по крайней мере одна дуга, исходящая из некоторой вершины компоненты Si и входящая в одну из вершин компоненты Sj.

База – это подмножество вершин орграфа, обладающее следующими свойствами:
1) каждая вершина орграфа достижима, по крайней мере, из одной вершины базы;
2) в базе нет вершин, достижимых из других вершин базы.

Очевидно, что в базе не может быть двух вершин, принадлежащих одной и той же компоненте сильной связности.

Также нетрудно доказать, что в ациклическом орграфе существует только одна база. Она состоит из всех вершин с полустепенью захода, равной 0.

С учётом вышесказанного поиск баз в оргрфае можно проводить в следующем порядке:
1) найти все компоненты сильной связности орграфа;
2) построить его конденсацию;
3) найти базу конденсации;
4) из каждой компоненты сильной связности, образующей вершину базы конденсации, взять по одной вершине.

Составьте программу, вычисляющую базу заданного орграфа. В случае, если языком реализации программы выбран язык Java, то точкой входа в программу должен являться метод main класса GraphBase.

Программа должна считывать со стандартного потока ввода количество вершин орграфа N, количество дуг M и данные о дугах орграфа. При этом каждая дуга кодируется парой чисел u и v, где u – номер вершины, из которой дуга исходит, а v – номер вершины, в которую дуга входит. Вершины нумеруются, начиная с нуля.

Для обеспечения уникальности ответа из компоненты сильной связности, образующей вершину базы конденсации, следует брать вершину с минимальным номером.

Программа должна выводить в стандартный поток вывода номера вершин базы, отсортированные в порядке возрастания.

# [Output info](https://personalfebus.s-ul.eu/graphimg/UCCkJvUM)