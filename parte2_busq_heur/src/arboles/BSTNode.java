package arboles;

public class BSTNode implements IBSTNode {
	BSTNode parent;
	BSTNode left;
	BSTNode right;
	int key;
	String elem;
	String ID;
	BSTNode(String e, int k){
		elem=e;
		key=k;
		ID=this.toString();
	}
	
	public int getSize() {
		return getSize(this);
	}

	public static int getSize(BSTNode node) {
		if (node == null)
			return 0;
		else
			return 1 + getSize(node.left) + getSize(node.right);
	}

	
	public int getHeight() {
		return getHeight(this);
	}

	protected static int getHeight(BSTNode node) {
		if (node == null)
			return -1;
		else
			return 1 + Math.max(getHeight(node.left),
					getHeight(node.right));
	}

	
	public int getDepth() {
		return getDepth(this);
	}

	protected static  int getDepth(BSTNode node) {
		if (node == null)
			return -1;
		else
			return 1 + getDepth(node.parent);
	}
	
	public void showPreorder() {
		showPreorder(this);
	}
	
	public void showInorder() {
		showInorder(this);
	}
	
	public void showPostorder() {
		showPostorder(this);
	}
	
	
	//complete this code
	protected static  void showPreorder(BSTNode node) {
		
	}
	//complete this code

	protected static  void showInorder(BSTNode node) {
		
	}
	
	protected static  void showPostorder(BSTNode node) {
		
	}
	
	//Métodos para VisualEDA:
	@Override
	public IBSTNode getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public IBSTNode getLeft() {
		// TODO Auto-generated method stub
		return left;
	}

	@Override
	public IBSTNode getRight() {
		// TODO Auto-generated method stub
		return right;
	}

	@Override
	public Object getElem() {
		// TODO Auto-generated method stub
		return elem;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
