package model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//@Entity
//@Table(name = "t_order")
public class Order implements Serializable
{
	private static final long serialVersionUID = 3373981139157922935L;

	@Id
	private String orderID;

	private String orderName = "";

	@ManyToOne(cascade = CascadeType.ALL, optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "personID", nullable = false)
	private Person person = new Person();

	public String getOrderID()
	{
		return orderID;
	}

	public void setOrderID(String orderID)
	{
		this.orderID = orderID;
	}

	public String getOrderName()
	{
		return orderName;
	}

	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

	public Person getPerson()
	{
		return person;
	}

	public void setPerson(Person person)
	{
		this.person = person;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderID == null) ? 0 : orderID.hashCode());
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
		Order other = (Order) obj;
		if (orderID == null)
		{
			if (other.orderID != null)
				return false;
		}
		else if (!orderID.equals(other.orderID))
			return false;
		return true;
	}

}
