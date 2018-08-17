package model;

import java.io.Serializable;

public class Dog implements Serializable
{

	private static final long serialVersionUID = -4246226573510594527L;

	private String dogID = "";

	private String name = "";

	public String getDogID()
	{
		return dogID;
	}

	public void setDogID(String dogID)
	{
		this.dogID = dogID;
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
		result = prime * result + ((dogID == null) ? 0 : dogID.hashCode());
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
		Dog other = (Dog) obj;
		if (dogID == null)
		{
			if (other.dogID != null)
				return false;
		}
		else if (!dogID.equals(other.dogID))
			return false;
		return true;
	}

}
