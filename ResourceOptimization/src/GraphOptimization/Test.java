package GraphOptimization;

public class Test {

	public static void main(String[] args) {
		String path = "C:...\\adjacency.csv";
		int[][] matrix = Util.getAdjacencyMatrixFromCSV(path, true);

		Graph mstGraph = Util.getMSTFromAdjacencyMatrix(matrix, "Kruskal");

		int[][] mstMatrix = Util.getMatrixFromGraph(mstGraph);
		/*System.out.println(matrix[0][0] + " " +matrix[0][1] + " " +matrix[0][2] + " " +matrix[0][3] + " " );
		System.out.println(matrix[1][0] + " " +matrix[1][1] + " " +matrix[1][2] + " " +matrix[1][3] + " " );
		System.out.println(matrix[2][0] + " " +matrix[2][1] + " " +matrix[2][2] + " " +matrix[2][3] + " " );
		System.out.println(matrix[3][0] + " " +matrix[3][1] + " " +matrix[3][2] + " " +matrix[3][3] + " " );*/
		Util.exportMatrixToCSV(mstMatrix, "C:...\\adjacencyExported.csv");
		

	}

}
