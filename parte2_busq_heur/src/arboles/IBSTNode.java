package arboles;

public interface IBSTNode {

	/*number of nodes in its subtree*/
	public int getSize();
	/*length of the longest way to some of its subtree's leaves plus one*/
	public int getHeight();
	/*length of the way from to the root*/
	public int getDepth();
	
	/*shows the preorder traverse of its subtree*/
	public void showPreorder();
	/*shows the inorder traverse of its subtree*/
	public void showInorder();
	/*shows the postorder traverse of its subtree*/
	public void showPostorder();
	
	public IBSTNode getParent();//Método sólo para VisualEDA
	
	public IBSTNode getLeft();//Método sólo para VisualEDA
	
	public IBSTNode getRight();//Método sólo para VisualEDA
	
	public Object getElem();//Método sólo para VisualEDA
	
	public String getID();//Método sólo para VisualEDA
	
	
	
	
}
