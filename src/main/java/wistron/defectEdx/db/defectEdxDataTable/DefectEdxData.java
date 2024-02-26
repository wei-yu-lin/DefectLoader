package wistron.defectEdx.db.defectEdxDataTable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@Table (name = "DEFECT_EDX_DATA")
@NoArgsConstructor
public class DefectEdxData extends InspToDefectData implements Serializable { 
	
	private static final long serialVersionUID = 594277961414434808L;
	
	@EmbeddedId
	private DefectEdxId defectEdxId;

	@Column(name = "IMAGE_FILE_FTP_PATH")
	private String imageFileFtpPath;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOADER_UPDATE_TIME")
	private Date loaderUpdateTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TABLE_CREATE_TIME")
	private Date tableCreateTime;
}
