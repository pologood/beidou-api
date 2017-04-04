package com.baidu.beidou.api.internal.util.converter;

import org.dozer.DozerConverter;

/**
 * ClassName: YuanToCentConverter
 * Function: TODO ADD FUNCTION
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class YuanToCentConverter extends DozerConverter<Integer, Float> {

	public YuanToCentConverter() {
		super(Integer.class, Float.class);
	}

	@Override
	public Integer convertFrom(Float source, Integer destination) {
		if (source == null) {
			return null;
		}
		return Math.round(Float.valueOf(source * 100));
	}

	@Override
	public Float convertTo(Integer source, Float destination) {
		if (source == null) {
			return null;
		}
		return source.intValue() / 100F;
	}

}
