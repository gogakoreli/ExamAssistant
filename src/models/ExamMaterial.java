package models;

import java.sql.ResultSet;

import data_managers.ExamManager;
import helper.LogManager;

public class ExamMaterial {

	private int materialID;
	private int examID;
	private String material;
	private String materialType;
	private int variant;
	private String location;

	public ExamMaterial(ResultSet rs) {
		if (rs != null) {
			try {
				if (rs.next()) {
					this.setMaterialID(rs.getInt("MaterialID"));
					this.setExamID(rs.getInt("ExamID"));
					this.setMaterial(rs.getString("Material"));
					this.setMaterialType(rs.getString("MaterialType"));
					this.setVariant(rs.getInt("Variant"));
					this.setLocation(rs.getString("Location"));
				} else {
					this.examID = ExamManager.NO_EXAM_ID;
				}
			} catch (Exception e) {
				LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
			}
		}
	}

	/**
	 * @return the materialID
	 */
	public int getMaterialID() {
		return materialID;
	}

	/**
	 * @param materialID
	 *            the materialID to set
	 */
	public void setMaterialID(int materialID) {
		this.materialID = materialID;
	}

	/**
	 * @return the examID
	 */
	public int getExamID() {
		return examID;
	}

	/**
	 * @param examID
	 *            the examID to set
	 */
	public void setExamID(int examID) {
		this.examID = examID;
	}

	/**
	 * @return the material
	 */
	public String getMaterial() {
		return material;
	}

	/**
	 * @param material
	 *            the material to set
	 */
	public void setMaterial(String material) {
		this.material = material;
	}

	/**
	 * @return the materialType
	 */
	public String getMaterialType() {
		return materialType;
	}

	/**
	 * @param materialType
	 *            the materialType to set
	 */
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	/**
	 * @return the variant
	 */
	public int getVariant() {
		return variant;
	}

	/**
	 * @param variant
	 *            the variant to set
	 */
	public void setVariant(int variant) {
		this.variant = variant;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

}
