package common.file;

/**
 * Program Name 	: FileInfo
 * Description 		:
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 */

public class FileInfo {

	private String fileNo;
	private String fileName;
	private String orgFileName;
	private String fileSize;
	private String path;
	private String remark;
	private String userId;
	private String ip;
	private String type;
	/* 특정 그룹내 순서 */
	private int    index;

	public String getFileNo() {
		return fileNo;
	}
	public FileInfo setFileNo(String fileNo) {
		this.fileNo = fileNo;
		return this;
	}
	public String getRemark() {
		return remark;
	}
	public FileInfo setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getUserId() {
		return userId;
	}
	public FileInfo setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	public String getIp() {
		return ip;
	}
	public FileInfo setIp(String ip) {
		this.ip = ip;
		return this;
	}
	public String getFileSize() {
		return fileSize;
	}
	public FileInfo setFileSize(String fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	public String getPath() {
		return path;
	}
	public FileInfo setPath(String path) {
		this.path = path;
		return this;
	}
	public String getFileName() {
		return fileName;
	}
	public FileInfo setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	public String getOrgFileName() {
		return orgFileName;
	}
	public FileInfo setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
		return this;
	}
    public String getType() {
        return type;
    }
    public FileInfo setType(String type) {
        this.type = type;
        return this;
    }
    public int getIndex() {
        return index;
    }
    public FileInfo setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {

        return "FileInfo [fileNo=" + fileNo
                + ", fileName=" + fileName
                + ", orgFiileName=" + orgFileName
                + ", fileSize=" + fileSize
                + ", path=" + path
                + ", remark=" + remark
                + ", userId=" + userId
                + ", ip=" + ip
                + ", type=" + type
                + ", index=" + index
                + "]";
    }

}
