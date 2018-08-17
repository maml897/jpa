package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries( { @NamedQuery(name = "getPerson", query = "select p from Person p where p.personID=?1"),
		@NamedQuery(name = "getPersonList", query = "select p from Person p"), })
@Entity
public class Person implements Serializable
{
	private static final long serialVersionUID = -1677511249163130654L;

	@Id
	private Integer personID;

	private String personName;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
//	private List<Order> orders =new ArrayList<Order>();

	public Integer getPersonID()
	{
		return personID;
	}

	public void setPersonID(Integer personID)
	{
		this.personID = personID;
	}

	public String getPersonName()
	{
		return personName;
	}

	public void setPersonName(String personName)
	{
		this.personName = personName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((personID == null) ? 0 : personID.hashCode());
		result = prime * result + ((personName == null) ? 0 : personName.hashCode());
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
		Person other = (Person) obj;
		if (personID == null)
		{
			if (other.personID != null)
				return false;
		}
		else if (!personID.equals(other.personID))
			return false;
		if (personName == null)
		{
			if (other.personName != null)
				return false;
		}
		else if (!personName.equals(other.personName))
			return false;
		return true;
	}

	public Person(Integer personID)
	{
		this.personID = personID;
	}

	public Person()
	{
		super();
		// TODO Auto-generated constructor stub
	}

//	public List<Order> getOrders()
//	{
//		return orders;
//	}
//
//	public void setOrders(List<Order> orders)
//	{
//		this.orders = orders;
//	}

}
