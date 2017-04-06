package nearestNeigh;

public class KDNode {
	
	private KDNode parent;
	private Point value;
	private KDNode left;
	private KDNode right;
	
	KDNode(Point point, KDNode parent) {
		this.parent = parent;
		this.value = point;
		this.left = null;
		this.right = null;
	}
	
	public KDNode getParent() {
		return parent;
	}
	
	public void setParent(KDNode parent) {
		this.parent = parent;
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
