package model;

import java.io.Serializable;

public class Cat implements Serializable
{
	private static final long serialVersionUID = -6483356606972828205L;
	
	private String catID="";
	
	private String name="";

	public String getCatID()
	{
		return catID;
	}

	public void setCatID(String catID)
	{
		this.catID = catID;
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
		result = prime * result + ((catID == null) ? 0 : catID.hashCode());
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
		Cat other = (Cat) obj;
		if (catID == null)
		{
			if (other.catID != null)
				return false;
		}
		else if (!catID.equals(other.catID))
			return false;
		return true;
	}
	
	

}
