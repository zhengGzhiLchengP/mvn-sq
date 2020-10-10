package com.feiqu.system.model.basicData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CpuExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public CpuExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvIsNull() {
            addCriterion("basic_pinlv is null");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvIsNotNull() {
            addCriterion("basic_pinlv is not null");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvEqualTo(Double value) {
            addCriterion("basic_pinlv =", value, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvNotEqualTo(Double value) {
            addCriterion("basic_pinlv <>", value, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvGreaterThan(Double value) {
            addCriterion("basic_pinlv >", value, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvGreaterThanOrEqualTo(Double value) {
            addCriterion("basic_pinlv >=", value, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvLessThan(Double value) {
            addCriterion("basic_pinlv <", value, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvLessThanOrEqualTo(Double value) {
            addCriterion("basic_pinlv <=", value, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvIn(List<Double> values) {
            addCriterion("basic_pinlv in", values, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvNotIn(List<Double> values) {
            addCriterion("basic_pinlv not in", values, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvBetween(Double value1, Double value2) {
            addCriterion("basic_pinlv between", value1, value2, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andBasicPinlvNotBetween(Double value1, Double value2) {
            addCriterion("basic_pinlv not between", value1, value2, "basicPinlv");
            return (Criteria) this;
        }

        public Criteria andCoreNumIsNull() {
            addCriterion("core_num is null");
            return (Criteria) this;
        }

        public Criteria andCoreNumIsNotNull() {
            addCriterion("core_num is not null");
            return (Criteria) this;
        }

        public Criteria andCoreNumEqualTo(Integer value) {
            addCriterion("core_num =", value, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumNotEqualTo(Integer value) {
            addCriterion("core_num <>", value, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumGreaterThan(Integer value) {
            addCriterion("core_num >", value, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("core_num >=", value, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumLessThan(Integer value) {
            addCriterion("core_num <", value, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumLessThanOrEqualTo(Integer value) {
            addCriterion("core_num <=", value, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumIn(List<Integer> values) {
            addCriterion("core_num in", values, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumNotIn(List<Integer> values) {
            addCriterion("core_num not in", values, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumBetween(Integer value1, Integer value2) {
            addCriterion("core_num between", value1, value2, "coreNum");
            return (Criteria) this;
        }

        public Criteria andCoreNumNotBetween(Integer value1, Integer value2) {
            addCriterion("core_num not between", value1, value2, "coreNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumIsNull() {
            addCriterion("thread_num is null");
            return (Criteria) this;
        }

        public Criteria andThreadNumIsNotNull() {
            addCriterion("thread_num is not null");
            return (Criteria) this;
        }

        public Criteria andThreadNumEqualTo(Integer value) {
            addCriterion("thread_num =", value, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumNotEqualTo(Integer value) {
            addCriterion("thread_num <>", value, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumGreaterThan(Integer value) {
            addCriterion("thread_num >", value, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("thread_num >=", value, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumLessThan(Integer value) {
            addCriterion("thread_num <", value, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumLessThanOrEqualTo(Integer value) {
            addCriterion("thread_num <=", value, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumIn(List<Integer> values) {
            addCriterion("thread_num in", values, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumNotIn(List<Integer> values) {
            addCriterion("thread_num not in", values, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumBetween(Integer value1, Integer value2) {
            addCriterion("thread_num between", value1, value2, "threadNum");
            return (Criteria) this;
        }

        public Criteria andThreadNumNotBetween(Integer value1, Integer value2) {
            addCriterion("thread_num not between", value1, value2, "threadNum");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreIsNull() {
            addCriterion("ludashi_score is null");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreIsNotNull() {
            addCriterion("ludashi_score is not null");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreEqualTo(Double value) {
            addCriterion("ludashi_score =", value, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreNotEqualTo(Double value) {
            addCriterion("ludashi_score <>", value, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreGreaterThan(Double value) {
            addCriterion("ludashi_score >", value, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreGreaterThanOrEqualTo(Double value) {
            addCriterion("ludashi_score >=", value, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreLessThan(Double value) {
            addCriterion("ludashi_score <", value, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreLessThanOrEqualTo(Double value) {
            addCriterion("ludashi_score <=", value, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreIn(List<Double> values) {
            addCriterion("ludashi_score in", values, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreNotIn(List<Double> values) {
            addCriterion("ludashi_score not in", values, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreBetween(Double value1, Double value2) {
            addCriterion("ludashi_score between", value1, value2, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andLudashiScoreNotBetween(Double value1, Double value2) {
            addCriterion("ludashi_score not between", value1, value2, "ludashiScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreIsNull() {
            addCriterion("sdmark_score is null");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreIsNotNull() {
            addCriterion("sdmark_score is not null");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreEqualTo(Double value) {
            addCriterion("sdmark_score =", value, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreNotEqualTo(Double value) {
            addCriterion("sdmark_score <>", value, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreGreaterThan(Double value) {
            addCriterion("sdmark_score >", value, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreGreaterThanOrEqualTo(Double value) {
            addCriterion("sdmark_score >=", value, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreLessThan(Double value) {
            addCriterion("sdmark_score <", value, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreLessThanOrEqualTo(Double value) {
            addCriterion("sdmark_score <=", value, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreIn(List<Double> values) {
            addCriterion("sdmark_score in", values, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreNotIn(List<Double> values) {
            addCriterion("sdmark_score not in", values, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreBetween(Double value1, Double value2) {
            addCriterion("sdmark_score between", value1, value2, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andSdmarkScoreNotBetween(Double value1, Double value2) {
            addCriterion("sdmark_score not between", value1, value2, "sdmarkScore");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("gmt_create is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("gmt_create is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("gmt_create =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("gmt_create <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("gmt_create >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_create >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("gmt_create <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_create <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("gmt_create in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("gmt_create not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("gmt_create between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_create not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andPowerIsNull() {
            addCriterion("power is null");
            return (Criteria) this;
        }

        public Criteria andPowerIsNotNull() {
            addCriterion("power is not null");
            return (Criteria) this;
        }

        public Criteria andPowerEqualTo(Double value) {
            addCriterion("power =", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotEqualTo(Double value) {
            addCriterion("power <>", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerGreaterThan(Double value) {
            addCriterion("power >", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerGreaterThanOrEqualTo(Double value) {
            addCriterion("power >=", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerLessThan(Double value) {
            addCriterion("power <", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerLessThanOrEqualTo(Double value) {
            addCriterion("power <=", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerIn(List<Double> values) {
            addCriterion("power in", values, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotIn(List<Double> values) {
            addCriterion("power not in", values, "power");
            return (Criteria) this;
        }

        public Criteria andPowerBetween(Double value1, Double value2) {
            addCriterion("power between", value1, value2, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotBetween(Double value1, Double value2) {
            addCriterion("power not between", value1, value2, "power");
            return (Criteria) this;
        }

        public Criteria andNamiIsNull() {
            addCriterion("nami is null");
            return (Criteria) this;
        }

        public Criteria andNamiIsNotNull() {
            addCriterion("nami is not null");
            return (Criteria) this;
        }

        public Criteria andNamiEqualTo(Integer value) {
            addCriterion("nami =", value, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiNotEqualTo(Integer value) {
            addCriterion("nami <>", value, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiGreaterThan(Integer value) {
            addCriterion("nami >", value, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiGreaterThanOrEqualTo(Integer value) {
            addCriterion("nami >=", value, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiLessThan(Integer value) {
            addCriterion("nami <", value, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiLessThanOrEqualTo(Integer value) {
            addCriterion("nami <=", value, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiIn(List<Integer> values) {
            addCriterion("nami in", values, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiNotIn(List<Integer> values) {
            addCriterion("nami not in", values, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiBetween(Integer value1, Integer value2) {
            addCriterion("nami between", value1, value2, "nami");
            return (Criteria) this;
        }

        public Criteria andNamiNotBetween(Integer value1, Integer value2) {
            addCriterion("nami not between", value1, value2, "nami");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvIsNull() {
            addCriterion("rui_pinlv is null");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvIsNotNull() {
            addCriterion("rui_pinlv is not null");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvEqualTo(Double value) {
            addCriterion("rui_pinlv =", value, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvNotEqualTo(Double value) {
            addCriterion("rui_pinlv <>", value, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvGreaterThan(Double value) {
            addCriterion("rui_pinlv >", value, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvGreaterThanOrEqualTo(Double value) {
            addCriterion("rui_pinlv >=", value, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvLessThan(Double value) {
            addCriterion("rui_pinlv <", value, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvLessThanOrEqualTo(Double value) {
            addCriterion("rui_pinlv <=", value, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvIn(List<Double> values) {
            addCriterion("rui_pinlv in", values, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvNotIn(List<Double> values) {
            addCriterion("rui_pinlv not in", values, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvBetween(Double value1, Double value2) {
            addCriterion("rui_pinlv between", value1, value2, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andRuiPinlvNotBetween(Double value1, Double value2) {
            addCriterion("rui_pinlv not between", value1, value2, "ruiPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvIsNull() {
            addCriterion("super_pinlv is null");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvIsNotNull() {
            addCriterion("super_pinlv is not null");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvEqualTo(Double value) {
            addCriterion("super_pinlv =", value, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvNotEqualTo(Double value) {
            addCriterion("super_pinlv <>", value, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvGreaterThan(Double value) {
            addCriterion("super_pinlv >", value, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvGreaterThanOrEqualTo(Double value) {
            addCriterion("super_pinlv >=", value, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvLessThan(Double value) {
            addCriterion("super_pinlv <", value, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvLessThanOrEqualTo(Double value) {
            addCriterion("super_pinlv <=", value, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvIn(List<Double> values) {
            addCriterion("super_pinlv in", values, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvNotIn(List<Double> values) {
            addCriterion("super_pinlv not in", values, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvBetween(Double value1, Double value2) {
            addCriterion("super_pinlv between", value1, value2, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andSuperPinlvNotBetween(Double value1, Double value2) {
            addCriterion("super_pinlv not between", value1, value2, "superPinlv");
            return (Criteria) this;
        }

        public Criteria andMainBoardIsNull() {
            addCriterion("main_board is null");
            return (Criteria) this;
        }

        public Criteria andMainBoardIsNotNull() {
            addCriterion("main_board is not null");
            return (Criteria) this;
        }

        public Criteria andMainBoardEqualTo(String value) {
            addCriterion("main_board =", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardNotEqualTo(String value) {
            addCriterion("main_board <>", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardGreaterThan(String value) {
            addCriterion("main_board >", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardGreaterThanOrEqualTo(String value) {
            addCriterion("main_board >=", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardLessThan(String value) {
            addCriterion("main_board <", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardLessThanOrEqualTo(String value) {
            addCriterion("main_board <=", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardLike(String value) {
            addCriterion("main_board like", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardNotLike(String value) {
            addCriterion("main_board not like", value, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardIn(List<String> values) {
            addCriterion("main_board in", values, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardNotIn(List<String> values) {
            addCriterion("main_board not in", values, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardBetween(String value1, String value2) {
            addCriterion("main_board between", value1, value2, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMainBoardNotBetween(String value1, String value2) {
            addCriterion("main_board not between", value1, value2, "mainBoard");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeIsNull() {
            addCriterion("memory_type is null");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeIsNotNull() {
            addCriterion("memory_type is not null");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeEqualTo(String value) {
            addCriterion("memory_type =", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotEqualTo(String value) {
            addCriterion("memory_type <>", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeGreaterThan(String value) {
            addCriterion("memory_type >", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeGreaterThanOrEqualTo(String value) {
            addCriterion("memory_type >=", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeLessThan(String value) {
            addCriterion("memory_type <", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeLessThanOrEqualTo(String value) {
            addCriterion("memory_type <=", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeLike(String value) {
            addCriterion("memory_type like", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotLike(String value) {
            addCriterion("memory_type not like", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeIn(List<String> values) {
            addCriterion("memory_type in", values, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotIn(List<String> values) {
            addCriterion("memory_type not in", values, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeBetween(String value1, String value2) {
            addCriterion("memory_type between", value1, value2, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotBetween(String value1, String value2) {
            addCriterion("memory_type not between", value1, value2, "memoryType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeIsNull() {
            addCriterion("interface_type is null");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeIsNotNull() {
            addCriterion("interface_type is not null");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeEqualTo(String value) {
            addCriterion("interface_type =", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeNotEqualTo(String value) {
            addCriterion("interface_type <>", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeGreaterThan(String value) {
            addCriterion("interface_type >", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeGreaterThanOrEqualTo(String value) {
            addCriterion("interface_type >=", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeLessThan(String value) {
            addCriterion("interface_type <", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeLessThanOrEqualTo(String value) {
            addCriterion("interface_type <=", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeLike(String value) {
            addCriterion("interface_type like", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeNotLike(String value) {
            addCriterion("interface_type not like", value, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeIn(List<String> values) {
            addCriterion("interface_type in", values, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeNotIn(List<String> values) {
            addCriterion("interface_type not in", values, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeBetween(String value1, String value2) {
            addCriterion("interface_type between", value1, value2, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andInterfaceTypeNotBetween(String value1, String value2) {
            addCriterion("interface_type not between", value1, value2, "interfaceType");
            return (Criteria) this;
        }

        public Criteria andExtraInfoIsNull() {
            addCriterion("extra_info is null");
            return (Criteria) this;
        }

        public Criteria andExtraInfoIsNotNull() {
            addCriterion("extra_info is not null");
            return (Criteria) this;
        }

        public Criteria andExtraInfoEqualTo(String value) {
            addCriterion("extra_info =", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotEqualTo(String value) {
            addCriterion("extra_info <>", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoGreaterThan(String value) {
            addCriterion("extra_info >", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoGreaterThanOrEqualTo(String value) {
            addCriterion("extra_info >=", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoLessThan(String value) {
            addCriterion("extra_info <", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoLessThanOrEqualTo(String value) {
            addCriterion("extra_info <=", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoLike(String value) {
            addCriterion("extra_info like", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotLike(String value) {
            addCriterion("extra_info not like", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoIn(List<String> values) {
            addCriterion("extra_info in", values, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotIn(List<String> values) {
            addCriterion("extra_info not in", values, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoBetween(String value1, String value2) {
            addCriterion("extra_info between", value1, value2, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotBetween(String value1, String value2) {
            addCriterion("extra_info not between", value1, value2, "extraInfo");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}