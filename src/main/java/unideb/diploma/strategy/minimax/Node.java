package unideb.diploma.strategy.minimax;

import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private int maxDepth;
    private int actualDepth;
    private List<Node> childNodes;
    private List<Operator> useableOperators;
    private State state;
    private Operator madeBy;
    private Node parent;

    public Node(int maxDepth, int actualDepth, State state, List<Operator> useableOperators, Operator madeBy, Node parent){
        this.maxDepth = maxDepth;
        this.actualDepth = actualDepth;
        childNodes = new ArrayList<>();
        this.useableOperators = useableOperators;
        this.state = state;
        this.madeBy = madeBy;
        this.parent = parent;
        makeChildNotes(state.clone());
    }

    private void makeChildNotes(State state){
        if(actualDepth < maxDepth) {
            for (Operator operator : useableOperators) {
                List<Operator> copyOfUseableOperators = new ArrayList<>(useableOperators);
                state.applyOperator(operator);
                copyOfUseableOperators.remove(operator);
                childNodes.add(new Node(maxDepth, actualDepth + 1, state.clone(), copyOfUseableOperators, operator, this));
                state.getFieldAt(operator.getPosition()).setColor(FieldColor.WHITE);
            }
        }
    }

    public boolean isLeafNode() {
    	return state.isEndState() || (actualDepth == maxDepth);
    }

    public List<Node> getChildNodes(){
        return childNodes;
    }

    public Operator getMadeBy(){
        return madeBy;
    }

    public State getState(){
        return state;
    }

    public int getActualDepth(){
        return actualDepth;
    }
    
    public Node getParent() {
    	return parent;
    }
}
