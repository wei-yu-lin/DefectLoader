package wistron.defectEdx.db.defectEdxDataTable;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//import org.springframework.data.annotation.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DEFECT_EDX_PRE_DATA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectEdxPreData implements Serializable {

	private static final long serialVersionUID = 174563534456L;

	@Id
	@Column(name = "ROWID")
	private String rowid;

	@Column(name = "WAFER_KEY")
	private Integer waferKey;

	@Column(name = "IMAGE_TYPE")
	private String imageType ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESULT_FILETIMESTAMP")
	private Date resultFileTimeStamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSPECTION_TIME")
	private Date inspectionTime;
	
	@Column(name = "DEFECT_ID")
	private Integer defectId;
	
	@Column(name = "REVIEW_EQUIP_ID")
	private String reviewEquipId;
	
	@Column(name = "EDX_IMAGE")
	private String edxImage;
	
	@Column(name = "TABLE_CREATE_TIME")
	private Date tableCreateTime;

	@Column(name = "IMAGE_FILESPEC")
	private String imageFileSpec;
	
	
	@Column(name = "CENTER_X")
	private String centerX;

}
