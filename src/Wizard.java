import java.awt.Container;

import javax.swing.AbstractButton;

public interface Wizard {

	Container getWizardPageContainer();

	AbstractButton getExportButton();

	AbstractButton getPrintButton();

	AbstractButton getFinishButton();

	AbstractButton getPreviousButton();

	AbstractButton getNextButton();

}