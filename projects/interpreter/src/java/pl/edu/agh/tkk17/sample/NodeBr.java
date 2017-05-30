package pl.edu.agh.tkk17.sample;

/**
 * Created by kamil on 30.05.17.
 */
public class NodeBr extends Node {

    private Node node;

    public NodeBr(Node node) {
        this.node = node;
    }

    public Node getInnerNode() {
        return node;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public void visitChild(NodeVisitor visitor) {
        if(node instanceof NodeBr)
            visitor.visit((NodeBr)node);
        else if(node instanceof NodeMul)
            visitor.visit((NodeMul)node);
        else if(node instanceof NodeAdd)
            visitor.visit((NodeAdd) node);
        else if(node instanceof NodeNumber)
            visitor.visit((NodeNumber) node);
    }
}
