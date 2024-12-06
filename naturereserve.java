import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class naturereserve {
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

        int TC = reader.nextInt();                      // number of datasets
        while (TC-- > 0) {
            pq.clear(); taken.clear(); AL.clear();      // reset variables
            
            int N = reader.nextInt();                   // number of environmenal monitoring stations
            int M = reader.nextInt();                   // number of two-way communication channels
            int L = reader.nextInt();                   // size of the Smart Data Analysis program (in bytes)
            int S = reader.nextInt();                   // number of initial stations

            int[] initial_stations = new int[S];        // represents the initial S stations
            for (int i=0; i<S; ++i) initial_stations[i] = reader.nextInt()-1;
            for (int i=0; i<N; ++i) AL.add(new ArrayList<>());

            for (int m=0; m<M; ++m) {
                int i = reader.nextInt()-1;             
                int j = reader.nextInt()-1;             
                int E = reader.nextInt()+L;
                AL.get(i).add(new IntegerPair(j, E));
                AL.get(j).add(new IntegerPair(i, E));
            }

            long mst_cost = 0, num_taken = 0;
            for (int i=0; i<N; ++i) taken.add(false);
            for (int i=0; i<S; ++i) {
                num_taken++;
                process(initial_stations[i]);
            }
            while (!pq.isEmpty()) {
                IntegerPair front = pq.poll();
                int u = front.second(), w = front.first();
                if (taken.get(u)) continue;
                mst_cost += w;
                process(u);
                ++num_taken;
                if (num_taken == N) break;
            }
            pw.println(mst_cost);
        }
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
            din = new DataInputStream(
                new FileInputStream(file_name));
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
                buf[cnt++] = (byte)c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
            if (neg)
                return -ret;
            return ret;
        }

        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();

            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0,
                                 BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null)
                return;
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