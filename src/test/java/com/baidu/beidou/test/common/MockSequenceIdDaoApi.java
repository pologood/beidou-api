package com.baidu.beidou.test.common;

import com.baidu.beidou.util.dao.SequenceIdDao;

/**
 * SequenceIdDao mock实现，用于生成id
 * 
 * @author hexiufeng
 *
 */
public class MockSequenceIdDaoApi implements SequenceIdDao {
    private long vtCodeId = 1;
    private long userPackId = 1;
    private long unitId = 1;
    private long groupRt = 1;
    private long groupVtId = 1;
    private long groupItId = 1;
    private long packKeywordId = 1;

    @Override
    public Long getUnitId() {
        return unitId++;
    }

    @Override
    public Integer getUserPackIdAsInt() {
        return getUserPackId().intValue();
    }

    @Override
    public Integer[] getUserPackIdAsInt(int step) {
        Integer[] ids = new Integer[step];
        for (int i = 0; i < step; i++) {
            ids[i] = getUserPackIdAsInt();
        }
        return ids;
    }

    @Override
    public Long getUserPackId() {
        return userPackId++;
    }

    @Override
    public Long[] getUserPackId(int step) {
        Long[] ids = new Long[step];
        for (int i = 0; i < step; i++) {
            ids[i] = getUserPackId();
        }
        return ids;
    }

    @Override
    public Long getGroupRtId() {
        return groupRt++;
    }

//    @Override
//    public Long getGroupVtId() {
//        return groupVtId++;
//    }
//
//    @Override
//    public Long getGroupItId() {
//        return groupItId++;
//    }
//
//    @Override
//    public Long[] getGroupItId(int step) {
//        Long[] ids = new Long[step];
//        for (int i = 0; i < step; i++) {
//            ids[i] = getGroupItId();
//        }
//        return ids;
//    }
//
//    @Override
//    public Long getVtCodeId() {
//        return vtCodeId++;
//    }

    @Override
    public Long getPackKeywordId() {
        return packKeywordId++;
    }

    @Override
    public Long[] getPackKeywordId(int step) {
        Long[] ids = new Long[step];
        for (int i = 0; i < step; i++) {
            ids[i] = getPackKeywordId();
        }
        return ids;
    }

}
