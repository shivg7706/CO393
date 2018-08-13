import java.util.Scanner;

class Dijkstra {

    private int node, edge, source;
    private int [][] adjacencyMatrix;
    private int [] distance;
    private int [] path;
    private boolean [] visited;

    public Dijkstra() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the no. of nodes: ");
        node = in.nextInt();
        System.out.print("Enter the no. of Edges: ");
        edge = in.nextInt();
        System.out.print("Enter the source ");
        source = in.nextInt();

        adjacencyMatrix = new int [node][node];
        distance = new int [node];
        path = new int [node];
        visited = new boolean [node];

        for(int i = 1; i < node; i++){
            distance[i] = Integer.MAX_VALUE;
            visited[i] = false;
            
        }
        for(int i = 0; i  < node; i++) {
            path[i] = -1;
        }
        
        in.nextLine();
        char ch = 'U';
        System.out.print("Is graph undirected or directed (U/D): ");
        ch = in.nextLine().charAt(0);
        System.out.println("Enter the details of each edges <Start Node> <End Node> <Weight>");
        for(int i = 0; i < edge; i++) {
            int start, end, weight;
            //System.out.printf("-> ");
            String [] temp = in.nextLine().split(" ");
            start = Integer.parseInt(temp[0]) - 1;
            end = Integer.parseInt(temp[1]) - 1;
            weight = Integer.parseInt(temp[2]);
            adjacencyMatrix[start][end] = weight;
            if(ch == 'U'){    
                adjacencyMatrix[end][start] = weight;
            }
        }
    }

    private int getMinimumDistanceIndex() {
        int temp = -1;
        for(int i = 0; i < node; i++) {
            if((temp == -1 || distance[i] < distance[temp]) && !visited[i]) {
                temp = i;
            }
        }
        return temp;
    }
    /*
    public void recur(int k) {
        System.out.println(k+ " " + path[k]);
        if (path[k] == -1)
            return;
 
        recur(path[k]);
 
        System.out.printf("%d ", k);
    }
    */
    public void solve() {
        distance[source] = 0;

        for(int i = 0; i < node - 1; i++) {
            int min = getMinimumDistanceIndex();
            visited[min] = true;
            for(int j = 0; j < node; j++) {
                if(adjacencyMatrix[min][j] != 0 && !visited[j] && distance[min] != Integer.MAX_VALUE) {
                    int dist = adjacencyMatrix[min][j] + distance[min];
                    if(dist < distance[j]) {
                        distance[j] = dist;
                        path[j] = min;
                    }
                }
            }
        }
    }

    public void show(){
        for(int i = 0; i < node; i++){
            if(distance[i] != Integer.MAX_VALUE)
                System.out.printf("Vertex- %d Distance- %d\n", i+1, distance[i]);
            else
                System.out.printf("Vertex- %d No available path\n", i+1);
        }
    }
}


public class Main {
    public static void main(String[] args) {

        Dijkstra dijkstra = new Dijkstra();
        dijkstra.solve();
        dijkstra.show();
    }   
}