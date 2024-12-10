import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class sverigekartan {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int R = Integer.parseInt(br.readLine());                    // number of rows
        int C = Integer.parseInt(br.readLine());                    // number of columns
        int U = Integer.parseInt(br.readLine());                    // number of changes from water to land
        
        UnionFind ufds = new UnionFind(R*C);
        char[][] map = new char[R][C];
        for (int i=0; i<R; ++i) {
            char[] c = br.readLine().toCharArray();
            for (int j=0; j<C; ++j) 
                map[i][j] = c[j];
        }

        int[][] m = {{1,0},{-1,0},{0,1},{0,-1}};
        int S = 0;
        for (int i=0; i<R; ++i) {
            for (int j=0; j<C; ++j) {
                if (map[i][j]=='.') continue;
                int cur_pos = i*C+j;
                if (map[i][j]=='S' && S==0) S=cur_pos;
                for (int[] move : m) {
                    int new_i = i+move[0];
                    int new_j = j+move[1];
                    if (new_i >= 0 && new_i < R && new_j >= 0 && new_j < C) {
                        if (map[new_i][new_j] != '.') {
                            int new_pos = new_i*C+new_j;
                            ufds.unionSet(cur_pos, new_pos);
                        }
                    }
                }
            }
        }
        pw.println(ufds.sizeOfSet(S));
        for (int u=0; u<U; ++u) {
            String[] input = br.readLine().split(" ");
            int i = Integer.parseInt(input[0])-1;
            int j = Integer.parseInt(input[1])-1;
            map[i][j] = '#';
            int pos = i*C+j;
            for (int[] move : m) {
                int new_i = i+move[0];
                int new_j = j+move[1];
                if (new_i >= 0 && new_i < R && new_j >= 0 && new_j < C) {
                    if (map[new_i][new_j] != '.') {
                        int new_pos = new_i*C+new_j;
                        ufds.unionSet(pos, new_pos);
                    }
                }
            }
            pw.println(ufds.sizeOfSet(S));
        }
        pw.flush();
    }
}

class UnionFind {                                             
    private ArrayList<Integer> p, rank, setSize;
    private int numSets;
  
    public UnionFind(int N) {
      p = new ArrayList<>(N);
      rank = new ArrayList<>(N);
      setSize = new ArrayList<>(N);
      numSets = N;
      for (int i = 0; i < N; i++) {
        p.add(i);
        rank.add(0);
        setSize.add(1);
      }
    }
  
    public int findSet(int i) { 
      if (p.get(i) == i) return i;
      else {
        int ret = findSet(p.get(i)); p.set(i, ret);
        return ret; } }
  
    public Boolean isSameSet(int i, int j) { return findSet(i) == findSet(j); }
  
    public void unionSet(int i, int j) { 
      if (!isSameSet(i, j)) { numSets--; 
      int x = findSet(i), y = findSet(j);
      if (rank.get(x) > rank.get(y)) { p.set(y, x); setSize.set(x, setSize.get(x) + setSize.get(y)); }
      else                           { p.set(x, y); setSize.set(y, setSize.get(y) + setSize.get(x));
                                       if (rank.get(x) == rank.get(y)) rank.set(y, rank.get(y) + 1); } } }
    public int numDisjointSets() { return numSets; }
    public int sizeOfSet(int i) { return setSize.get(findSet(i)); }
  }
