public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    BacktrackingBST.Node root = null;
    int redo;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
        this.redo = 0;
    }

    public Node getRoot() {
        return root;
    }

    public Node search(int x) {
        return recSearch(root, x);
    }

    private Node recSearch(BacktrackingBST.Node node, int x) { //using the qualitites of BST.
        if (node == null || node.key == x)
            return node;
        if (node.key > x)
            return recSearch(node.left, x);
        else
            return recSearch(node.right, x);
    }

    public void insert(BacktrackingBST.Node z) { //inserting while maintaning qualities of BST.
        if (root == null)
            root = z;
        else
            recInsert(root, z);
        stack.push(z);
        stack.push(1);
        if(redo == 0)
            redoStack.clear();
    }

    private void recInsert(BacktrackingBST.Node node, BacktrackingBST.Node toIns) {
        if (node.key > toIns.key) {
            if (node.left != null)
                recInsert(node.left, toIns);
            else {
                node.left = toIns;
                toIns.parent = node;
            }
        } else {
            if (node.right != null)
                recInsert(node.right, toIns);
            else {
                node.right = toIns;
                toIns.parent = node;
            }
        }
    }

    public void delete(Node x) {
            stack.push(x);
            if (x.left == null && x.right == null) {// node has no children.
                if (x == root)
                    root = null;
                else if (x.parent.key > x.key) {
                    x.parent.left = null;
                }
                else
                    x.parent.right = null;
                stack.push(0);
            } else if (x.left != null && x.right != null) {// node has two children.
                BacktrackingBST.Node succ = new BacktrackingBST.Node(successor(x).key, successor(x).value);
                succ.parent = successor(x).parent;
                succ.right = successor(x).right;
                succ.left = successor(x).left;
                stack.push(succ);
                stack.push(x.key);
                if(successor(x).right!=null){
                    successor(x).right.parent = successor(x).parent;
                }
                if (succ.parent.key > succ.key) {
                    successor(x).parent.left = succ.right;
                }
                else {
                    successor(x).parent.right = succ.right;
                }
                x.key = succ.key;
                x.value = succ.value;
                stack.push(2);
            } else {// node has one child.
                if (x == root) {
                    if (x.left != null)
                        root = x.left;
                    else
                        root = x.right;
                } else if (x.left != null) {
                    if (x.parent.key < x.key)
                        x.parent.right = x.left;
                    else
                        x.parent.left = x.left;
                    x.left.parent = x.parent;
                } else {
                    if (x.parent.key < x.key)
                        x.parent.right = x.right;
                    else
                        x.parent.left = x.right;
                    x.right.parent = x.parent;
                }
                stack.push(1);
            }
            stack.push(0);// pushing the action.
        if(redo == 0)
            redoStack.clear();
    }


    public Node minimum() {
        if (root == null || root.left == null)
            return root;
        else
            return root.left.minimum();
    }

    public Node maximum() {
        if (root == null || root.right == null)
            return root;
        else
            return root.right.maximum();
    }

    public Node successor(Node x) {
        if (x.right != null)
            return x.right.minimum();
        else {
            BacktrackingBST.Node par = x.parent;
            while (par != null && x == par.right) {
                x = par;
                par = par.parent;
            }
            return par;
        }
    }

    public Node predecessor(Node x) {
        if (x.left != null)
            return x.left.maximum();
        else {
            BacktrackingBST.Node par = x.parent;
            while (par != null && x == par.left) {
                x = par;
                par = par.parent;
            }
            return par;
        }
    }

    @Override
    public void backtrack() {
        int action = (int) stack.pop();
        if (action == 0) {// last action was delete.
            int sons = (int) stack.pop();
            if (sons == 2) {// node had 2 sons.
                int ogKey = (int) stack.pop();
                BacktrackingBST.Node succ = (BacktrackingBST.Node) stack.pop();
                BacktrackingBST.Node node = (BacktrackingBST.Node) stack.pop();
                redoStack.push(node);
                redoStack.push(2);
                if (succ.parent.key > succ.key)
                    succ.parent.left = succ;
                else
                    succ.parent.right = succ;
                node.key = ogKey;
            } else { // node had 1 or 0 sons.
                BacktrackingBST.Node node = (BacktrackingBST.Node) stack.pop();
                redoStack.push(node);
                redoStack.push(0);
                if (node.parent == null) {// node was root before delete action.
                    if (root != null) {
                        if (node.key < root.key)
                            node.right = root;
                        else
                            node.left = root;
                        root.parent = node;
                        root = node;
                    } else
                        root = node;
                } else if (node.parent.key < node.key)
                    node.parent.right = node;
                else
                    node.parent.left = node;
            }
            redoStack.push(0);
        } else {// last action was insert.
            BacktrackingBST.Node node = (BacktrackingBST.Node) stack.pop();
            redoStack.push(node);
            redoStack.push(1);
            if (node.parent.key < node.key)
                node.parent.right = null;
            else
                node.parent.left = null;
        }
        System.out.println("backtracking performed");
    }


    @Override
    public void retrack() {
        if(!redoStack.isEmpty()) {
            redo = 1; //this is for NOT deleting redoStack.
            int action = (int) redoStack.pop();
            if (action == 1) { //last backtrack was insert.
                BacktrackingBST.Node node = (BacktrackingBST.Node) redoStack.pop();
                insert(node);
                stack.pop();
                stack.pop();
            } else { //last backtrack was delete.
                int sons = (int) redoStack.pop();
                BacktrackingBST.Node node = (BacktrackingBST.Node) redoStack.pop();
                delete(node);
                if (sons == 0)
                    for (int i = 0; i < 3; i = i + 1)
                        stack.pop();
                else
                    for (int i = 0; i < 5; i = i + 1)
                        stack.pop();
            }
            redo = 0;
        }
    }

    public void printPreOrder(){
        System.out.print(root.getKey());
        recPrint(root.left);
        recPrint(root.right);
    }

    private void recPrint(Node x) {
        if(x!=null) {
            System.out.print(" " + x.getKey());
            recPrint(x.left);
            recPrint(x.right);
        }
    }

    @Override
    public void print() {
        System.out.print(root.getKey());
        recPrint(root.left);
        recPrint(root.right);
    }


    public static class Node {
        //These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;

        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        private Node minimum() {
            if (this.left == null)
                return this;
            else
                return this.left.minimum();
        }

        private Node maximum() {
            if (this.right == null)
                return this;
            else
                return this.right.maximum();
        }
    }
}