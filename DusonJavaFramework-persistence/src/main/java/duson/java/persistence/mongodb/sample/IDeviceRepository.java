package duson.java.persistence.mongodb.sample;

import org.springframework.data.domain.Page;

import duson.java.persistence.mongodb.repository.IBaseRepository;
import duson.java.persistence.mongodb.sample.entity.Device;

public interface IDeviceRepository extends IBaseRepository<Device, String> {
	Device findByUserId(long systemId, long userId);
	Page<Device> findByPage(DeviceFilter filter, int pageIndex, int pageSize);
	boolean saveDevicePlatform(long systemId, long userId, int pushPlatform, String appId, int osType, String deviceCode, String deviceNickName);
}
