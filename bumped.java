import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class bumped {
    public static final long INF = 1_000_000_000_000l;
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int n = reader.nextInt();           // number of cities
        int m = reader.nextInt();           // number of roads
        int f = reader.nextInt();           // number of flights
        int s = reader.nextInt();           // number of city in which Peter's trip starts
        int t = reader.nextInt();           // number of city Peter is trying to travel to

        ArrayList<ArrayList<LongPair>> AL = new ArrayList<>();
        for (int i=0; i<n; ++i) AL.add(new ArrayList<>());
        // roads, can be used in either directions
        for (int idx=0; idx<m; ++idx) {
            int i = reader.nextInt();
            int j = reader.nextInt();
            int c = reader.nextInt();
            AL.get(i).add(new LongPair(j, c, false));
            AL.get(j).add(new LongPair(i, c, false));
        }
        // flights, cannot be used in either directions
        for (int idx=0; idx<f; ++idx) {
            int u = reader.nextInt();
            int v = reader.nextInt();
            AL.get(u).add(new LongPair(v, 0, true));
        }
        
        // (Modified) Dijkstra's Algorithm
        ArrayList<Long> dist = new ArrayList<>(Collections.nCopies(n, INF));
        dist.set(s, 0l);
        PriorityQueue<LongPair> pq = new PriorityQueue<>();
        pq.offer(new LongPair(0l, s, false));

        while (!pq.isEmpty()) {
            LongPair top = pq.poll();
            long d = top.first(), u = top.second();
            if (u == t) break;
            boolean hasUsedFlight = top.getIsFlight();
            if (d > dist.get((int) u)) continue;
            for (LongPair v_w : AL.get((int) u)) {
                long v = v_w.first();
                long w = v_w.second();
                boolean flight = v_w.getIsFlight();
                if (hasUsedFlight && flight) continue;
                if (dist.get((int) u)+w >= dist.get((int) v)) continue;
                dist.set((int) v, dist.get((int) u)+w);
                pq.offer(new LongPair(dist.get((int)v), v, (flight||hasUsedFlight)));
            }
        }
        pw.println(dist.get(t));
        pw.flush();
    }

    static class LongPair implements Comparable<LongPair> {
        long _first, _second;
        boolean isFlight;
        public LongPair(long f, long s, boolean isFlight) {
            _first = f;
            _second = s;
            this.isFlight = isFlight;
        }   
        public int compareTo(LongPair o) {
            if (this.first() != o.first())
                return Long.compare(this.first(),o.first());
            else
                return Long.compare(this.second(), o.second());
        }
        long first() { return _first; }
        long second() { return _second; }
        boolean getIsFlight() { return isFlight; }
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
