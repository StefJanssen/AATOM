package model.environment.objects.physicalObject;

/**
 * Indicates that environment objects can be opened or closed.
 * 
 * @author S.A.M. Janssen
 */
public interface Openable {

	/**
	 * Is open or not.
	 * 
	 * @return True if open, false if closed.
	 */
	public boolean isOpen();
	
	/**
	 * Sets open.
	 * 
	 * @param open
	 *            Open or not.
	 */
	public void setOpen(boolean open);
}
