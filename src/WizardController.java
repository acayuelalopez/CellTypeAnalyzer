import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import javax.swing.AbstractButton;

public class WizardController {

	private final Wizard wizard;
	private final Stack<AbstractWizardPage> pageHistory = new Stack<AbstractWizardPage>();
	private AbstractWizardPage currentPage = null;

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

	public AbstractWizardPage getCurrentPage() {
		return currentPage;
	}

	public List<AbstractWizardPage> getPageHistoryList() {
		return new ArrayList<AbstractWizardPage>(pageHistory);
	}

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