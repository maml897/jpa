package model;

import java.io.Serializable;

public class Cow implements Serializable
{

	private static final long serialVersionUID = 3681462545531665552L;

	private String cowID = "";

	private String name = "";

	public String getCowID()
	{
		return cowID;
	}

	public void setCowID(String cowID)
	{
		this.cowID = cowID;
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
		result = prime * result + ((cowID == null) ? 0 : cowID.hashCode());
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
		Cow other = (Cow) obj;
		if (cowID == null)
		{
			if (other.cowID != null)
				return false;
		}
		else if (!cowID.equals(other.cowID))
			return false;
		return true;
	}

}
