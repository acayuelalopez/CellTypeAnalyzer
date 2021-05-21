import java.io.File;
import java.text.Normalizer;

import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ij.IJ;

public class CreateXMLFile {

	public String xmlFilePath;
	public int selectedRow;

	public CreateXMLFile(String xmlFilePath, int selectedRow) {

		this.xmlFilePath = xmlFilePath;
		this.selectedRow = selectedRow;

	}

	public void exportXMLAction() {

		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			// root element
			Element root = document.createElement("analysisparameters");
			document.appendChild(root);

			// Channel_w1
			Element channel1 = document.createElement("channelonecellcount");

			root.appendChild(channel1);

			// set an attribute to staff element
			Attr attr = document.createAttribute(StartPageModified.comboCh1.getSelectedItem().toString());
			attr.setValue(StartPageModified.tfCh1.getText());
			channel1.setAttributeNode(attr);

			// you can also use staff.setAttribute("id", "1") for this

			// firstname element
			Element threshold1 = document.createElement("thresholdmethod");
			threshold1.appendChild(
					document.createTextNode(StartPageModified.thresholdMeths.getSelectedItem().toString()));
			channel1.appendChild(threshold1);

			// lastname element
			Element filter1 = document.createElement("filterparameter");
			for (int i = 0; i < StartPageModified.filterList.getModel().getSize(); i++)
				filter1.appendChild(
						document.createTextNode(String
								.valueOf(StartPageModified.filterList.getModel().getElementAt(i).substring(0,
										StartPageModified.filterList.getModel().getElementAt(i).lastIndexOf(":")))
								+ ","));
			channel1.appendChild(filter1);

			// email element
			Element minValue1 = document.createElement("minvalue");
			for (int i = 0; i < StartPageModified.filterList.getModel().getSize(); i++)
				minValue1.appendChild(document.createTextNode(String
						.valueOf(StartPageModified.filterList.getModel().getElementAt(i).substring(
								StartPageModified.filterList.getModel().getElementAt(i).lastIndexOf("["),
								StartPageModified.filterList.getModel().getElementAt(i).lastIndexOf(",")))
						.replace("[", "") + ","));
			channel1.appendChild(minValue1);

			// department elements
			Element maxValue1 = document.createElement("maxvalue");
			for (int i = 0; i < StartPageModified.filterList.getModel().getSize(); i++)
				maxValue1.appendChild(document.createTextNode(String
						.valueOf(StartPageModified.filterList.getModel().getElementAt(i).substring(
								StartPageModified.filterList.getModel().getElementAt(i).lastIndexOf(","),
								StartPageModified.filterList.getModel().getElementAt(i).lastIndexOf("]")))
						.replace(",", "") + ","));
			channel1.appendChild(maxValue1);

			// employee element
			Element channel2 = document.createElement("channeltwoanalysis");

			root.appendChild(channel2);

			// set an attribute to staff element
			Attr attr2 = document.createAttribute(StartPageModified.comboCh2.getSelectedItem().toString());
			attr2.setValue(StartPageModified.tfCh2.getText());
			channel2.setAttributeNode(attr2);

			// you can also use staff.setAttribute("id", "1") for this

			// firstname element
			Element morphoName2 = document.createElement("morphooperatorname");
			if (MiddlePageModified.Erosion.isSelected() == Boolean.TRUE)
				morphoName2.appendChild(document.createTextNode("Erosion"));
			if (MiddlePageModified.Dilation.isSelected() == Boolean.TRUE)
				morphoName2.appendChild(document.createTextNode("Dilation"));
			channel2.appendChild(morphoName2);

			// lastname element
			Element morphoValue2 = document.createElement("morphooperatorvalue");
			morphoValue2.appendChild(document.createTextNode(MiddlePageModified.filterMorpho.getValue().toString()));
			channel2.appendChild(morphoValue2);

			// email element
			Element className2 = document.createElement("classname");
			className2.appendChild(document.createTextNode(((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(
					ColorEditorEnd.tableC.convertRowIndexToModel(selectedRow),
					ColorEditorEnd.tableC.convertColumnIndexToModel(0))).getText() + ","));
			channel2.appendChild(className2);

			// department elements
			Element classColor2 = document.createElement("classcolor");
			classColor2.appendChild(document.createTextNode(((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(
					ColorEditorEnd.tableC.convertRowIndexToModel(selectedRow),
					ColorEditorEnd.tableC.convertColumnIndexToModel(1))).getBackground().toString() + ","));
			channel2.appendChild(classColor2);
			// department elements
			Element classParameters2 = document.createElement("classparameters");
			String inputParameter = null;
			inputParameter = ((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(
					ColorEditorEnd.tableC.convertRowIndexToModel(selectedRow),
					ColorEditorEnd.tableC.convertColumnIndexToModel(2))).getText().toString().replace("<html>", "")
							.replace("<br>", "").replace("</html>", "");
			Element classMinValues2 = document.createElement("classminvalues");
			Element classMaxValues2 = document.createElement("classmaxvalues");
			String[] splitParameter = inputParameter.split(";");
			for (int i = 0; i < splitParameter.length; i++) {
				if (splitParameter[i].contains(StartPageModified.tfCh2.getText()) == Boolean.TRUE) {
					classParameters2.appendChild(document.createTextNode(
							splitParameter[i].substring(0, splitParameter[i].indexOf(":")) + "-" + i + ","));
					classMinValues2.appendChild(
							document.createTextNode(splitParameter[i].substring(splitParameter[i].indexOf("[") + 1,
									splitParameter[i].indexOf(",")) + "-" + i + ","));
					classMaxValues2.appendChild(
							document.createTextNode(splitParameter[i].substring(splitParameter[i].indexOf(",") + 1,
									splitParameter[i].indexOf("]")) + "-" + i + ","));
				}
			}
			channel2.appendChild(classParameters2);
			channel2.appendChild(classMinValues2);
			channel2.appendChild(classMaxValues2);

			// employee element
			Element channel3 = document.createElement("channelthreeanalysis");

			root.appendChild(channel3);

			// set an attribute to staff element
			Attr attr3 = document.createAttribute(StartPageModified.comboCh3.getSelectedItem().toString());
			attr3.setValue(StartPageModified.tfCh3.getText());
			channel3.setAttributeNode(attr3);

			// you can also use staff.setAttribute("id", "1") for this

			// firstname element
			Element morphoName3 = document.createElement("morphooperatorname");
			if (MiddlePageModified.Erosion.isSelected() == Boolean.TRUE)
				morphoName3.appendChild(document.createTextNode("Erosion"));
			if (MiddlePageModified.Dilation.isSelected() == Boolean.TRUE)
				morphoName3.appendChild(document.createTextNode("Dilation"));
			channel3.appendChild(morphoName3);

			// lastname element
			Element morphoValue3 = document.createElement("morphooperatorvalue");
			morphoValue3.appendChild(document.createTextNode(MiddlePageModified.filterMorpho.getValue().toString()));
			channel3.appendChild(morphoValue3);

			// email element
			Element className3 = document.createElement("classname");
			className3.appendChild(document.createTextNode(((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(
					ColorEditorEnd.tableC.convertRowIndexToModel(selectedRow),
					ColorEditorEnd.tableC.convertColumnIndexToModel(0))).getText() + ","));
			channel3.appendChild(className3);

			// department elements
			Element classColor3 = document.createElement("classcolor");
			classColor3.appendChild(document.createTextNode(((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(
					ColorEditorEnd.tableC.convertRowIndexToModel(selectedRow),
					ColorEditorEnd.tableC.convertColumnIndexToModel(1))).getBackground().toString() + ","));
			channel3.appendChild(classColor3);
			// department elements
			Element classParameters3 = document.createElement("classparameters");
			Element classMinValues3 = document.createElement("classminvalues");
			Element classMaxValues3 = document.createElement("classmaxvalues");

			for (int i = 0; i < splitParameter.length; i++) {
				if (splitParameter[i].contains(StartPageModified.tfCh3.getText()) == Boolean.TRUE) {
					classParameters3.appendChild(document.createTextNode(
							splitParameter[i].substring(0, splitParameter[i].indexOf(":")) + "-" + i + ","));
					classMinValues3.appendChild(
							document.createTextNode(splitParameter[i].substring(splitParameter[i].indexOf("[") + 1,
									splitParameter[i].indexOf(",")) + "-" + i + ","));
					classMaxValues3.appendChild(
							document.createTextNode(splitParameter[i].substring(splitParameter[i].indexOf(",") + 1,
									splitParameter[i].indexOf("]")) + "-" + i + ","));
				}
			}
			channel3.appendChild(classParameters3);
			channel3.appendChild(classMinValues3);
			channel3.appendChild(classMaxValues3);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(
					new File(xmlFilePath, "Parameters_file.xml").getAbsolutePath());
			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
