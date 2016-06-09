package interfaces;

import models.EAUser;

public interface ISecure {

	/** checks for valid info */
	public boolean CheckInfo(EAUser user);
}
