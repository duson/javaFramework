package duson.java.persistence.mongodb.sample.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;

@CompoundIndexes({  
    @CompoundIndex(name = "systemId_userId", def = "{'systemId': 1, 'userId': 1}")  
}) 
public class Device {
	@Id
    private ObjectId deviceId;

	private Long systemId;
	
    private Long userId;
    
    private String userName;

    private Short statusCode;

    private String comments;

    private Long createdUserId;

    private Date createdDtm;

    private Long lastUserId;

    private Date lastDtm;
    
    @DBRef(lazy=true)
    private List<Tag> tags = new ArrayList<Tag>();

    private List<DevicePlatform> platforms = new ArrayList<DevicePlatform>();

    public ObjectId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(ObjectId deviceId) {
        this.deviceId = deviceId;
    }

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

	public Short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Short statusCode) {
        this.statusCode = statusCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Date getCreatedDtm() {
        return createdDtm;
    }

    public void setCreatedDtm(Date createdDtm) {
        this.createdDtm = createdDtm;
    }

    public Long getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(Long lastUserId) {
        this.lastUserId = lastUserId;
    }

    public Date getLastDtm() {
        return lastDtm;
    }

    public void setLastDtm(Date lastDtm) {
        this.lastDtm = lastDtm;
    }

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<DevicePlatform> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<DevicePlatform> platforms) {
		this.platforms = platforms;
	}
}