package n21;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Forum
{
	
	@Id
	private String forumID="";
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name = "groupId", nullable = false)
	private ForumGroup group;

	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	public ForumGroup getGroup()
	{
		return group;
	}

	public void setGroup(ForumGroup group)
	{
		this.group = group;
	}

	public String getForumID()
	{
		return forumID;
	}

	public void setForumID(String forumID)
	{
		this.forumID = forumID;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forumID == null) ? 0 : forumID.hashCode());
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
		Forum other = (Forum) obj;
		if (forumID == null)
		{
			if (other.forumID != null)
				return false;
		}
		else if (!forumID.equals(other.forumID))
			return false;
		return true;
	}

}