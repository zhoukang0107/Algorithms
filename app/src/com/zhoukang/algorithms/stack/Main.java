package com.zhoukang.algorithms.stack;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 栈：
 * 栈是一种操作受限的数据结构，只支持入栈和出栈操作。后进先出是它最大的特点。栈既可以通过数组实现，也可以通过链表来实现。不管基于数组还是链表，入栈、出栈的时间复杂度都为O(1)。除此之外，我们还讲了一种支持动态扩容的顺序栈，你需要重点掌握它的均摊时间复杂度分析方法。
 *
 * 栈在函数调用中的应用：
 *
 *
 * 栈在表达式求值中的应用
 * 实际上，编译器就是通过两个栈来实现的。其中一个保存操作数的栈，另一个是保存运算符的栈。我们从左向右遍历表达式，当遇到数字，我们就直接压入操作数栈；当遇到运算符，就与运算符栈的栈顶元素进行比较。
 * 如果比运算符栈顶元素的优先级高，就将当前运算符压入栈；如果比运算符栈顶元素的优先级低或者相同，从运算符栈中取栈顶运算符，从操作数栈的栈顶取2个操作数，然后进行计算，再把计算完的结果压入操作数栈，继续比较。
 *
 *
 * 栈在括号匹配中的应用：
 * 假设表达式中只包含三种括号，圆括号()、方括号[]和花括号{}，并且它们可以任意嵌套。比如， {[{}]}或[{()}([])]等都为合法格式，而{[}()]或[({)]为不合法的格式。那我现在给你一个包含三种括号的表达式字符串，如何检查它是否合法呢？
 * 用栈来保存未匹配的左括号，从左到右依次扫描字符串。当扫描到左括号时，则将其压入栈中；当扫描到右括号时，从栈顶取出一个左括号。如果能够匹配，比如“(”跟“)”匹配， “[”跟“]”匹配， “{”跟“}”匹配，则继续扫描剩下的字符串。如果扫描的过程中，遇到不能配对的右括号，或者栈中没有数据，则说明为非法格式。
 * 当所有的括号都扫描完成之后，如果栈为空，则说明字符串为合法格式；否则，说明有未匹配的左括号，为非法格式。
 *
 * 思考：
 * 1. 我们在讲栈的应用时，讲到用函数调用栈来保存临时变量，为什么函数调用要用“栈”来保存临时变量呢？用其他数据结构不行吗？
 * 2. 我们都知道， JVM内存管理中有个“堆栈”的概念。栈内存用来存储局部变量和方法调用，堆内存用来存储Java中的对象。那JVM里面的“栈”跟我们这里说的“栈”是不是一回事呢？如果不是，那它为什么又叫作“栈”呢？
 *
 */
public class Main {

    public static void main(String[] args){
        //System.out.println(isBracketMatch("[]{}()"));
        System.out.println(calculate("2+3-2/2+1*5-7*5"));

    }

    //栈在表达式求值中的应用
    public static int calculate(String str){
        Stack<Integer> operands = new Stack<>();
        Stack<Character> operator = new Stack<>();
        for (int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if (isOperator(c)){
                if (operator.isEmpty()){
                    operator.push(c);
                } else {
                    while (true){
                        if (!operator.isEmpty()&&!advanced(operator.peek() , c)){
                            //计算
                            operands.push(calculate(operator.pop(),operands.pop(), operands.pop()));
                        } else {
                            operator.push(c);
                            break;
                        }
                    }

                }
            } else {
                operands.push(c - '0');
            }
        }
        while (!operator.isEmpty()){
            operands.push(calculate(operator.pop(),operands.pop(), operands.pop()));
        }
        return operands.pop();
    }

    private static boolean advanced(Character c1, Character c2) {
        int c1v = 0;
        int c2v = 0;
        if (c1 == '+' || c1 == '-') {
             c1v = 0;
        } else {
            c1v = 1;
        }

        if (c2 == '+' || c2 == '-') {
            c2v = 0;
        } else {
            c2v = 1;
        }
        return c2v - c1v > 0;
    }

    private static Integer calculate(Character symbol, Integer pop2, Integer pop1) {
        int res = 0;
        switch (symbol){
            case '+':
                res = pop1 + pop2;
                break;
            case '-':
                res = pop1 - pop2;
                break;
            case '*':
                res = pop1 * pop2;
                break;
            case '/':
                res = pop1 / pop2;
                break;
        }
        return res;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' ||c == '/';
    }

    private static boolean isBracketMatch(String str) {
        Stack<Character> stack = new Stack<>();
        for (int i=0; i< str.length(); i++) {
            char c = str.charAt(i);
            switch (c){
                case '[':
                case '{':
                case '(':
                    stack.push(c);
                    break;
                case ')':
                    if (stack.isEmpty() || stack.pop()!='('){
                        return false;
                    }
                    break;
                case ']':
                    if (stack.isEmpty() || stack.pop()!='['){
                        return false;
                    }
                    break;
                case '}':
                    if (stack.isEmpty() || stack.pop()!='{'){
                        return false;
                    }
                    break;
                default:
            }
        }
        return stack.isEmpty();
    }


}

//使用数组实现可扩容栈
class ArrayStack{
    int count;
    String[] items;
    public ArrayStack(){
        items = new String[16];
        count = 0;
    }

    public void push(String value){
        if (count == items.length){
            String[] newArr = new String[2*items.length];
            System.arraycopy(items, 0, newArr,0 , count);
        }
        items[count] = value;
        count++;
    }


    public String pop(){
        if (count<=0){
            return null;
        }
        return items[--count];
    }
}

//模仿浏览器前进后退功能
class BrowserStack{
    Stack<String> forword;
    Stack<String> back;
    public BrowserStack(){
        forword = new Stack<>();
        back = new Stack<>();
    }

    //打开新页面
    public void newpager(String url){
        if (!back.isEmpty()){
            back.clear();
        }
        forword.push(url);
    }

    //前进
    public String forward(){
        if (back.isEmpty()){
            return null;
        }
        String url = back.pop();
        forword.push(url);
        return url;
    }

    //后退
    public String undo(){
        if (forword.isEmpty()){
            return null;
        }
        String url = forword.pop();
        back.push(url);
        return url;
    }
}

//无锁并发队列实现
class ConcorrentQueue{
    AtomicReference<Node> head;
    AtomicReference<Node> tail;
    public void push(int value){
        Node node = new Node();
        node.value = value;
        Node t = null;
        do {
            t = tail.get();
        } while (tail.compareAndSet(t, node));
    }

    public int pop(){
        return 0;
    }

    class Node{
        int value;
        AtomicReference<Node> next;
    }
}