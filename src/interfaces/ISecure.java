package interfaces;

import javax.servlet.http.HttpServletRequest;

import models.EAUser;

public interface ISecure {

	/** checks for valid info */
	public boolean CheckInfo(EAUser user, HttpServletRequest request);
}
