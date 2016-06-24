package duson.java.persistence.mongodb.sample.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import duson.java.persistence.mongodb.repository.SimpleMongoRepositoryEx;
import duson.java.persistence.mongodb.sample.DeviceFilter;
import duson.java.persistence.mongodb.sample.IDeviceRepository;
import duson.java.persistence.mongodb.sample.entity.Device;
import duson.java.persistence.mongodb.sample.entity.DevicePlatform;

@Repository
public class DeviceRepositoryImpl extends SimpleMongoRepositoryEx<Device, String> implements IDeviceRepository {
	private MongoTemplate mongoTemplate;
	    
	@Autowired
	public DeviceRepositoryImpl(MongoTemplate mongoTemplate) {
		super(mongoTemplate, Device.class);
		
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Device findByUserId(long systemId, long userId) {
		Query query = new Query();
		CriteriaDefinition criteria = Criteria.where("systemId").is(systemId).and("userId").is(userId);
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, Device.class);
	}
	
	@Override
	public Page<Device> findByPage(DeviceFilter filter, int pageIndex, int pageSize) {
		PageRequest pageReq = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "createdDtm");
		Criteria criteria = new Criteria();
		
		if(filter.getSystemId() != null)
			criteria = criteria.andOperator(Criteria.where("systemId").is(filter.getSystemId()));
		if(filter.getUserId() != null)
			criteria = criteria.andOperator(Criteria.where("userId").is(filter.getUserId()));
		if(filter.getUserName() != null && !filter.getUserName().isEmpty())
			criteria = criteria.andOperator(Criteria.where("userName").regex(filter.getUserName()));
		if(filter.getOsType() != null)
			criteria = criteria.andOperator(Criteria.where("platforms").elemMatch(Criteria.where("osType").is(filter.getOsType())));
		if(filter.getPlatformAppId() != null && !filter.getPlatformAppId().isEmpty())
			criteria = criteria.andOperator(Criteria.where("platforms").elemMatch(Criteria.where("platformAppId").is(filter.getPlatformAppId())));
		if(filter.getPushPlatform() != null)
			criteria = criteria.andOperator(Criteria.where("platforms").elemMatch(Criteria.where("pushPlatform").is(filter.getPushPlatform())));
		
		// Or操作
		criteria = criteria.orOperator(Criteria.where("title").regex(filter.getUserName()),
				Criteria.where("content").regex(filter.getUserName()));
		
		if(filter.getTagId() != null && !filter.getTagId().isEmpty())
			criteria = criteria.andOperator(Criteria.where("tags").elemMatch(Criteria.where("$id").is(new ObjectId(filter.getTagId()))));
		
		/*
		多条件查询另一种方式：条件数组
		List<Criteria> conditions = new ArrayList<Criteria>();
		if(filter.getSystemId() != null)
			conditions.add(Criteria.where("system.$id").is(filter.getSystemId()));
		Criteria criteria2 = new Criteria();
		if(conditions.size() > 0)
			criteria2.andOperator(conditions.toArray((Criteria[])new Criteria[0]));*/
		
		return super.findAll(criteria, pageReq);
	}
	
	public boolean saveDevicePlatform(long systemId, long userId, int pushPlatform, String appId, int osType, String deviceCode, String deviceNickName) {
		Query deviceQuery = Query.query(Criteria.where("systemId").is(systemId).and("userId").is(userId));
		
		if(!mongoTemplate.exists(deviceQuery, Device.class)) {
			return false;
		}
		
		Criteria criteria = Criteria.where("systemId").is(systemId).and("userId").is(userId)
				.and("platforms").elemMatch(Criteria.where("platformAppId").is(appId).and("pushPlatform").is(pushPlatform));
		
		criteria = criteria.andOperator(Criteria.where("platforms").elemMatch(Criteria.where("deviceCode").is(deviceCode)));
		
		Query platformQuery = Query.query(criteria);
				
		Update update = new Update();
		if(!mongoTemplate.exists(platformQuery, Device.class)) {
			DevicePlatform devicePlatform = new DevicePlatform();
			devicePlatform.setPushPlatform(pushPlatform);
			devicePlatform.setPlatformAppId(appId);
			devicePlatform.setOsType(osType);
			devicePlatform.setDeviceCode(deviceCode);
			if(deviceNickName != null && !deviceNickName.isEmpty())
				devicePlatform.setDeviceNickName(deviceNickName);
			
			//AddToSetBuilder bulider = update.addToSet("platforms");
            //bulider.each(devicePlatform);
			
			update.addToSet("platforms", devicePlatform);
			mongoTemplate.findAndModify(deviceQuery, update, Device.class);
		} else {
			update.set("platforms.$.deviceNickName", deviceNickName);
	        mongoTemplate.updateFirst(platformQuery, update, Device.class);
		}
		
		return true;
	}

	public List<Device> findUserByOrAnd() {
		Query query = new Query();
		Criteria criteriaOr1 = new Criteria();
		criteriaOr1.orOperator(Criteria.where("age").is(4),
				Criteria.where("emale").is("male"));
		Criteria criteriaOr2 = new Criteria();
		criteriaOr2.orOperator(Criteria.where("name").is("老李"),
				Criteria.where("email").is("airfey@126.com"));
		Criteria criteria3 = new Criteria();
		criteria3.andOperator(criteriaOr1, criteriaOr2);

		query.addCriteria(criteria3);

		return mongoTemplate.find(query, Device.class);
	}
	
	public Device findUsersByPage(Integer age, int beginNum, int pageSize) {
		Query query = new Query();
		Device userVo = null;
		if (null != age) {
			query.addCriteria(Criteria.where("age").is(age));
		}
		long count = mongoTemplate.count(query, Device.class);
		if (count > 0) {
			query.with(new Sort(Direction.DESC, "_id"));
			query.skip(beginNum).limit(pageSize);
			mongoTemplate.find(query, Device.class);
		}
		return userVo;
	}
	
	public List<Device> findUserByGroup() {
		GroupBy groupBy = GroupBy.key("age").initialDocument("{count:0}")
				.reduceFunction("function(doc, prev){prev.count+=1}");

		GroupByResults<Device> results = mongoTemplate.group(
				this.getCollectionName(Device.class), groupBy, Device.class);

		BasicDBList list = (BasicDBList) results.getRawResults().get("retval");

		List<Device> ageCounts = null;
		if (null != list && list.size() > 0) {
			ageCounts = new ArrayList<Device>();
			Device ageCount = new Device();
			for (int i = 0; i < list.size(); i++) {
				BasicDBObject obj = (BasicDBObject) list.get(i);
				//ageCount.setAge(Float.valueOf(obj.get("age").toString()));
				//ageCount.setCount(Float.valueOf(obj.get("count").toString()));
				ageCounts.add(ageCount);
			}
			return ageCounts;
		} else {
			return null;
		}
	}
	
	protected String getCollectionName(Class<?> entityClass) {
		// 查看该类上面Document注解
		Document document = entityClass.getAnnotation(Document.class);
		// 如果类上面有Document注解，并且collection属性有值，则数据库collection名称即为该Document注解的collection的值
		if (document != null && document.collection() != null
				&& !"".equals(document.collection().trim())) {
			return document.collection();
		}
		return entityClass.getSimpleName();
	}
	
}
