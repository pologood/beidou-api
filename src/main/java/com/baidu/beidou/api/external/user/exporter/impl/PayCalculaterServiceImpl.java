package com.baidu.beidou.api.external.user.exporter.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.account.service.AccountMgr;
import com.baidu.beidou.api.external.user.exporter.PayCalculaterService;
import com.baidu.beidou.api.external.user.vo.request.PaymentRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.util.LogUtils;

/**
 * ClassName: PayCalculaterServiceImpl
 * Function: 获取用户上周消费，提供给dr-api，计算配额使用
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class PayCalculaterServiceImpl implements PayCalculaterService {
	private static final Log LOG = LogFactory
			.getLog(PayCalculaterServiceImpl.class);
	private AccountMgr accountMgr = null;

	public ApiResult<Double> getUserCostLastWeek(DataPrivilege user,
			PaymentRequest request, ApiOption apiOption) {

		ApiResult<Double> result = new ApiResult<Double>();
		if (request == null || request.getUserIds() == null
				|| request.getUserIds().length < 1
				|| request.getStartDate() == null
				|| request.getEndDate() == null) {
			result.setData(new ArrayList<Double>());
			LOG.fatal("error parameter in PayCalculaterServiceImpl");
			return result;
		}

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date date = null;
			date = format.parse(request.getStartDate());
			start.setTime(date);
			date = format.parse(request.getEndDate());
			end.setTime(date);
		} catch (ParseException e) {
			result.setData(new ArrayList<Double>());
			LOG.fatal("error date format in PayCalculaterServiceImpl");
			return result;
		}

		if (start.compareTo(end) > 0) {
			result.setData(new ArrayList<Double>());
			LOG.fatal("start date can't later than end date");
			return result;
		}

		double[] weekCost = new double[request.getUserIds().length];
		double[] dayCost = null;
		for (; start.compareTo(end) <= 0; start.add(Calendar.DATE, 1)) {
			dayCost = accountMgr.findUserCostByUserDate(request.getUserIds(),
					start.getTime());
			if (dayCost == null || weekCost.length != dayCost.length) {
				LOG.error("output is not the same size with input");
				LogUtils
						.fatal(LOG,
								"output is not the same size with input in getUserCostLastWeek");
				return result;
			}
			for (int index = 0; index < weekCost.length; index++) {
				weekCost[index] += dayCost[index];
			}
		}

		result.setData(Arrays.asList(ArrayUtils.toObject(weekCost)));

		return result;
	}

	/**
	 * @return the accountMgr
	 */
	public AccountMgr getAccountMgr() {
		return accountMgr;
	}

	/**
	 * @param accountMgr
	 *            the accountMgr to set
	 */
	public void setAccountMgr(AccountMgr accountMgr) {
		this.accountMgr = accountMgr;
	}

}
