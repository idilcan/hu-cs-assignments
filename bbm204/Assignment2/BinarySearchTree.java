import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BinarySearchTree{

    private HashMap<Integer, Integer> widthMap = new HashMap<>();
    private class TreeNode{
        int value;
        TreeNode leftChild;
        TreeNode rightChild;

        public TreeNode(int value){
            this.value = value;
            leftChild = null;
            rightChild = null;
        }
        public boolean hasLeftChild(){
            return leftChild != null;
        }
        public boolean hasRightChild(){ return rightChild != null; }
        public void calculateWidth(int level)
        {
            if(widthMap.containsKey(level))
                widthMap.put(level, widthMap.get(level) + 1);
            else
                widthMap.put(level, 1);

            if(leftChild != null)
                leftChild.calculateWidth(level + 1);
            if(rightChild != null)
                rightChild.calculateWidth(level + 1);
        }
    }
    private TreeNode root;
    private int height;
    private int width;
    public BinarySearchTree(ArrayList<Integer> array, BufferedWriter output) throws IOException {
        if(array.size() < 1){
            output.write("error");
            output.newline();
            return;
        }
        root = new TreeNode(array.get(0));
        height = 0;
        width = 0;
        array.remove(0);
        createBTS(array);
        calcWidth();
        calcHeight(root,0, output);
        output.write("BST created with elements: ");
        inorder(root, output);
        output.newLine();
    }
    public BinarySearchTree(int height, BufferedWriter output) throws IOException{
        if(height < 1){
            output.write("error");
            output.newline();
            return;
        }
        ArrayList<Integer> array = createArrayForFullBTS(height);
        root = new TreeNode(array.get(0));
        this.height = height;
        width = (int) Math.pow(2, height-1);
        array.remove(0);
        createBTS(array);
        output.write("A full BST created with elements: ");
        inorder(root, output);
        output.newLine();
    }
    private ArrayList<Integer> createArrayForFullBTS(int height) {
        ArrayList<Integer> returnArr = new ArrayList<>();
        reorder(1, (int) (Math.pow(2, height+1)-1), returnArr);
        return returnArr;
    }
    public void calcWidth(BufferedWriter output){
        if(this.root == null){
            output.write("error");
            output.newline();
            return;
        }
        root.calculateWidth(0);
        int i = 0;
        while(true){
            if(!widthMap.containsKey(i)){
                return;
            }
            if(widthMap.get(i) > width){
                this.width = widthMap.get(i);
            }
            i++;
        }
    }
    private void reorder(int head, int tail, ArrayList<Integer> returnArr){
        int middle = (head+tail)/2;
        returnArr.add(middle);
        if(middle == head) return;
        reorder(head,middle-1, returnArr);
        reorder(middle+1,tail, returnArr);
    }
    private void createBTS(ArrayList<Integer> array){
        if(array.isEmpty()) return;
        add(array.get(0), root);
        array.remove(0);
        createBTS(array);
    }
    private void add(int element, TreeNode treeRoot){
        if (element < treeRoot.value) {
            if (treeRoot.leftChild == null) {
                treeRoot.leftChild = new TreeNode(element);
                return;
            }
            add(element, treeRoot.leftChild);
        }
        if (element > treeRoot.value) {
            if (treeRoot.rightChild == null) {
                treeRoot.rightChild = new TreeNode(element);
                return;
            }
            add(element, treeRoot.rightChild);
        }
    }
    public void calcHeight(TreeNode root, int current, BufferedWriter output){
        if(this.root == null){
            output.write("error");
            output.newline();
            return;
        }
        int max = 0;
        for(int i :widthMap.keySet()){
            if(i > max){
                max = i;
            }
        }
        this.height = max;
    }
    public void delete(int elementToBeDeleted, BufferedWriter output) throws IOException{
        if(this.root == null){
            output.write("error");
            output.newline();
            return;
        }
        TreeNode tmp = null;
        ArrayList<TreeNode> toMove = new ArrayList<>();
        if(elementToBeDeleted == 0){
            output.write("Root Deleted:");
            output.write(Integer.toString(root.value));
            tmp = deleteRoot(root, output);
            output.newLine();
            if(tmp != null){
                root = tmp;
            }
        }
        if(elementToBeDeleted == -1){
            if(root.hasLeftChild()) {
                output.write("Left Child of Root Deleted:");
                output.write(Integer.toString(root.leftChild.value));
                tmp = deleteRoot(root.leftChild, output);
                output.newLine();
            }else {
                output.write("error");
                output.newLine();
            }
            if(tmp != null){
                TreeNode temp2 = root.leftChild.leftChild;
                root.leftChild = tmp;
                root.leftChild.leftChild = temp2;
            }
        }
        if(elementToBeDeleted == 1) {
            if(root.hasRightChild()) {
                output.write("Right Child of Root Deleted:");
                output.write(Integer.toString(root.rightChild.value));
                tmp = deleteRoot(root.rightChild, output);
                output.newLine();
            }else {
                output.write("error");
                output.newLine();
            }
            if(tmp != null){
                TreeNode temp2 = root.rightChild.leftChild;
                root.rightChild = tmp;
                root.rightChild.leftChild = temp2;
            }
        }
    }
    private TreeNode deleteRoot(TreeNode root, BufferedWriter output) {
        TreeNode tmp;
        if(root.hasRightChild()) {
            if (root.rightChild.hasLeftChild()) {
                tmp = pop(root);
                tmp.leftChild = root.leftChild;
                tmp.rightChild = root.rightChild;
                return tmp;
            }
        }
        return root.rightChild;
    }
    private TreeNode pop(TreeNode root){
        TreeNode tmp;
        if(root.hasRightChild()) {
            root = root.rightChild;
            while (root.leftChild.hasLeftChild()) {
                root = root.leftChild;
            }
        }
        tmp = root.leftChild;
        root.leftChild = root.leftChild.rightChild;
        return tmp;
    }
    public void leavesAsc(BufferedWriter output) throws IOException{
        if(this.root == null){
            output.write("error");
            output.newline();
            return;
        }
        output.write("LeavesAsc:");
        leaves(root,output);
        output.newLine();
    }
    private void leaves(TreeNode treeroot, BufferedWriter output) throws IOException{
        if(!treeroot.hasRightChild() && !treeroot.hasLeftChild()) {
            output.write(Integer.toString(treeroot.value));
            output.write(" ");
            return;
        }
        if(treeroot.hasLeftChild()) {
            leaves(treeroot.leftChild, output);
        }
        if(treeroot.hasRightChild()){
            leaves(treeroot.rightChild, output);
        }
    }
    public void inorder(TreeNode treeroot, BufferedWriter output) throws IOException{
        //left root right
        if(treeroot.hasLeftChild()) {
            inorder(treeroot.leftChild, output);
        }
        output.write(Integer.toString(treeroot.value));
        output.write(" ");
        if(treeroot.hasRightChild()){
            inorder(treeroot.rightChild, output);
        }
    }
    private void preord(TreeNode root,BufferedWriter output) throws IOException{
        //root left right
        output.write(Integer.toString(root.value));
        output.write(" ");
        if(root.hasLeftChild()) {
            preord(root.leftChild,output);
        }
        if(root.hasRightChild()){
            preord(root.rightChild,output);
        }
    }
    public void preorder(BufferedWriter output) throws IOException{
        if(this.root == null){
            output.write("error");
            output.newline();
            return;
        }
        output.write("Preorder:");
        preord(root,output);
        output.newLine();
    }
    public void getHeight(BufferedWriter output) throws IOException{
        output.write("Height:");
        output.write(Integer.toString(height));
        output.newLine();
    }
    public void getWidth(BufferedWriter output) throws IOException{
        output.write("Width:");
        output.write(Integer.toString(width));
        output.newLine();
    }
}