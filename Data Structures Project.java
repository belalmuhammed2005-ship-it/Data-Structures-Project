import java.util.*;
import java.io.*;

// ===================== Queue =====================
class Queue {
    private ArrayList<String> list = new ArrayList<>();

    public void enqueue(String x) {
        list.add(x);
    }

    public String dequeue() {
        return list.remove(0);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public String get(int i) {
        return list.get(i);
    }

    public String toString() {
        return list.toString();
    }
}

// ===================== Stack =====================
class Stack {
    private ArrayList<String> list = new ArrayList<>();

    public void push(String x) {
        list.add(x);
    }

    public String pop() {
        return list.remove(list.size() - 1);
    }

    public String peek() {
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}

// ===================== BST =====================
class BST {
    class Node {
        String val;
        Node left, right;

        Node(String v) {
            val = v;
        }
    }

    Node root;

    public void buildFromPostfix(Queue q) {
        Stack st = new Stack();
        HashMap<String, Node> map = new HashMap<>();

        for (int i = 0; i < q.size(); i++) {
            String t = q.get(i);

            if (isNumber(t)) {
                map.put(t + i, new Node(t));
                st.push(t + i);
            } else {
                Node right = map.get(st.pop());
                Node left = map.get(st.pop());

                Node op = new Node(t);
                op.left = left;
                op.right = right;

                String key = t + i;
                map.put(key, op);
                st.push(key);
                root = op;
            }
        }
    }

    public void preorder(Node n, PrintWriter out) {
        if (n == null) return;
        out.print(n.val + " ");
        preorder(n.left, out);
        preorder(n.right, out);
    }

    public void inorder(Node n, PrintWriter out) {
        if (n == null) return;
        inorder(n.left, out);
        out.print(n.val + " ");
        inorder(n.right, out);
    }

    public void postorder(Node n, PrintWriter out) {
        if (n == null) return;
        postorder(n.left, out);
        postorder(n.right, out);
        out.print(n.val + " ");
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

// ===================== HashTable =====================
class HashTable {
    int size;
    double[] table;

    public HashTable(int s) {
        size = s;
        table = new double[size];
        Arrays.fill(table, -1);
    }

    int hash(double x) {
        return ((int) x) % size;
    }

    public void insert(double x) {
        int idx = hash(x);
        while (table[idx] != -1) {
            idx = (idx + 1) % size;
        }
        table[idx] = x;
    }

    public void print(PrintWriter out) {
        for (int i = 0; i < size; i++) {
            out.println(i + " : " + table[i]);
        }
    }
}

// ===================== Main =====================
public class Main {

    static int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static Queue toPostfix(Queue q) {
        Queue output = new Queue();
        Stack st = new Stack();

        for (int i = 0; i < q.size(); i++) {
            String t = q.get(i);

            if (isNumber(t)) {
                output.enqueue(t);
            } else if (t.equals("(")) {
                st.push(t);
            } else if (t.equals(")")) {
                while (!st.peek().equals("(")) {
                    output.enqueue(st.pop());
                }
                st.pop();
            } else {
                while (!st.isEmpty() &&
                        precedence(st.peek()) >= precedence(t)) {
                    output.enqueue(st.pop());
                }
                st.push(t);
            }
        }

        while (!st.isEmpty()) {
            output.enqueue(st.pop());
        }

        return output;
    }

    static double evaluate(Queue q) {
        Stack st = new Stack();

        for (int i = 0; i < q.size(); i++) {
            String t = q.get(i);

            if (isNumber(t)) {
                st.push(t);
            } else {
                double b = Double.parseDouble(st.pop());
                double a = Double.parseDouble(st.pop());

                switch (t) {
                    case "+": st.push(String.valueOf(a + b)); break;
                    case "-": st.push(String.valueOf(a - b)); break;
                    case "*": st.push(String.valueOf(a * b)); break;
                    case "/":
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        st.push(String.valueOf(a / b));
                        break;
                }
            }
        }

        return Double.parseDouble(st.pop());
    }

    static Queue tokenize(String s) {
        Queue q = new Queue();
        String[] arr = s.split(" ");
        for (String x : arr) {
            q.enqueue(x);
        }
        return q;
    }

    public static void main(String[] args) {
        try {
            // قراءة من ملف
            Scanner fileReader = new Scanner(new File("input.txt"));
            String input = fileReader.nextLine();

            // كتابة في ملف
            PrintWriter out = new PrintWriter("output.txt");

            Queue infix = tokenize(input);
            out.println("Queue: " + infix);

            Queue postfix = toPostfix(infix);
            out.println("Postfix: " + postfix);

            double result = evaluate(postfix);
            out.println("Result: " + result);

            // BST
            BST tree = new BST();
            tree.buildFromPostfix(postfix);

            out.print("Preorder: ");
            tree.preorder(tree.root, out);
            out.println();

            out.print("Inorder: ");
            tree.inorder(tree.root, out);
            out.println();

            out.print("Postorder: ");
            tree.postorder(tree.root, out);
            out.println();

            // HashTable
            HashTable ht = new HashTable(10);

            for (int i = 0; i < infix.size(); i++) {
                String t = infix.get(i);
                if (isNumber(t)) {
                    ht.insert(Double.parseDouble(t));
                }
            }

            out.println("Hash Table:");
            ht.print(out);

            out.close();
            fileReader.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}