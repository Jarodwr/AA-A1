package nearestNeigh;

public class KDNode {
	
	private Point value;
	private KDNode parent;
	private KDNode left;
	private KDNode right;
	
	KDNode(Point point, KDNode parent) {
		
		this.value = point;
		this.left = null;
		this.right = null;
	}
	
	public KDNode getParent() {
		
		return parent;
	}
	
	public Point getValue() {
		
		return value;
	}
	
	public KDNode getLeft() {
		
		return left;
	}
	
	public KDNode getRight() {
		
		return right;
	}
	
	public void setLeft(KDNode newLeft) {
		
		this.left = newLeft;
	}
	
	public void setRight(KDNode newRight) {
		
		this.right = newRight;
	}
	
}
