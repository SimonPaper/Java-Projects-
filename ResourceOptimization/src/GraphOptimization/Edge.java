package GraphOptimization;

public class Edge implements Comparable<Edge>{

	public String label;
	public Vertex[] vertices = new Vertex[2];
	public int weight;

	public Edge(Vertex v1, Vertex v2, int weight, String label) {
		if (v1 != null && v2 != null) {
			this.vertices[0] = v1;
			this.vertices[1] = v2;
			this.weight = weight;
			this.label = label;
		} else {
			try {
				throw new Exception("Error: trying to create and edge with one or more null vertices!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int compareTo(Edge comparesTo) {
		 int comparedWeight=((Edge)comparesTo).weight;
	        /* For Ascending order*/
	        return this.weight-comparedWeight;

	}

}
