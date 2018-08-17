package n21;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//@Entity
public class ForumGroup
{
	@Id
	private String forumGroupID = "";

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "group", cascade = CascadeType.ALL)
	private List<Forum> forums = new ArrayList<Forum>();

	public List<Forum> getForums()
	{
		return forums;
	}

	public void setForums(List<Forum> forums)
	{
		this.forums = forums;
	}

	public String getForumGroupID()
	{
		return forumGroupID;
	}

	public void setForumGroupID(String forumGroupID)
	{
		this.forumGroupID = forumGroupID;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forumGroupID == null) ? 0 : forumGroupID.hashCode());
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
		ForumGroup other = (ForumGroup) obj;
		if (forumGroupID == null)
		{
			if (other.forumGroupID != null)
				return false;
		}
		else if (!forumGroupID.equals(other.forumGroupID))
			return false;
		return true;
	}

}