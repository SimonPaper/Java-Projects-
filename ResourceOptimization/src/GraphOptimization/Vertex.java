package GraphOptimization;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

	String label;

	public Vertex() {
	}

	public Vertex(String label) {
		this.label = label;

	}

	public Vertex predecessor;
	public List<Edge> edges = new ArrayList<Edge>();
}
