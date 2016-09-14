package com.bonc.rule;

import com.bonc.entity.RuleEntity;
import com.bonc.entity.WarnEntity;

public interface IRule {
	/**
	 * 获取判断数据
	 * @param obj
	 * @param rList
	 */
	public float execute(Object obj);
}
