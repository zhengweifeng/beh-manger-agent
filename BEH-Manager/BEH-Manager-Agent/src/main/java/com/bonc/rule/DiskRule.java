package com.bonc.rule;


import com.bonc.entity.Disk;
import com.bonc.entity.RuleEntity;
import com.bonc.entity.WarnEntity;

public class DiskRule implements IRule{

	
	//产生判断数据
	public float execute(Object obj) {
		Disk disk = (Disk)obj;
		return disk.getUsed() * 100 / disk.getSize();
	}
	
}
