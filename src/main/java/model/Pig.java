package model;

import java.io.Serializable;

public class Pig implements Serializable
{
	private static final long serialVersionUID = 1609299929980306808L;

	private String pigId = "";

	private String name = "";

	public String getPigId()
	{
		return pigId;
	}

	public void setPigId(String pigId)
	{
		this.pigId = pigId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pigId == null) ? 0 : pigId.hashCode());
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
		Pig other = (Pig) obj;
		if (pigId == null)
		{
			if (other.pigId != null)
				return false;
		}
		else if (!pigId.equals(other.pigId))
			return false;
		return true;
	}

}
