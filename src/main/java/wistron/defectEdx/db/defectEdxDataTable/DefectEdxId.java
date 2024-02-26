package wistron.defectEdx.db.defectEdxDataTable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import wistron.defectEdx.util.date.DateUtils;

@Embeddable
@Data
public class DefectEdxId implements Serializable{
	private static final long serialVersionUID = 5027504769555993591L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSPECTION_TIME")
	private Date inspectionTime;
	@Column(name = "WAFER_KEY")
	private Integer waferKey;
	@Column(name = "DEFECT_ID")
	private Integer defectId;
	@Column(name = "IMAGE_FILESPEC")
	private String imageFileSpec;

	
	
	public DefectEdxId() {}

	public DefectEdxId(Date inspectionTime, Integer waferKey, Integer defectId, String imageFileSpec) {
		this.inspectionTime = inspectionTime;
		this.waferKey = waferKey;
		this.defectId = defectId;
		this.imageFileSpec = imageFileSpec;
	}
	
	public LocalDateTime inspectionTimeToLocalDateTime() {
		return DateUtils.datetoLocalDateTime(inspectionTime);
	}
	
}
