package com.feiqu.system.model.basicData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WsLogExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public WsLogExample() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveIsNull() {
            addCriterion("bytesPerTcpReceive is null");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveIsNotNull() {
            addCriterion("bytesPerTcpReceive is not null");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveEqualTo(Double value) {
            addCriterion("bytesPerTcpReceive =", value, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveNotEqualTo(Double value) {
            addCriterion("bytesPerTcpReceive <>", value, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveGreaterThan(Double value) {
            addCriterion("bytesPerTcpReceive >", value, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveGreaterThanOrEqualTo(Double value) {
            addCriterion("bytesPerTcpReceive >=", value, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveLessThan(Double value) {
            addCriterion("bytesPerTcpReceive <", value, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveLessThanOrEqualTo(Double value) {
            addCriterion("bytesPerTcpReceive <=", value, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveIn(List<Double> values) {
            addCriterion("bytesPerTcpReceive in", values, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveNotIn(List<Double> values) {
            addCriterion("bytesPerTcpReceive not in", values, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveBetween(Double value1, Double value2) {
            addCriterion("bytesPerTcpReceive between", value1, value2, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andBytespertcpreceiveNotBetween(Double value1, Double value2) {
            addCriterion("bytesPerTcpReceive not between", value1, value2, "bytespertcpreceive");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountIsNull() {
            addCriterion("decodeErrorCount is null");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountIsNotNull() {
            addCriterion("decodeErrorCount is not null");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountEqualTo(Integer value) {
            addCriterion("decodeErrorCount =", value, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountNotEqualTo(Integer value) {
            addCriterion("decodeErrorCount <>", value, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountGreaterThan(Integer value) {
            addCriterion("decodeErrorCount >", value, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("decodeErrorCount >=", value, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountLessThan(Integer value) {
            addCriterion("decodeErrorCount <", value, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountLessThanOrEqualTo(Integer value) {
            addCriterion("decodeErrorCount <=", value, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountIn(List<Integer> values) {
            addCriterion("decodeErrorCount in", values, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountNotIn(List<Integer> values) {
            addCriterion("decodeErrorCount not in", values, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountBetween(Integer value1, Integer value2) {
            addCriterion("decodeErrorCount between", value1, value2, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDecodeerrorcountNotBetween(Integer value1, Integer value2) {
            addCriterion("decodeErrorCount not between", value1, value2, "decodeerrorcount");
            return (Criteria) this;
        }

        public Criteria andDurationIsNull() {
            addCriterion("duration is null");
            return (Criteria) this;
        }

        public Criteria andDurationIsNotNull() {
            addCriterion("duration is not null");
            return (Criteria) this;
        }

        public Criteria andDurationEqualTo(Long value) {
            addCriterion("duration =", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotEqualTo(Long value) {
            addCriterion("duration <>", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThan(Long value) {
            addCriterion("duration >", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThanOrEqualTo(Long value) {
            addCriterion("duration >=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThan(Long value) {
            addCriterion("duration <", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThanOrEqualTo(Long value) {
            addCriterion("duration <=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationIn(List<Long> values) {
            addCriterion("duration in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotIn(List<Long> values) {
            addCriterion("duration not in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationBetween(Long value1, Long value2) {
            addCriterion("duration between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotBetween(Long value1, Long value2) {
            addCriterion("duration not between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationtypeIsNull() {
            addCriterion("durationType is null");
            return (Criteria) this;
        }

        public Criteria andDurationtypeIsNotNull() {
            addCriterion("durationType is not null");
            return (Criteria) this;
        }

        public Criteria andDurationtypeEqualTo(Integer value) {
            addCriterion("durationType =", value, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeNotEqualTo(Integer value) {
            addCriterion("durationType <>", value, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeGreaterThan(Integer value) {
            addCriterion("durationType >", value, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("durationType >=", value, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeLessThan(Integer value) {
            addCriterion("durationType <", value, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeLessThanOrEqualTo(Integer value) {
            addCriterion("durationType <=", value, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeIn(List<Integer> values) {
            addCriterion("durationType in", values, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeNotIn(List<Integer> values) {
            addCriterion("durationType not in", values, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeBetween(Integer value1, Integer value2) {
            addCriterion("durationType between", value1, value2, "durationtype");
            return (Criteria) this;
        }

        public Criteria andDurationtypeNotBetween(Integer value1, Integer value2) {
            addCriterion("durationType not between", value1, value2, "durationtype");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationIsNull() {
            addCriterion("formated_duration is null");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationIsNotNull() {
            addCriterion("formated_duration is not null");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationEqualTo(String value) {
            addCriterion("formated_duration =", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationNotEqualTo(String value) {
            addCriterion("formated_duration <>", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationGreaterThan(String value) {
            addCriterion("formated_duration >", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationGreaterThanOrEqualTo(String value) {
            addCriterion("formated_duration >=", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationLessThan(String value) {
            addCriterion("formated_duration <", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationLessThanOrEqualTo(String value) {
            addCriterion("formated_duration <=", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationLike(String value) {
            addCriterion("formated_duration like", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationNotLike(String value) {
            addCriterion("formated_duration not like", value, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationIn(List<String> values) {
            addCriterion("formated_duration in", values, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationNotIn(List<String> values) {
            addCriterion("formated_duration not in", values, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationBetween(String value1, String value2) {
            addCriterion("formated_duration between", value1, value2, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andFormatedDurationNotBetween(String value1, String value2) {
            addCriterion("formated_duration not between", value1, value2, "formatedDuration");
            return (Criteria) this;
        }

        public Criteria andHandledBytesIsNull() {
            addCriterion("handled_bytes is null");
            return (Criteria) this;
        }

        public Criteria andHandledBytesIsNotNull() {
            addCriterion("handled_bytes is not null");
            return (Criteria) this;
        }

        public Criteria andHandledBytesEqualTo(Long value) {
            addCriterion("handled_bytes =", value, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesNotEqualTo(Long value) {
            addCriterion("handled_bytes <>", value, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesGreaterThan(Long value) {
            addCriterion("handled_bytes >", value, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesGreaterThanOrEqualTo(Long value) {
            addCriterion("handled_bytes >=", value, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesLessThan(Long value) {
            addCriterion("handled_bytes <", value, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesLessThanOrEqualTo(Long value) {
            addCriterion("handled_bytes <=", value, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesIn(List<Long> values) {
            addCriterion("handled_bytes in", values, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesNotIn(List<Long> values) {
            addCriterion("handled_bytes not in", values, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesBetween(Long value1, Long value2) {
            addCriterion("handled_bytes between", value1, value2, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledBytesNotBetween(Long value1, Long value2) {
            addCriterion("handled_bytes not between", value1, value2, "handledBytes");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketIsNull() {
            addCriterion("handled_costs_per_packet is null");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketIsNotNull() {
            addCriterion("handled_costs_per_packet is not null");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketEqualTo(Float value) {
            addCriterion("handled_costs_per_packet =", value, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketNotEqualTo(Float value) {
            addCriterion("handled_costs_per_packet <>", value, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketGreaterThan(Float value) {
            addCriterion("handled_costs_per_packet >", value, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketGreaterThanOrEqualTo(Float value) {
            addCriterion("handled_costs_per_packet >=", value, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketLessThan(Float value) {
            addCriterion("handled_costs_per_packet <", value, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketLessThanOrEqualTo(Float value) {
            addCriterion("handled_costs_per_packet <=", value, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketIn(List<Float> values) {
            addCriterion("handled_costs_per_packet in", values, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketNotIn(List<Float> values) {
            addCriterion("handled_costs_per_packet not in", values, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketBetween(Float value1, Float value2) {
            addCriterion("handled_costs_per_packet between", value1, value2, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledCostsPerPacketNotBetween(Float value1, Float value2) {
            addCriterion("handled_costs_per_packet not between", value1, value2, "handledCostsPerPacket");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsIsNull() {
            addCriterion("handled_packet_costs is null");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsIsNotNull() {
            addCriterion("handled_packet_costs is not null");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsEqualTo(Integer value) {
            addCriterion("handled_packet_costs =", value, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsNotEqualTo(Integer value) {
            addCriterion("handled_packet_costs <>", value, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsGreaterThan(Integer value) {
            addCriterion("handled_packet_costs >", value, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsGreaterThanOrEqualTo(Integer value) {
            addCriterion("handled_packet_costs >=", value, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsLessThan(Integer value) {
            addCriterion("handled_packet_costs <", value, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsLessThanOrEqualTo(Integer value) {
            addCriterion("handled_packet_costs <=", value, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsIn(List<Integer> values) {
            addCriterion("handled_packet_costs in", values, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsNotIn(List<Integer> values) {
            addCriterion("handled_packet_costs not in", values, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsBetween(Integer value1, Integer value2) {
            addCriterion("handled_packet_costs between", value1, value2, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketCostsNotBetween(Integer value1, Integer value2) {
            addCriterion("handled_packet_costs not between", value1, value2, "handledPacketCosts");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsIsNull() {
            addCriterion("handled_packets is null");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsIsNotNull() {
            addCriterion("handled_packets is not null");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsEqualTo(Integer value) {
            addCriterion("handled_packets =", value, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsNotEqualTo(Integer value) {
            addCriterion("handled_packets <>", value, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsGreaterThan(Integer value) {
            addCriterion("handled_packets >", value, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsGreaterThanOrEqualTo(Integer value) {
            addCriterion("handled_packets >=", value, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsLessThan(Integer value) {
            addCriterion("handled_packets <", value, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsLessThanOrEqualTo(Integer value) {
            addCriterion("handled_packets <=", value, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsIn(List<Integer> values) {
            addCriterion("handled_packets in", values, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsNotIn(List<Integer> values) {
            addCriterion("handled_packets not in", values, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsBetween(Integer value1, Integer value2) {
            addCriterion("handled_packets between", value1, value2, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andHandledPacketsNotBetween(Integer value1, Integer value2) {
            addCriterion("handled_packets not between", value1, value2, "handledPackets");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveIsNull() {
            addCriterion("packets_per_tcp_receive is null");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveIsNotNull() {
            addCriterion("packets_per_tcp_receive is not null");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveEqualTo(Double value) {
            addCriterion("packets_per_tcp_receive =", value, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveNotEqualTo(Double value) {
            addCriterion("packets_per_tcp_receive <>", value, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveGreaterThan(Double value) {
            addCriterion("packets_per_tcp_receive >", value, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveGreaterThanOrEqualTo(Double value) {
            addCriterion("packets_per_tcp_receive >=", value, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveLessThan(Double value) {
            addCriterion("packets_per_tcp_receive <", value, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveLessThanOrEqualTo(Double value) {
            addCriterion("packets_per_tcp_receive <=", value, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveIn(List<Double> values) {
            addCriterion("packets_per_tcp_receive in", values, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveNotIn(List<Double> values) {
            addCriterion("packets_per_tcp_receive not in", values, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveBetween(Double value1, Double value2) {
            addCriterion("packets_per_tcp_receive between", value1, value2, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andPacketsPerTcpReceiveNotBetween(Double value1, Double value2) {
            addCriterion("packets_per_tcp_receive not between", value1, value2, "packetsPerTcpReceive");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesIsNull() {
            addCriterion("received_bytes is null");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesIsNotNull() {
            addCriterion("received_bytes is not null");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesEqualTo(Long value) {
            addCriterion("received_bytes =", value, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesNotEqualTo(Long value) {
            addCriterion("received_bytes <>", value, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesGreaterThan(Long value) {
            addCriterion("received_bytes >", value, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesGreaterThanOrEqualTo(Long value) {
            addCriterion("received_bytes >=", value, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesLessThan(Long value) {
            addCriterion("received_bytes <", value, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesLessThanOrEqualTo(Long value) {
            addCriterion("received_bytes <=", value, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesIn(List<Long> values) {
            addCriterion("received_bytes in", values, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesNotIn(List<Long> values) {
            addCriterion("received_bytes not in", values, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesBetween(Long value1, Long value2) {
            addCriterion("received_bytes between", value1, value2, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedBytesNotBetween(Long value1, Long value2) {
            addCriterion("received_bytes not between", value1, value2, "receivedBytes");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsIsNull() {
            addCriterion("received_packets is null");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsIsNotNull() {
            addCriterion("received_packets is not null");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsEqualTo(Integer value) {
            addCriterion("received_packets =", value, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsNotEqualTo(Integer value) {
            addCriterion("received_packets <>", value, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsGreaterThan(Integer value) {
            addCriterion("received_packets >", value, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsGreaterThanOrEqualTo(Integer value) {
            addCriterion("received_packets >=", value, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsLessThan(Integer value) {
            addCriterion("received_packets <", value, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsLessThanOrEqualTo(Integer value) {
            addCriterion("received_packets <=", value, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsIn(List<Integer> values) {
            addCriterion("received_packets in", values, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsNotIn(List<Integer> values) {
            addCriterion("received_packets not in", values, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsBetween(Integer value1, Integer value2) {
            addCriterion("received_packets between", value1, value2, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedPacketsNotBetween(Integer value1, Integer value2) {
            addCriterion("received_packets not between", value1, value2, "receivedPackets");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsIsNull() {
            addCriterion("received_tcps is null");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsIsNotNull() {
            addCriterion("received_tcps is not null");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsEqualTo(Integer value) {
            addCriterion("received_tcps =", value, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsNotEqualTo(Integer value) {
            addCriterion("received_tcps <>", value, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsGreaterThan(Integer value) {
            addCriterion("received_tcps >", value, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsGreaterThanOrEqualTo(Integer value) {
            addCriterion("received_tcps >=", value, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsLessThan(Integer value) {
            addCriterion("received_tcps <", value, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsLessThanOrEqualTo(Integer value) {
            addCriterion("received_tcps <=", value, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsIn(List<Integer> values) {
            addCriterion("received_tcps in", values, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsNotIn(List<Integer> values) {
            addCriterion("received_tcps not in", values, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsBetween(Integer value1, Integer value2) {
            addCriterion("received_tcps between", value1, value2, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andReceivedTcpsNotBetween(Integer value1, Integer value2) {
            addCriterion("received_tcps not between", value1, value2, "receivedTcps");
            return (Criteria) this;
        }

        public Criteria andRequestCountIsNull() {
            addCriterion("request_count is null");
            return (Criteria) this;
        }

        public Criteria andRequestCountIsNotNull() {
            addCriterion("request_count is not null");
            return (Criteria) this;
        }

        public Criteria andRequestCountEqualTo(Integer value) {
            addCriterion("request_count =", value, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountNotEqualTo(Integer value) {
            addCriterion("request_count <>", value, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountGreaterThan(Integer value) {
            addCriterion("request_count >", value, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("request_count >=", value, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountLessThan(Integer value) {
            addCriterion("request_count <", value, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountLessThanOrEqualTo(Integer value) {
            addCriterion("request_count <=", value, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountIn(List<Integer> values) {
            addCriterion("request_count in", values, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountNotIn(List<Integer> values) {
            addCriterion("request_count not in", values, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountBetween(Integer value1, Integer value2) {
            addCriterion("request_count between", value1, value2, "requestCount");
            return (Criteria) this;
        }

        public Criteria andRequestCountNotBetween(Integer value1, Integer value2) {
            addCriterion("request_count not between", value1, value2, "requestCount");
            return (Criteria) this;
        }

        public Criteria andSentBytesIsNull() {
            addCriterion("sent_bytes is null");
            return (Criteria) this;
        }

        public Criteria andSentBytesIsNotNull() {
            addCriterion("sent_bytes is not null");
            return (Criteria) this;
        }

        public Criteria andSentBytesEqualTo(Long value) {
            addCriterion("sent_bytes =", value, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesNotEqualTo(Long value) {
            addCriterion("sent_bytes <>", value, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesGreaterThan(Long value) {
            addCriterion("sent_bytes >", value, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesGreaterThanOrEqualTo(Long value) {
            addCriterion("sent_bytes >=", value, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesLessThan(Long value) {
            addCriterion("sent_bytes <", value, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesLessThanOrEqualTo(Long value) {
            addCriterion("sent_bytes <=", value, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesIn(List<Long> values) {
            addCriterion("sent_bytes in", values, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesNotIn(List<Long> values) {
            addCriterion("sent_bytes not in", values, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesBetween(Long value1, Long value2) {
            addCriterion("sent_bytes between", value1, value2, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentBytesNotBetween(Long value1, Long value2) {
            addCriterion("sent_bytes not between", value1, value2, "sentBytes");
            return (Criteria) this;
        }

        public Criteria andSentPacketsIsNull() {
            addCriterion("sent_packets is null");
            return (Criteria) this;
        }

        public Criteria andSentPacketsIsNotNull() {
            addCriterion("sent_packets is not null");
            return (Criteria) this;
        }

        public Criteria andSentPacketsEqualTo(Integer value) {
            addCriterion("sent_packets =", value, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsNotEqualTo(Integer value) {
            addCriterion("sent_packets <>", value, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsGreaterThan(Integer value) {
            addCriterion("sent_packets >", value, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsGreaterThanOrEqualTo(Integer value) {
            addCriterion("sent_packets >=", value, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsLessThan(Integer value) {
            addCriterion("sent_packets <", value, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsLessThanOrEqualTo(Integer value) {
            addCriterion("sent_packets <=", value, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsIn(List<Integer> values) {
            addCriterion("sent_packets in", values, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsNotIn(List<Integer> values) {
            addCriterion("sent_packets not in", values, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsBetween(Integer value1, Integer value2) {
            addCriterion("sent_packets between", value1, value2, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andSentPacketsNotBetween(Integer value1, Integer value2) {
            addCriterion("sent_packets not between", value1, value2, "sentPackets");
            return (Criteria) this;
        }

        public Criteria andStartIsNull() {
            addCriterion("start is null");
            return (Criteria) this;
        }

        public Criteria andStartIsNotNull() {
            addCriterion("start is not null");
            return (Criteria) this;
        }

        public Criteria andStartEqualTo(String value) {
            addCriterion("start =", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotEqualTo(String value) {
            addCriterion("start <>", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartGreaterThan(String value) {
            addCriterion("start >", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartGreaterThanOrEqualTo(String value) {
            addCriterion("start >=", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartLessThan(String value) {
            addCriterion("start <", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartLessThanOrEqualTo(String value) {
            addCriterion("start <=", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartLike(String value) {
            addCriterion("start like", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotLike(String value) {
            addCriterion("start not like", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartIn(List<String> values) {
            addCriterion("start in", values, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotIn(List<String> values) {
            addCriterion("start not in", values, "start");
            return (Criteria) this;
        }

        public Criteria andStartBetween(String value1, String value2) {
            addCriterion("start between", value1, value2, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotBetween(String value1, String value2) {
            addCriterion("start not between", value1, value2, "start");
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