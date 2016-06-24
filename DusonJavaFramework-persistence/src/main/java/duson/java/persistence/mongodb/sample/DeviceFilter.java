package duson.java.persistence.mongodb.sample;

import java.io.Serializable;

public class DeviceFilter implements Serializable {
    private Long systemId;
    
    private Long userId;
    
    private String userName;

    private String platformAppId;

    private Integer osType; // 0：android，1：ios

    private Integer pushPlatform; // 0：百度推送，1：腾讯信鸽，2：小米推送
    
    private String tagId;
    
    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlatformAppId() {
        return platformAppId;
    }

    public void setPlatformAppId(String platformAppId) {
        this.platformAppId = platformAppId == null ? null : platformAppId.trim();
    }

    public Integer getOsType() {
        return osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public Integer getPushPlatform() {
        return pushPlatform;
    }

    public void setPushPlatform(Integer pushPlatform) {
        this.pushPlatform = pushPlatform;
    }

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
}
