package com.baidu.beidou.api.external.report.facade;

import java.util.Date;

public interface TransReportFacade {


	public boolean needToFetchTransData(Integer userId, Date from,
			Date to, boolean forceGet);

	public boolean isTransToolSigned(Integer userId, boolean forceGet);


	public String getTransName(Integer userId, Long siteId, Long transId);

}