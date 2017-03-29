package nearestNeigh;

public class KDNode {
	
	private Point value;
	private KDNode left;
	private KDNode right;
	
	KDNode(Point point) {
		
		this.value = point;
		this.left = null;
		this.right = null;
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
