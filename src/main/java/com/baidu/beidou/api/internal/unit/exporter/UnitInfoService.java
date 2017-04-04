package com.baidu.beidou.api.internal.unit.exporter;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.internal.unit.vo.MediaResult;
import com.baidu.beidou.api.internal.unit.vo.UBMCTextParam;
import com.baidu.beidou.api.internal.unit.vo.UnitParam;
import com.baidu.beidou.api.internal.unit.vo.UnitPreResult;
import com.baidu.beidou.api.internal.unit.vo.UnitResult;
import com.baidu.beidou.cprounit.vo.UnitInfoView;

public interface UnitInfoService {

	public UnitResult<String> getUbmcTmpUrl(Long mcId, Integer versionId);
	
	public Map<Long, String> getUBMCUrlByMcIdAndMcVersionId(List<UBMCTextParam> paramList);
	
    /**
     * Function: 获取创意的预览html片段，创意必须是智能创意
     * @author genglei01
     * @param unitId    unitId
     * @param userId    userId
     * @param flowTag    0-beidou, 1-google
     * @return string
     */
    public UnitResult<UnitPreResult> getHtmlSnippetForSmartUnit(Long unitId, Integer userId, Integer flowTag);

    /**
     * Function: 上传实验性的二进制多媒体数据
     * 
     * @author genglei01
     * @param desc  描述
     * @param data  二进制data
     * @return MediaResult
     */
    public UnitResult<MediaResult> uploadMedia(String desc, byte[] data);

    /**
     * 获取创意列表
     * @param unitParamList 创意参数列表
     * @return 创意信息列表
     */
    public UnitResult<UnitInfoView> getUnitList(List<UnitParam> unitParamList);
}
