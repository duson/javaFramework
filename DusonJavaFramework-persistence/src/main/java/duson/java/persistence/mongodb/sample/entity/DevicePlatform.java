package duson.java.persistence.mongodb.sample.entity;

public class DevicePlatform {

	private Integer pushPlatform; // 0：百度推送，1：腾讯信鸽，2：小米推送，3：极光推送
	
    private String platformAppId;
    
    private int osType; // 0：Android，1：IOS

    private String deviceCode;

    private String deviceNickName;

    public Integer getPushPlatform() {
		return pushPlatform;
	}

	public void setPushPlatform(Integer pushPlatform) {
		this.pushPlatform = pushPlatform;
	}

	public String getPlatformAppId() {
        return platformAppId;
    }

    public void setPlatformAppId(String platformAppId) {
        this.platformAppId = platformAppId;
    }

    public int getOsType() {
		return osType;
	}

	public void setOsType(int osType) {
		this.osType = osType;
	}

	public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public String getDeviceNickName() {
        return deviceNickName;
    }

    public void setDeviceNickName(String deviceNickName) {
        this.deviceNickName = deviceNickName == null ? null : deviceNickName.trim();
    }
    
}
