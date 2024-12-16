import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class destinationunknown {
    public static final int INF = 1_000_000_000;
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int TC = reader.nextInt();
        while (TC-- > 0) {
            int n = reader.nextInt();                                       // number of intersections in the city
            int m = reader.nextInt();                                       // number of roads between intersections
            int t = reader.nextInt();                                       // number of possible destinations
            int s = reader.nextInt()-1;                                     // source intersection
            int g = reader.nextInt()-1, h = reader.nextInt()-1;             // two intersections the duo has travelled

            ArrayList<ArrayList<IntegerPair>> AL = new ArrayList<>();
            for (int i=0; i<n; ++i) AL.add(new ArrayList<>());
            for (int i=0; i<m; ++i) {
                int a = reader.nextInt()-1, b = reader.nextInt()-1, d = reader.nextInt();
                AL.get(a).add(new IntegerPair(b, d));
                AL.get(b).add(new IntegerPair(a, d));
            }

            ArrayList<Integer> destinations = new ArrayList<>();
            for (int i=0; i<t; ++i) {
                int x = reader.nextInt()-1;                                   // possible destination
                ArrayList<Integer> dist_s = modifiedDijsktra(AL, n, s);
                int dist_sx = dist_s.get(x), dist_sg = dist_s.get(g), dist_sh = dist_s.get(h);
                ArrayList<Integer> dist_g = modifiedDijsktra(AL, n, g);
                int dist_gh = dist_g.get(h), dist_gx = dist_g.get(x);
                int dist_hx = modifiedDijsktra(AL, n, h).get(x);
                if (dist_sx == dist_sg+dist_gh+dist_hx || dist_sx == dist_sh+dist_gh+dist_gx)
                    destinations.add(x+1);
            }

            Collections.sort(destinations);
            for (int i=0; i<destinations.size(); ++i) pw.print(destinations.get(i)+" ");
            pw.println();
        }
        pw.flush();
    }

    static ArrayList<Integer> modifiedDijsktra(ArrayList<ArrayList<IntegerPair>> AL, int n, int s) {
        ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(n, INF));
        dist.set(s, 0);
        PriorityQueue<IntegerPair> pq = new PriorityQueue<>();
        pq.offer(new IntegerPair(0, s));

        while (!pq.isEmpty()) {
            IntegerPair top = pq.poll();
            int d = top.first(), u = top.second();
            if (d > dist.get(u)) continue;
            for (IntegerPair v_w : AL.get(u)) {
                int v = v_w.first(), w = v_w.second();
                if (dist.get(u)+w >= dist.get(v)) continue;
                dist.set(v, dist.get(u)+w);
                pq.offer(new IntegerPair(dist.get(v), v));
            }
        }
        return dist;
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