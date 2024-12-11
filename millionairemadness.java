import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class millionairemadness {
    static ArrayList<ArrayList<IntegerPair>> AL = new ArrayList<>();
    static ArrayList<Boolean> taken = new ArrayList<>();
    static PriorityQueue<IntegerPair> pq = new PriorityQueue<>();

    static void process(int u) {
        taken.set(u, true);
        for (IntegerPair v_w : AL.get(u)) 
            if (!taken.get(v_w.first()))
                pq.offer(new IntegerPair(v_w.second(), v_w.first()));
    }

    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int M = reader.nextInt();                   // length of the vault
        int N = reader.nextInt();                   // width of the vault
        int[][] vault = new int[M][N];
        for (int i=0; i<M; ++i) 
            for (int j=0; j<N; ++j) 
                vault[i][j] = reader.nextInt();

        for (int i=0; i<M*N; ++i) AL.add(new ArrayList<>());
        int[][] moves = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int i=0; i<M; ++i) {
            for (int j=0; j<N; ++j) {
                int current_value = vault[i][j];
                int current_posit = i*N+j;
                for (int[] move : moves) {
                    int new_i = i+move[0];
                    int new_j = j+move[1];
                    if (new_i>=0 && new_i<M && new_j>=0 && new_j<N) {
                        int new_value = vault[new_i][new_j];
                        int new_posit = new_i*N+new_j;
                        int diff = Math.max(0, new_value-current_value);
                        AL.get(current_posit).add(new IntegerPair(new_posit, diff));
                    }
                }
            }
        }
        // Modified Prim's Algorithm
        int ladder = 0;
        for (int i=0; i<M*N; ++i) taken.add(false);
        process(0);                                 // Process from index 0, the source
        while (!pq.isEmpty()) {
            IntegerPair front = pq.poll();
            int u = front.second(), w = front.first();
            if (taken.get(u)) continue;
            ladder = Math.max(ladder, w);
            process(u);
            if (u == M*N-1) break;
        }
        pw.println(ladder);
        pw.flush();
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