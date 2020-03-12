package GraphOptimization;

public interface IGraph {
	
	
	public abstract Vertex addVertex(String label);
	
	public abstract Vertex getVertex(int ind);

	public abstract void removeVertex(Vertex v);

	public abstract Edge addEdge(Vertex v1, Vertex v2, int weight, String label); 
	
	public abstract Edge getEdge(Vertex v1, Vertex v2);
	
	public abstract void removeEdge(Vertex v1, Vertex v2);
	
}
