import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class muddyhike {
    private static final int UNVISITED = 0;
    private static final int VISITED = 1;
    private static ArrayList<ArrayList<IntegerPair>> AL;
    private static ArrayList<Integer> dfs_num;
    private static HashSet<Integer> destSet = new HashSet<>();
    private static ArrayList<Integer> entranceSet = new ArrayList<>();
    private static int best = Integer.MAX_VALUE;

    private static void dfs(int u, int currentDepth) {
        dfs_num.set(u, VISITED);
        if (currentDepth >= best) return;
        if (destSet.contains(u)) {
            best = Math.min(best, currentDepth);
            return;
        }
        for (IntegerPair v_w : AL.get(u)) {
            if (dfs_num.get(v_w.first()) == UNVISITED) {
                dfs(v_w.first(), Math.max(currentDepth, v_w.second()));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int r = reader.nextInt();
        int c = reader.nextInt();
        int[][] grid = new int[r][c];
        for (int i=0; i<r; ++i) {
            for (int j=0; j<c; ++j) {
                grid[i][j] = reader.nextInt();
                if (j==c-1) destSet.add(i*c+j);
                if (j==0) entranceSet.add(i*c+j);
            }
        }

        // Kruskal's Algorithm
        ArrayList<IntegerTriple> EL = new ArrayList<>();
        int[][] moves = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int i=0; i<r; ++i) {
            for (int j=0; j<c; ++j) {
                for (int[] move : moves) {
                    int new_i = i+move[0], new_j = j+move[1];
                    if (new_i>=0 && new_i<r && new_j>=0 && new_j<c) {
                        int depth = Math.max(grid[i][j], grid[new_i][new_j]);
                        int current_posit = i*c+j;
                        int new_posit = new_i*c+new_j;
                        EL.add(new IntegerTriple(depth, current_posit, new_posit));
                    }
                }
            }
        }
        Collections.sort(EL);           // sort by depth

        AL = new ArrayList<>();
        for (int i=0; i<r*c; ++i) AL.add(new ArrayList<>());
        int num_taken = 0;
        UnionFind UFDS = new UnionFind(r*c);
        for (int i=0; i<EL.size(); ++i) {
            IntegerTriple front = EL.get(i);
            if (UFDS.isSameSet(front.second(), front.third())) continue;
            UFDS.unionSet(front.second(), front.third());
            AL.get(front.second()).add(new IntegerPair(front.third(), front.first()));
            AL.get(front.third()).add(new IntegerPair(front.second(), front.first()));
            ++num_taken;
            if (num_taken == r*c-1) break;
        }

        for (int i=0; i<entranceSet.size(); ++i) {
            dfs_num = new ArrayList<>(Collections.nCopies(r*c, UNVISITED));
            dfs(entranceSet.get(i), 0);
        }
        pw.println(best);
        pw.flush();
    }

    static class Reader {
        private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public Reader(String file_name) throws IOException {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n') {
                    if (cnt != 0) {
                        break;
                    }
                    else {
                        continue;
                    }
                }
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg) return -ret;
            return ret;
        }

        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
            if (neg) return -ret;
            return ret;
        }

        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();

            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg) return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null) return;
            din.close();
        }
    }
}

class UnionFind {                                             
    private ArrayList<Integer> p, rank, setSize;
    private int numSets;
  
    public UnionFind(int N) {
        p = new ArrayList<Integer>(N);
        rank = new ArrayList<Integer>(N);
        setSize = new ArrayList<Integer>(N);
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
            return ret;
        }
    }
  
    public Boolean isSameSet(int i, int j) { return findSet(i) == findSet(j); }
  
    public void unionSet(int i, int j) { 
        if (!isSameSet(i, j)) { 
            numSets--; 
            int x = findSet(i), y = findSet(j);
            if (rank.get(x) > rank.get(y)) { 
                p.set(y, x); 
                setSize.set(x, setSize.get(x) + setSize.get(y)); 
            } else { 
                p.set(x, y); 
                setSize.set(y, setSize.get(y) + setSize.get(x));
                if (rank.get(x) == rank.get(y)) 
                    rank.set(y, rank.get(y) + 1); 
            } 
        } 
    }

    public int numDisjointSets() { return numSets; }
    public int sizeOfSet(int i) { return setSize.get(findSet(i)); }
}

class IntegerTriple implements Comparable<IntegerTriple> {
    Integer _first, _second, _third;
  
    public IntegerTriple(Integer f, Integer s, Integer t) {
      _first = f;
      _second = s;
      _third = t;
    }
  
    public int compareTo(IntegerTriple o) {
      if (!this.first().equals(o.first()))
        return this.first() - o.first();
      else if (!this.second().equals(o.second()))
        return this.second() - o.second();
      else
        return this.third() - o.third();
    }
  
    Integer first() { return _first; }
    Integer second() { return _second; }
    Integer third() { return _third; }
  
    public String toString() { return first() + " " + second() + " " + third(); }
}

class IntegerPair implements Comparable<IntegerPair> {
    Integer _first, _second;
  
    public IntegerPair(Integer f, Integer s) {
        _first = f;
        _second = s;
    }
  
    public int compareTo(IntegerPair o) {
        if (!this.first().equals(o.first()))
            return this.first() - o.first();
        else
            return this.second() - o.second();
    }
  
    Integer first() { return _first; }
    Integer second() { return _second; }
}