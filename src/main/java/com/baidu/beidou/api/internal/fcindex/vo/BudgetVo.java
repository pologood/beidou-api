package com.baidu.beidou.api.internal.fcindex.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.baidu.beidou.api.internal.fcindex.service.OneServiceConst;

public class BudgetVo extends BaseVo {

    private static final long serialVersionUID = -6270327809017361145L;

    public BudgetVo() {
        super();
    }

    public BudgetVo(int code, String err) {
        super(code, err);
    }

    Map<String, Budget> result = Collections.emptyMap();

    public Map<String, Budget> getResult() {
        return result;
    }

    public void setResult(Map<String, Budget> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Budget: ");
        sb.append("{code: ").append(this.code);
        sb.append(", errmsg: ").append(this.errmsg);
        sb.append(", budget: ").append(this.result.toString());
        sb.append("}");
        return sb.toString();
    }

    public static class Budget implements Serializable {

        private static final long serialVersionUID = -4447705707823434797L;
        // YUAN in Beidou
        private int budget = 0;

        private int type = OneServiceConst.PromoteType.PRO;

        public Budget() {
        }

        public Budget(int budget) {
            this(budget, OneServiceConst.PromoteType.PRO);
        }

        public Budget(int budget, int type) {
            this.budget = budget;
            this.type = type;
        }

        public double getBudget() {
            return budget;
        }

        public void setBudget(int budget) {
            this.budget = budget;
        }

        public int getType() {
            return type;
        }

        public void setType(int tpye) {
            this.type = tpye;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{budget:");
            sb.append(this.budget);
            sb.append(", type:");
            sb.append(this.type);
            sb.append("}");
            return sb.toString();
        }
    }

}
