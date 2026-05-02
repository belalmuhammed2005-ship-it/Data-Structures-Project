import java.io.*;
import java.util.*;

// ===================== Queue =====================
class Queue {
    private final LinkedList<String> list = new LinkedList<>();

    public void enqueue(String x) {
        list.add(x);
    }

    public String dequeue() {
        if (list.isEmpty()) throw new RuntimeException("Queue is empty");
        return list.removeFirst();
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

    @Override
    public String toString() {
        return list.toString();
    }
}

// ===================== Stack (String - for operators & tree) =====================
class Stack {
    private final ArrayList<String> list = new ArrayList<>();

    public void push(String x) {
        list.add(x);
    }

    public String pop() {
        if (list.isEmpty()) throw new RuntimeException("Stack is empty");
        return list.remove(list.size() - 1);
    }

    public String peek() {
        if (list.isEmpty()) throw new RuntimeException("Stack is empty");
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}

// ===================== Stack (Double - for evaluation) =====================
class DoubleStack {
    private final ArrayList<Double> list = new ArrayList<>();

    public void push(double x) {
        list.add(x);
    }

    public double pop() {
        if (list.isEmpty()) throw new RuntimeException("Stack is empty");
        return list.remove(list.size() - 1);
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

    // Build Expression Tree from Postfix
    public void buildFromPostfix(Queue q) {
        Stack st = new Stack();
        HashMap<String, Node> map = new HashMap<>();

        for (int i = 0; i < q.size(); i++) {
            String t = q.get(i);

            if (isNumber(t)) {
                map.put(t + i, new Node(t));
                st.push(t + i);
            } else {
                if (st.size() < 2) throw new RuntimeException("Invalid expression");

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

        if (root == null) throw new RuntimeException("Invalid Tree");
    }

    // Traversals
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
            Double.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
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
        return Math.abs((int) x) % size;
    }

    public void insert(double x) {
        int idx = hash(x);
        int start = idx;

        while (table[idx] != -1) {
            idx = (idx + 1) % size;
            if (idx == start) throw new RuntimeException("HashTable Full");
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

    // Operator precedence
    static int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    // Check if token is number
    static boolean isNumber(String s) {
        try {
            Double.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Convert Infix to Postfix
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
                while (!st.isEmpty() && !st.peek().equals("(")) {
                    output.enqueue(st.pop());
                }
                if (!st.isEmpty()) st.pop();
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

    // Evaluate Postfix (FIXED: no unnecessary String conversions)
    static double evaluate(Queue q) {
        DoubleStack st = new DoubleStack();

        for (int i = 0; i < q.size(); i++) {
            String t = q.get(i);

            if (isNumber(t)) {
                st.push(Double.parseDouble(t));
            } else {
                if (st.size() < 2) throw new RuntimeException("Invalid expression");

                double b = st.pop();
                double a = st.pop();

                switch (t) {
                    case "+" -> st.push(a + b);
                    case "-" -> st.push(a - b);
                    case "*" -> st.push(a * b);
                    case "/" -> {
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        st.push(a / b);
                    }
                }
            }
        }

        return st.pop();
    }

    // Convert string to tokens
    static Queue tokenize(String s) {
        Queue q = new Queue();
        String[] arr = s.trim().split("\\s+");
        for (String x : arr) {
            q.enqueue(x);
        }
        return q;
    }

    public static void main(String[] args) {
        try (
            Scanner fileReader = new Scanner(new File("input.txt"));
            PrintWriter out = new PrintWriter("output.txt")
        ) {

            if (!fileReader.hasNextLine()) {
                throw new RuntimeException("Empty file");
            }

            String input = fileReader.nextLine();

            Queue infix = tokenize(input);
            out.println("Queue: " + infix);

            Queue postfix = toPostfix(infix);
            out.println("Postfix: " + postfix);

            double result = evaluate(postfix);
            out.println("Result: " + result);

            // Build Expression Tree
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

            // Hash Table
            HashTable ht = new HashTable(10);

            for (int i = 0; i < infix.size(); i++) {
                String t = infix.get(i);
                if (isNumber(t)) {
                    ht.insert(Double.parseDouble(t));
                }
            }

            out.println("Hash Table:");
            ht.print(out);

            System.out.println("Program executed successfully. Check output.txt");

        } catch (IOException | RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
