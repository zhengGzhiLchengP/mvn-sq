package com.feiqu.system.model.basicData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShellScriptExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public ShellScriptExample() {
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

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andDictionaryIsNull() {
            addCriterion("dictionary is null");
            return (Criteria) this;
        }

        public Criteria andDictionaryIsNotNull() {
            addCriterion("dictionary is not null");
            return (Criteria) this;
        }

        public Criteria andDictionaryEqualTo(String value) {
            addCriterion("dictionary =", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryNotEqualTo(String value) {
            addCriterion("dictionary <>", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryGreaterThan(String value) {
            addCriterion("dictionary >", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryGreaterThanOrEqualTo(String value) {
            addCriterion("dictionary >=", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryLessThan(String value) {
            addCriterion("dictionary <", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryLessThanOrEqualTo(String value) {
            addCriterion("dictionary <=", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryLike(String value) {
            addCriterion("dictionary like", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryNotLike(String value) {
            addCriterion("dictionary not like", value, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryIn(List<String> values) {
            addCriterion("dictionary in", values, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryNotIn(List<String> values) {
            addCriterion("dictionary not in", values, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryBetween(String value1, String value2) {
            addCriterion("dictionary between", value1, value2, "dictionary");
            return (Criteria) this;
        }

        public Criteria andDictionaryNotBetween(String value1, String value2) {
            addCriterion("dictionary not between", value1, value2, "dictionary");
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

        public Criteria andOperatorIsNull() {
            addCriterion("operator is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIsNotNull() {
            addCriterion("operator is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorEqualTo(String value) {
            addCriterion("operator =", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotEqualTo(String value) {
            addCriterion("operator <>", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorGreaterThan(String value) {
            addCriterion("operator >", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorGreaterThanOrEqualTo(String value) {
            addCriterion("operator >=", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLessThan(String value) {
            addCriterion("operator <", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLessThanOrEqualTo(String value) {
            addCriterion("operator <=", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLike(String value) {
            addCriterion("operator like", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotLike(String value) {
            addCriterion("operator not like", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorIn(List<String> values) {
            addCriterion("operator in", values, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotIn(List<String> values) {
            addCriterion("operator not in", values, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorBetween(String value1, String value2) {
            addCriterion("operator between", value1, value2, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotBetween(String value1, String value2) {
            addCriterion("operator not between", value1, value2, "operator");
            return (Criteria) this;
        }

        public Criteria andShellFileNameIsNull() {
            addCriterion("shell_file_name is null");
            return (Criteria) this;
        }

        public Criteria andShellFileNameIsNotNull() {
            addCriterion("shell_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andShellFileNameEqualTo(String value) {
            addCriterion("shell_file_name =", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameNotEqualTo(String value) {
            addCriterion("shell_file_name <>", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameGreaterThan(String value) {
            addCriterion("shell_file_name >", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("shell_file_name >=", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameLessThan(String value) {
            addCriterion("shell_file_name <", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameLessThanOrEqualTo(String value) {
            addCriterion("shell_file_name <=", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameLike(String value) {
            addCriterion("shell_file_name like", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameNotLike(String value) {
            addCriterion("shell_file_name not like", value, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameIn(List<String> values) {
            addCriterion("shell_file_name in", values, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameNotIn(List<String> values) {
            addCriterion("shell_file_name not in", values, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameBetween(String value1, String value2) {
            addCriterion("shell_file_name between", value1, value2, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellFileNameNotBetween(String value1, String value2) {
            addCriterion("shell_file_name not between", value1, value2, "shellFileName");
            return (Criteria) this;
        }

        public Criteria andShellNameIsNull() {
            addCriterion("shell_name is null");
            return (Criteria) this;
        }

        public Criteria andShellNameIsNotNull() {
            addCriterion("shell_name is not null");
            return (Criteria) this;
        }

        public Criteria andShellNameEqualTo(String value) {
            addCriterion("shell_name =", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameNotEqualTo(String value) {
            addCriterion("shell_name <>", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameGreaterThan(String value) {
            addCriterion("shell_name >", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameGreaterThanOrEqualTo(String value) {
            addCriterion("shell_name >=", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameLessThan(String value) {
            addCriterion("shell_name <", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameLessThanOrEqualTo(String value) {
            addCriterion("shell_name <=", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameLike(String value) {
            addCriterion("shell_name like", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameNotLike(String value) {
            addCriterion("shell_name not like", value, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameIn(List<String> values) {
            addCriterion("shell_name in", values, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameNotIn(List<String> values) {
            addCriterion("shell_name not in", values, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameBetween(String value1, String value2) {
            addCriterion("shell_name between", value1, value2, "shellName");
            return (Criteria) this;
        }

        public Criteria andShellNameNotBetween(String value1, String value2) {
            addCriterion("shell_name not between", value1, value2, "shellName");
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