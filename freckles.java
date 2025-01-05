import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class freckles {
    static ArrayList<ArrayList<IntegerDoublePair>> AL = new ArrayList<>();
    static ArrayList<Boolean> taken = new ArrayList<>();
    static PriorityQueue<DoubleIntegerPair> pq = new PriorityQueue<>();

    static void process(int u) {
        taken.set(u, true);
        for (IntegerDoublePair v_w : AL.get(u))
            if (!taken.get(v_w.first()))
                pq.offer(new DoubleIntegerPair(v_w.second(), v_w.first()));
    }

    static double calcEuclideanDist(DoublePair f, DoublePair s) {
        double x1 = f.first(), y1 = f.second();
        double x2 = s.first(), y2 = s.second();
        return Math.sqrt(Math.pow(y1-y2, 2)+Math.pow(x1-x2,2));
    }

    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int TC = reader.nextInt();
        while (TC-- > 0) {
            int n = reader.nextInt();                   // number of freckles

            AL.clear(); taken.clear(); pq.clear();
            ArrayList<DoublePair> freckles = new ArrayList<>(n);
            for (int i=0; i<n; ++i) {
                double x = reader.nextDouble();
                double y = reader.nextDouble();
                freckles.add(new DoublePair(x,y));
            }

            for (int i=0; i<n; ++i) AL.add(new ArrayList<>());

            for (int i=0; i<n-1; ++i) {
                for (int j=i+1; j<n; ++j) {
                    double distance = calcEuclideanDist(freckles.get(i), freckles.get(j));
                    AL.get(i).add(new IntegerDoublePair(j, distance));
                    AL.get(j).add(new IntegerDoublePair(i, distance));
                }
            }

            double mst_cost = 0;
            int num_taken = 0;
            for (int i=0; i<n; ++i) taken.add(false);
            process(0);
            while (!pq.isEmpty()) {
                DoubleIntegerPair front = pq.poll();
                int u = front.second();
                double w = front.first();
                if (taken.get(u)) continue;
                mst_cost += w;
                process(u);
                ++num_taken;
                if (num_taken == n-1) break;
            }
            pw.printf("%.2f", mst_cost);
            pw.println();
        }
        pw.flush();
    }

    static class DoubleIntegerPair implements Comparable<DoubleIntegerPair> {
        Double _first; Integer _second;
      
        public DoubleIntegerPair(Double f, Integer s) {
          _first = f;
          _second = s;
        }
      
        public int compareTo(DoubleIntegerPair o) {
          if (!this.first().equals(o.first()))
            return Double.compare(this.first(), o.first());
          else
            return Integer.compare(this.second(), o.second());
        }
        Double first() { return _first; }
        Integer second() { return _second; }
    }

    static class IntegerDoublePair implements Comparable<IntegerDoublePair> {
        Integer _first; Double _second;
      
        public IntegerDoublePair(Integer f, Double s) {
          _first = f;
          _second = s;
        }
      
        public int compareTo(IntegerDoublePair o) {
          if (!this.first().equals(o.first()))
            return Integer.compare(this.first(), o.first());
          else
            return Double.compare(this.second(), o.second());
        }
        Integer first() { return _first; }
        Double second() { return _second; }
    }
    
    static class DoublePair implements Comparable<DoublePair> {
        Double _first, _second;
      
        public DoublePair(Double f, Double s) {
          _first = f;
          _second = s;
        }
      
        public int compareTo(DoublePair o) {
          if (!this.first().equals(o.first()))
            return Double.compare(this.first(), o.first());
          else
            return Double.compare(this.second(), o.second());
        }
        Double first() { return _first; }
        Double second() { return _second; }
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
