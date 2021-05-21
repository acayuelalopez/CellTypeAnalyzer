import java.awt.Container;

import javax.swing.AbstractButton;

/**
 * An interface for creating wizards.
 * 
 * @author Gustav Karlsson <gustav.karlsson@gmail.com>
 */
public interface Wizard {

	/**
	 * Gets the container where wizard pages are contained. This is used by the {@link WizardController} to (un)set
	 * wizard pages.
	 * 
	 * @return the {@link AbstractWizardPage} container
	 */
	Container getWizardPageContainer();
	
	
	AbstractButton getExportButton();
	
	
	
	AbstractButton getPrintButton();

	/**
	 * Gets the navigation button for "cancel". This is needed so that the {@link WizardController} can enable/disable
	 * the button.
	 * 
	 * @return the "cancel" button
	 */
	AbstractButton getFinishButton();

	/**
	 * Gets the navigation button for "previous". This is used by the {@link WizardController} to navigate back in the
	 * wizard.
	 * 
	 * @return the "previous" button
	 */
	AbstractButton getPreviousButton();

	/**
	 * Gets the navigation button for "next". This is used by the {@link WizardController} to navigate forward in the
	 * wizard. Also used by {@link WizardController} to enable/disable the button.
	 * 
	 * @return the "next" button
	 */
	AbstractButton getNextButton();

	/**
	 * Gets the navigation button for "finish". This is needed so that the {@link WizardController} can enable/disable
	 * the button.
	 * 
	 * @return the "finish" button
	 */


}