import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class shortestpath3 {
    public static final int INF = 1_000_000_000;
    private static final int UNVISITED = 0; 
    private static final int VISITED = 1;

    private static ArrayList<ArrayList<IntegerPair>> AL;
    private static ArrayList<Integer> dfs_num;

    private static void dfs(int u) {
        dfs_num.set(u, VISITED);
        for (IntegerPair v_w : AL.get(u)) {
            if (dfs_num.get(v_w.first()) == UNVISITED) {
                dfs(v_w.first());
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        while (true) {
            int n = reader.nextInt();                                   // number of nodes in the graph
            int m = reader.nextInt();                                   // number of edges 
            int q = reader.nextInt();                                   // number of queries
            int s = reader.nextInt();                                   // index of starting node
            if (n==0 && m==0 && q==0 && s==0) break;

            AL = new ArrayList<>();
            for (int i=0; i<n; ++i) AL.add(new ArrayList<>());         
            for (int i=0; i<m; ++i) {
                int u = reader.nextInt(), v = reader.nextInt();         
                int w = reader.nextInt();                               // edge weight
                AL.get(u).add(new IntegerPair(v,w));
            }

            // Bellman Ford's Algorithm routine, relax all m edges (n-1) times
            ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(n,INF));
            dist.set(s,0);
            for (int i=0; i<n-1; ++i) {                                 // O(n*m)
                Boolean modified = false;                               // optimization
                for (int u=0; u<n; ++u) {
                    if (dist.get(u) != INF) {
                        for (IntegerPair v_w : AL.get(u)) {
                            int v = v_w.first(), w = v_w.second();
                            if (dist.get(u)+w >= dist.get(v)) continue;
                            dist.set(v, dist.get(u)+w);
                            modified = true;
                        }
                    }  
                }
                if (!modified) break;
            }
            ArrayList<Integer> og_dist = new ArrayList<>();
            for (int i=0; i<n; ++i) og_dist.add(dist.get(i));

            // One more pass to check for negative cycles
            for (int u=0; u<n; ++u) {
                if (dist.get(u) != INF) {
                    for (IntegerPair v_w : AL.get(u)) {
                        int v = v_w.first(), w = v_w.second();
                        if (dist.get(u)+w >= dist.get(v)) continue;
                        dist.set(v, dist.get(u)+w);
                    }
                }
            }
            // Perform DFS on nodes in negative cycles
            dfs_num = new ArrayList<>(Collections.nCopies(n, UNVISITED));
            for (int i=0; i<n; ++i) {
                if (dist.get(i) != og_dist.get(i)) {
                    dfs(i);
                }
            }
            for (int i=0; i<q; ++i) {
                int node = reader.nextInt();
                if (dfs_num.get(node) == VISITED) {
                    pw.println("-Infinity");
                } else if (dist.get(node) == INF) {
                    pw.println("IMPOSSIBLE");
                } else {
                    pw.println(dist.get(node));
                }
            }
            pw.println();
        }
        pw.flush();
    }

    static class IntegerPair implements Comparable<IntegerPair> {
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

    static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
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
