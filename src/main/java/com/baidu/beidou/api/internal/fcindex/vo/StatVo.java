package com.baidu.beidou.api.internal.fcindex.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StatVo extends BaseVo {

    private static final long serialVersionUID = 5192756245804762660L;

    public StatVo() {
        super();
    }

    public StatVo(int code, String err) {
        super(code, err);
    }

    Map<String, List<Stat>> result = Collections.emptyMap();

    public Map<String, List<Stat>> getResult() {
        return result;
    }

    public void setResult(Map<String, List<Stat>> result) {
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

    public static class Stat implements Serializable {

        private static final long serialVersionUID = -7727027512084802257L;

        private String date = "";

        private long shows = 0;

        private long clks = 0;

        private double paysum = 0;

        public Stat() {
        }

        public Stat(String date, long shows, long clks, double paysum) {
            this.date = date;
            this.shows = shows;
            this.clks = clks;
            this.paysum = paysum;
        }

        public Stat(long shows, long clks, double paysum) {
            this("", shows, clks, paysum);
        }

        public Stat(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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
            sb.append("{date: ").append(this.date);
            sb.append(", srchs: ").append(this.shows);
            sb.append(", clks: ").append(this.clks);
            sb.append(", cost: ").append(this.paysum);
            sb.append("}");
            return sb.toString();
        }

    }

}
