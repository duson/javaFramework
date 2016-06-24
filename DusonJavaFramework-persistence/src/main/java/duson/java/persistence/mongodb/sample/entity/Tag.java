package duson.java.persistence.mongodb.sample.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Tag {
	@Id
    private ObjectId tagId;
	
    private Long systemId;

    private String tagName;

    private Short statusCode;

    private String comments;

    private Long createdUserId;

    private Date createdDtm;

    private Long lastUserId;

    private Date lastDtm;

    @DBRef(lazy=true)
    private List<Device> devices = new ArrayList<Device>();

    public ObjectId getTagId() {
		return tagId;
	}

	public void setTagId(ObjectId tagId) {
		this.tagId = tagId;
	}

	public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
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

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
}