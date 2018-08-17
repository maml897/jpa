package n603;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Authority entity
 * @author lifw20081208
 */
//@Entity
//@Table(name = "t_authority")
public class Authority implements Serializable
{
	private static final long serialVersionUID = -2551034275758153899L;
	/**
	 * not assignable
	 */
	public static final String IS_ASSIGNED_NO = "0";
	/**
	 * assignable
	 */
	public static final String IS_ASSIGNED_YES = "1";
	
	/**
	 * id
	 */
	@Id
	@Column(name = "Authority", length = 50, nullable = false)
	private String authority = "";
	
	/**
	 * authorityName
	 */
	@Column(name = "AuthorityName", length = 50, nullable = false)
	private String authorityName = "";
	
	/**
	 * <h1>is assignable</h1>
	 * <br/>get list like "like" 
	 * <br/>{@link Authority#IS_ASSIGNED_NO}
	 * <br/>{@link Authority#IS_ASSIGNED_YES}
	 */
	@Column(name = "IsAssigned", length = 10, nullable = false)
	private String isAssigned = "";
	
	/**
	 * authority description
	 */
	@Column(name = "AuthorityDescription", length = 255, nullable = false)
	private String authorityDescription= " ";

	public String getAuthority()
	{
		return authority;
	}

	public void setAuthority(String authority)
	{
		this.authority = authority;
	}

	public String getAuthorityName()
	{
		return authorityName;
	}

	public void setAuthorityName(String authorityName)
	{
		this.authorityName = authorityName;
	}

	public String getIsAssigned()
	{
		return isAssigned;
	}

	public void setIsAssigned(String isAssigned)
	{
		this.isAssigned = isAssigned;
	}

	public String getAuthorityDescription()
	{
		return authorityDescription.trim();
	}

	public void setAuthorityDescription(String authorityDescription)
	{
		if(authorityDescription.equals(""))
		{
			authorityDescription = " ";
		}
		this.authorityDescription = authorityDescription;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
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
		Authority other = (Authority) obj;
		if (authority == null)
		{
			if (other.authority != null)
				return false;
		}
		else if (!authority.equals(other.authority))
			return false;
		return true;
	}
}
