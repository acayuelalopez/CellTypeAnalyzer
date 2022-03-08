import java.awt.LayoutManager;

import javax.swing.JPanel;

import ij.ImagePlus;

/**
 * A page in a <code>Wizard</code>. Extends {@link JPanel}.
 * 
 * @author Gustav Karlsson <gustav.karlsson@gmail.com>
 */
public abstract class AbstractWizardPage extends JPanel {
	private static final long serialVersionUID = 1000143453163604518L;

	private WizardController wizardController;

	/**
	 * Creates a new <code>AbstractWizardPage</code> with the specified <code>LayoutManager</code> and buffering
	 * strategy.
	 * 
	 * @param layout
	 *            the <code>LayoutManager</code> to use
	 * @param isDoubleBuffered
	 *            true for double-buffering, which uses additional memory space to achieve fast, flicker-free updates
	 * @see JPanel
	 */
	public AbstractWizardPage(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/**
	 * Create a new double-buffered <code>AbstractWizardPage</code> with the specified <code>LayoutManager</code>
	 * 
	 * @param layout
	 *            the <code>LayoutManager</code> to use
	 * @see JPanel
	 */
	public AbstractWizardPage(LayoutManager layout) {
		super(layout);
	}

	/**
	 * Creates a new <code>AbstractWizardPage</code> with <code>FlowLayout</code> and the specified buffering strategy.
	 * 
	 * @param isDoubleBuffered
	 *            true for double-buffering, which uses additional memory space to achieve fast, flicker-free updates
	 * @see JPanel
	 */
	public AbstractWizardPage(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * Creates a new <code>AbstractWizardPage</code> with a double buffer and a <code>FlowLayout</code>.
	 * 
	 * @see JPanel
	 */
	public AbstractWizardPage() {
		super();
	}

	void setWizardController(WizardController wizardController) {
		this.wizardController = wizardController;
	}

	/**
	 * Updates the wizards navigation buttons.
	 */
	public void updateWizardButtons() {
		wizardController.updateButtons();
	}

	/**
	 * Gets the page that will be displayed when the user clicks the "next" navigation button in the wizard. <br />
	 * <br />
	 * <em>Note: Actually allowing going to the next page requires this method not returning <code>null</code> and
	 * {@link #isNextAllowed()} to return <code>true</code>.</em>
	 * 
	 * @return the next page of the wizard
	 */
	protected abstract AbstractWizardPage getNextPage();

	/**
	 * Gets if the page allows the wizard to cancel.
	 * 
	 * @return <code>true</code> if the page allows the wizard to cancel, otherwise <code>false</code>
	 */
	protected abstract boolean isExportAllowed();
	
	
	protected abstract boolean isPrintAllowed();
	
	
	
	protected abstract boolean isCancelAllowed();

	/**
	 * Gets if the page allows the wizard to go to the previous page.
	 * 
	 * @return <code>true</code> if the page allows the wizard to go to the previous page, otherwise <code>false</code>
	 */
	protected abstract boolean isPreviousAllowed();

	/**
	 * Gets if the page allows the wizard to go to the next page. <br />
	 * <br />
	 * <em>Note: Actually allowing going to the next page requires this method to return <code>true</code> and
	 * {@link #getNextPage()} not returning <code>null</code>.</em>
	 * 
	 * @return <code>true</code> if the page allows the wizard to go to the next page, otherwise <code>false</code>
	 */
	protected abstract boolean isNextAllowed();

	/**
	 * Gets if the page allows the wizard to finish.
	 * 
	 * @return <code>true</code> if the page allows the wizard to finish, otherwise <code>false</code>
	 */
	

	

}