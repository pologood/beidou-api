package com.baidu.beidou.api.internal.fcindex.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class BatchStatVo extends BaseVo {

    private static final long serialVersionUID = 3369846444561976829L;

    public BatchStatVo() {
        super();
    }
    
    public BatchStatVo(int code, String err) {
        super(code, err);
    }

    List<BatchStat> result = Collections.emptyList();

    public List<BatchStat> getResult() {
        return result;
    }

    public void setResult(List<BatchStat> result) {
        this.result = result;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stat: ");
        sb.append("{code: ").append(this.code);
        sb.append(", errmsg: ").append(this.errmsg);
        sb.append(", stat: ").append(result.toString());
        sb.append("}");
        return sb.toString();
    }

    public static class BatchStat implements Serializable {

        private static final long serialVersionUID = 8432333237738464732L;

        private long userid = 0;

        private long shows = 0;

        private long clks = 0;

        private double paysum = 0;

        public BatchStat() {
        }

        public BatchStat(long userid, long shows, long clks, double paysum) {
            this.userid = userid;
            this.shows = shows;
            this.clks = clks;
            this.paysum = paysum;
        }

        public long getUserid() {
            return userid;
        }

        public void setUserid(long userid) {
            this.userid = userid;
        }

        public long getShows() {
            return shows;
        }

        public void setShows(long shows) {
            this.shows = shows;
        }

        public long getClks() {
            return clks;
        }

        public void setClks(long clks) {
            this.clks = clks;
        }

        public double getPaysum() {
            return paysum;
        }

        public void setPaysum(double paysum) {
            this.paysum = paysum;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{userid: ").append(this.userid);
            sb.append(", srchs: ").append(this.shows);
            sb.append(", clks: ").append(this.clks);
            sb.append(", cost: ").append(this.paysum);
            sb.append("}");
            return sb.toString();
        }
    }

}
