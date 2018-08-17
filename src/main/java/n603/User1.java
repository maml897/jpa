package n603;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * user entity
 */
//@Entity
//@Table(name = "t_user")
public class User1 implements Serializable
{
	private static final long serialVersionUID = 2890634511226110829L;

	@Id
	@Column(name = "UserID", length = 36, nullable = false)
	private String userID = "";

	/**
	 * after delete this user，this column will set to GUID
	 */
	@Column(name = "UserName", length = 40, nullable = false, unique = true)
	private String userName = "";

	@Column(name = "UserPassword", length = 40, nullable = false)
	private String userPassword = "";

	/**
	 * Real Name
	 */
	@Column(name = "RealName", length = 40, nullable = false)
	private String realName = "";

	/**
	 * default is true
	 */
	@Column(name = "Enabled", nullable = false)
	private Boolean enabled = true;

	/**
	 * Authority list
	 */
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "t_user_authority", joinColumns = { @JoinColumn(name = "UserID", referencedColumnName = "UserID") }, inverseJoinColumns = { @JoinColumn(name = "Authority", referencedColumnName = "Authority") })
	private List<Authority> authorities = new ArrayList<Authority>();
	
	public List<Authority> getAuthorities()
	{
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities)
	{
		this.authorities = authorities;
	}

	public void addAuthority(Authority authority)
	{
		if (!authorities.contains(authority))
		{
			authorities.add(authority);
		}
	}

	public void removeAuthority(Authority authority)
	{
		if (authorities.contains(authority))
		{
			authorities.remove(authority);
		}
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserPassword()
	{
		return userPassword;
	}

	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public Boolean getEnabled()
	{
		return enabled;
	}

	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		User1 other = (User1) obj;
		if (userID == null)
		{
			if (other.userID != null)
				return false;
		}
		else if (!userID.equals(other.userID))
			return false;
		return true;
	}
}
