package arboles;

public class BSTree{
	BSTNode root;
	public void insert(int key, String elem) {
		if(root==null){
			BSTNode nuevo = new BSTNode(elem, key);
			root = nuevo;
			return;
		}
		insert(key, elem, root);
		
		
		
	}
	
	
	
	public void insert(int key, String elem, BSTNode nodo){
		if(key==nodo.key){
			System.out.println("Clave ya existe");
			return;
		}
		
		if(key<nodo.key){
			if(nodo.left!=null){
			  insert(key, elem, nodo.left);
			}else{
				BSTNode nuevo = new BSTNode(elem, key);
				nodo.left = nuevo;
				nuevo.parent = nodo;
				return;
			}
		}
		else{
			if(nodo.right!=null){
				  insert(key, elem, nodo.right);
				}else{
					BSTNode nuevo = new BSTNode(elem, key);
					nodo.right = nuevo;
					nuevo.parent = nodo;
					return;
				}
		}
	}
	
	public void remove(int key) {
		if(root==null){
			 return;
		}
		 removeR(root, key);
	}
	
	public void removeR(BSTNode nodo, int key){
		if(nodo==null){
			System.out.println("Nodo no encontrado");
		   return;
		}
		if(key==nodo.key){
			if (nodo.left==null&&nodo.right==null){
				if (key<nodo.parent.key){
					nodo.parent.left=null;
					return;
				}else{
					nodo.parent.right=null;
					return;
				}
			}
			if (nodo.left==null||nodo.right==null){
				if (nodo.left==null){
					nodo.parent.left=nodo.right;
					nodo.right.parent=nodo.parent;
				}else{
					nodo.parent.right=nodo.left;
					nodo.left.parent=nodo.parent;
				}
			return;	
			}else{
				BSTNode sucesor=findMin(nodo.right);
				nodo.elem=sucesor.elem;
				nodo.key=sucesor.key;
				removeR(sucesor,sucesor.key);
			}
			
			
		}else if(key<nodo.key){
			 removeR(nodo.left, key);
		}else{
			 removeR(nodo.right, key);
		}
	}
	
	public BSTNode findMin(BSTNode nodo){
		if (nodo.left==null){
			return nodo;
		}else{
			return findMin(nodo.left);
		}
		
	}


	public Object find(int key) {
		if(root==null){
			return null;
		}BSTNode aux = root; 
		while(aux!=null){
			if(key==aux.key){
				return aux.elem.toString();
			}else if(key<aux.key){
				aux=aux.left;
			}else
				aux=aux.right;
			
		}
		
		
		return null;
	}
	
	public Object findR(int key) {
		if(root==null){
			return null;
		}
		return findR(root, key);
	}
	
	public Object findR(BSTNode nodo, int key){
		if(nodo==null){
		  return null;
		}
		if(key==nodo.key){
			return nodo.elem.toString();
		}else if(key<nodo.key){
			return findR(nodo.left, key);
		}else{
			return findR(nodo.right, key);
		}
	}
	

	public IBSTNode getRoot() {
		// TODO Auto-generated method stub
		return root;
	}

	
	public static void main(String[] args){
		
		BSTree arbol = new BSTree();
		//VisualEDA v = new VisualEDA();
		
		//v.draw(arbol);
		arbol.insert(5, "5");
		//v.draw(arbol);
		arbol.insert(3, "3");
		//v.draw(arbol);
		arbol.insert(8, "8");
		//v.draw(arbol);
		arbol.insert(6, "6");
		//v.draw(arbol);
		arbol.insert(7, "7");
		//v.draw(arbol);
		arbol.insert(2, "2");
		//v.draw(arbol);
		//System.out.println(arbol.findR(6));
		arbol.remove(5);
		//v.draw(arbol);

		
	}
	
	
}
