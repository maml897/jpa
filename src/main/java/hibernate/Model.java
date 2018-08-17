package hibernate;

import java.io.Serializable;

public class Model implements Serializable
{
	private static final long serialVersionUID = 4107048616017439620L;

	private String modelID = "";

	private String modelName = "";

	public String getModelID()
	{
		return modelID;
	}

	public void setModelID(String modelID)
	{
		this.modelID = modelID;
	}

	public String getModelName()
	{
		return modelName;
	}

	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelID == null) ? 0 : modelID.hashCode());
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
		Model other = (Model) obj;
		if (modelID == null)
		{
			if (other.modelID != null)
				return false;
		}
		else if (!modelID.equals(other.modelID))
			return false;
		return true;
	}

}
