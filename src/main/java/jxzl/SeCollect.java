package jxzl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "s_secollect")
public class SeCollect implements Serializable
{
	private static final long serialVersionUID = 3038721464902595725L;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private long id = 0;

	@Column(name = "FileName", length = 60, nullable = false)
	private String fileName = "";

	@Column(name = "SeID", nullable = false)
	private long seID = 0;

	@Column(name = "AreaID", nullable = false)
	private long areaID = 0;
	
	@Column(name = "FilePath", length = 120, nullable = false)
	private String filePath = "";

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public long getSeID()
	{
		return seID;
	}

	public void setSeID(long seID)
	{
		this.seID = seID;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeCollect other = (SeCollect) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getAreaID()
	{
		return areaID;
	}

	public void setAreaID(long areaID)
	{
		this.areaID = areaID;
	}

}
