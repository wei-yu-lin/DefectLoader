package wistron.defectEdx.db.defectEdxDataTable;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@MappedSuperclass
@Data
public class InspToDefectData{ 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATE")
	public Date lastUpdate;
	
	@Column(name = "FAB_ID")
	public String fabId;
	
	@Column(name = "LOT_ID")
	public String lotId;

	@Column(name = "WAFER_ID")
	public String waferId;
	
	@Column(name = "COMPONENT_ID")
	public String componentId;
	
	@Column(name = "SLOT_ID")
	public Integer slotId;
	
	@Column(name = "DEVICE")
	public String device;
	
	@Column(name = "TECHNOLOGY")
	public String technology;

	@Column(name = "LAYER_ID")
	public String layerId;
	
	@Column(name = "INSPECT_EQUIP_ID")
	 public String inspectEquipId;
	
	@Column(name = "PROCESS_EQUIP_ID")
	public String processEquipId;
	
	@Column(name = "CENTER_X")
	public Integer centerX;
	
	@Column(name = "CENTER_Y")
	public Integer centerY;
	
	@Column(name = "ORIGIN_X")
	public Integer originX;
	
	@Column(name = "ORIGIN_Y")
	public Integer originY;
	
	@Column(name = "CLASS_NUMBER")
	public Integer classNumber;
	
	@Column(name = "INDEX_X")
	public Integer indexX;
	
	@Column(name = "INDEX_Y")
	public Integer indexY;
	
	@Column(name = "WAFER_X")
	public Integer waferX;
	
	@Column(name = "WAFER_Y")
	public Integer waferY;
	
	@Column(name = "SIZE_X")
	public Integer sizeX;
	
	@Column(name = "SIZE_Y")
	public Integer sizeY;
	
	@Column(name = "SIZE_D")
	public Integer sizeD;
	
	@Column(name = "REVIEW_EQUIP_ID")
	public String reviewEquipId;
	
	@Column(name = "TOOL_KEY")
	public Integer toolKey;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESULT_FILETIMESTAMP")
	public Date resultFiletimestamp;
	
	@Column(name = "IMAGE_TYPE")
	public String imageType;
	
	
	@Column(name = "EDX_IMAGE")
	public String edxImage;
}
