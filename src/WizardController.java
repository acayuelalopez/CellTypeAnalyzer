import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import javax.swing.AbstractButton;

/**
 * A controller for a {@link Wizard}. Used to control navigation, setting the
 * correct {@link AbstractWizardPage}, and keeping tack of history.
 * 
 * @author Gustav Karlsson <gustav.karlsson@gmail.com>
 */
public class WizardController {

	private final Wizard wizard;
	private final Stack<AbstractWizardPage> pageHistory = new Stack<AbstractWizardPage>();
	private AbstractWizardPage currentPage = null;

	/**
	 * Creates a wizard controller for a wizard.
	 * 
	 * @param wizard the wizard that this controller controls
	 */
	public WizardController(Wizard wizard) {
		if (wizard == null) {
			throw new IllegalArgumentException("wizard can't be null");
		}
		this.wizard = wizard;
		setupNavigationButtons();
	}

	private void setupNavigationButtons() {
		wizard.getNextButton().addActionListener(new NextPageListener());
		wizard.getPreviousButton().addActionListener(new PreviousPageListener());
	}

	/**
	 * Sets the current page of this wizard to the specified page and adds the
	 * previous "current page" to the history.
	 * 
	 * @param nextPage the page to set as the current page
	 * @return <code>true</code> if the current page was changed, otherwise
	 *         <code>false</code>
	 */
	public boolean showNextPage(AbstractWizardPage nextPage) {
		if (nextPage == null) {
			// Next page is null. Updating buttons and ignoring request.
			updateButtons();
			return false;
		}
		if (currentPage != null) {
			pageHistory.push(currentPage);
		}
		setPage(nextPage);
		return true;
	}

	/**
	 * Sets the current page of this wizard to the previous page (if one exists) and
	 * removes it from the history.
	 * 
	 * @return <code>true</code> if the current page was changed, otherwise
	 *         <code>false</code>
	 */
	public boolean showPreviousPage() {
		AbstractWizardPage previousPage;
		try {
			previousPage = pageHistory.pop();
		} catch (EmptyStackException e) {
			// Previous page is null. Updating buttons and ignoring request.
			updateButtons();
			return false;
		}
		setPage(previousPage);
		return true;
	}

	private void setPage(AbstractWizardPage newPage) {
		Container wizardPageContainer = wizard.getWizardPageContainer();
		if (currentPage != null) {
			wizardPageContainer.remove(currentPage);
		}
		currentPage = newPage;
		currentPage.setWizardController(this);
		wizardPageContainer.add(currentPage);
		wizardPageContainer.validate();
		wizardPageContainer.repaint();
		updateButtons();
	}

	/**
	 * Starts (or restarts) the wizard with the given start page.
	 * 
	 * @param startPage the page to start (or restart) the wizard with
	 */
	public void startWizard(AbstractWizardPage startPage) {
		if (startPage == null) {
			throw new IllegalArgumentException("startPage can't be null");
		}
		if (currentPage != null) {
			wizard.getWizardPageContainer().remove(currentPage);
			pageHistory.clear();
			currentPage = null;
		}
		showNextPage(startPage);
	}

	/**
	 * Gets the current page of the wizard.
	 * 
	 * @return the current page of the wizard
	 */
	public AbstractWizardPage getCurrentPage() {
		return currentPage;
	}

	/**
	 * Gets a list of the pages in the page history. The list does not contain the
	 * current page. Modifications to the list does not modify the underlying page
	 * history.
	 * 
	 * @return the page history as a {@link List}
	 */
	public List<AbstractWizardPage> getPageHistoryList() {
		return new ArrayList<AbstractWizardPage>(pageHistory);
	}

	/**
	 * Enables/disables the "cancel", "next", "previous", and "finish" buttons of
	 * the current page based on the current state of pages and the wizard.
	 */
	public void updateButtons() {
		AbstractButton finishButton = wizard.getFinishButton();
		if (finishButton != null) {
			finishButton.setEnabled(currentPage.isCancelAllowed());
		}

		AbstractButton exportButton = wizard.getExportButton();
		if (exportButton != null) {
			exportButton.setEnabled((currentPage.getNextPage() == null));
		}
		AbstractButton printButton = wizard.getPrintButton();
		if (printButton != null) {
			printButton.setEnabled((currentPage.getNextPage() == null));
		}

		AbstractButton previousButton = wizard.getPreviousButton();
		if (previousButton != null) {
			previousButton.setEnabled(currentPage.isPreviousAllowed() && !pageHistory.isEmpty());
		}
		AbstractButton nextButton = wizard.getNextButton();
		if (nextButton != null) {
			nextButton.setEnabled(currentPage.isNextAllowed() && (currentPage.getNextPage() != null));
		}

	}

	private class NextPageListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showNextPage(currentPage.getNextPage());
		}
	}

	private class PreviousPageListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showPreviousPage();
		}
	}

}