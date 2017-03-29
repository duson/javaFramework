package com;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

@Component
public class BillCheckJob implements SimpleJob {
	private static Logger logger = Logger.getLogger("bill");
	
	@Override
	public void execute(ShardingContext ctx) {
		
	}

}
