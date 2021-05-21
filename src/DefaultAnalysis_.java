import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.gui.ShapeRoi;
import ij.measure.ResultsTable;
import ij.plugin.ChannelSplitter;
import ij.plugin.RoiEnlarger;
import ij.plugin.filter.MaximumFinder;
import ij.plugin.filter.RankFilters;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;

public class DefaultAnalysis_ {
	String[] imageTitles;
	List<String> roisNames;
	double[] counterTable0List, classCounterDef2L, counterPositiveDefL, classCounterDefL;
	JTable table0, table1, table2;
	Double[][] data1, data2, data3;
	Object[][] tableData1, tableData2, tableData3;

	public void run(String textSave, String textImages, File[] listOfFiles) {

		File directFolder = new File(textSave + File.separator + "Analysis_Summary");
		if (!directFolder.exists()) {
			boolean var76 = false;

			try {
				directFolder.mkdir();
				var76 = true;
			} catch (SecurityException var67) {
			}
		}
		counterTable0List = new double[listOfFiles.length];
		classCounterDefL = new double[listOfFiles.length];
		classCounterDef2L = new double[listOfFiles.length];
		counterPositiveDefL = new double[listOfFiles.length];
		RoiManager rm = new RoiManager();
		for (int ix = 0; ix < listOfFiles.length; ++ix) {

			File directFolderImage = new File(textSave + File.separator + listOfFiles[ix].getName());
			if (!directFolderImage.exists()) {
				boolean var76 = false;

				try {
					directFolderImage.mkdir();
					var76 = true;
				} catch (SecurityException var67) {
				}
			}
			imageTitles = new String[listOfFiles.length];
			if (listOfFiles[ix].isFile())
				imageTitles[ix] = listOfFiles[ix].getName();

			ImagePlus imp = new ImagePlus(textImages + File.separator + imageTitles[ix]);
			ImagePlus[] channels = ChannelSplitter.split(imp);
			ImagePlus channelAnalCellCount = null;

			channelAnalCellCount = channels[2];
			ImagePlus impSeg = channelAnalCellCount.duplicate();
			ImageProcessor impSegP = impSeg.getProcessor();
			RankFilters rf = new RankFilters();
			rf.rank(impSegP, 2.0, RankFilters.MEAN);
			rf.rank(impSegP, 2.0, RankFilters.MEDIAN);
			impSeg = new ImagePlus(impSeg.getTitle(), impSegP);
			//IJ.run(impSeg, "Auto Threshold", "method=Intermodes ignore_black white");
			//IJ.run(impSeg, "Auto Threshold", "method=Default ignore_black white");
			IJ.run(impSeg, "Auto Threshold", "method=Minimum ignore_black white");
			IJ.run(impSeg, "Create Selection", "");
			if (rm != null)
				rm.reset();
			Roi mainRoi = impSeg.getRoi();
			Roi[] totalRois = new ShapeRoi(mainRoi).getRois();
			if (rm.getCount() != 0)
				rm.reset();
			rm = RoiManager.getInstance();
			for (int j = 0; j < totalRois.length; j++)
				if (totalRois[j].getStatistics().area > 35)
					rm.addRoi(totalRois[j]);
			// IJ.run(impSeg, "Analyze Particles...", "size=50-2147483647 exclude clear
			// add");
			rm.runCommand(channelAnalCellCount, "Measure");
			Roi[] roisInitial = rm.getRoisAsArray();
			roisNames = new ArrayList();

			for (int jxxxx = 0; jxxxx < rm.getCount(); ++jxxxx) {
				roisNames.add(rm.getName(jxxxx));
			}

			ResultsTable rt0 = ResultsTable.getResultsTable();
			String[] columnHeaders0 = rt0.getHeadings();
			List<String> columnHeadersS0 = new ArrayList();

			for (int r = 0; r < columnHeaders0.length; ++r) {
				columnHeadersS0.add(columnHeaders0[r]);
			}

			String[] columnHeadersSS0 = new String[columnHeaders0.length];
			columnHeadersS0.toArray(columnHeadersSS0);
			int rows0 = rt0.size();
			List<List<String>> dataList0 = new ArrayList();

			int jxxxxx;
			for (int rx = 0; rx < rows0; ++rx) {
				List<String> strings0 = new ArrayList();

				for (jxxxxx = 0; jxxxxx < columnHeaders0.length; ++jxxxxx) {
					String values0 = rt0.getStringValue(columnHeaders0[jxxxxx], rx);
					strings0.add(values0);
				}

				dataList0.add(strings0);
			}

			Double[][] data0 = new Double[dataList0.size()][];

			int j;
			for (j = 0; j < data0.length; ++j) {
				data0[j] = new Double[((List) dataList0.get(j)).size()];
			}

			for (j = 0; j < dataList0.size(); ++j) {
				for (jxxxxx = 1; jxxxxx < ((List) dataList0.get(j)).size(); ++jxxxxx) {
					data0[j][jxxxxx] = Double.parseDouble((String) ((List) dataList0.get(j)).get(jxxxxx));
				}
			}

			DefaultTableModel model0 = new DefaultTableModel(data0, columnHeaders0) {
				public Class<?> getColumnClass(int column) {
					if (this.getRowCount() > 0) {
						Object value = this.getValueAt(0, column);
						if (value != null) {
							return this.getValueAt(0, column).getClass();
						}
					}

					return super.getColumnClass(column);
				}
			};
			table0 = new JTable();
			table0.setModel(model0);

			for (jxxxxx = 0; jxxxxx < roisNames.size(); ++jxxxxx) {
				table0.setValueAt(roisNames.get(jxxxxx), jxxxxx, 0);
			}

			// ArrayList listMaxValue;
			int row;
			int x;
			Object[][] tableData = new Object[table0.getRowCount()][table0.getColumnCount()];

			int col;
			for (int jx = 0; jx < table0.getRowCount(); ++jx) {
				for (col = 0; col < table0.getColumnCount(); ++col) {
					tableData[jx][col] = table0.getValueAt(jx, col);
				}
			}

			try {
				rt0.saveAs(directFolderImage + File.separator + "TableData_DAPI" + ".xlsx");
			} catch (IOException var66) {
				var66.printStackTrace();
			}

			if (WindowManager.getFrame("Results") != null) {
				IJ.selectWindow("Results");
				IJ.run("Close");
			}

			int jxx;
			ArrayList roisEnlargered;

			roisEnlargered = null;

			rm.reset();
			roisEnlargered = new ArrayList();

			for (jxx = 0; jxx < roisInitial.length; ++jxx) {
				roisEnlargered.add(roisInitial[jxx]);
			}

			for (jxx = 0; jxx < roisEnlargered.size(); ++jxx) {
				rm.addRoi((Roi) roisEnlargered.get(jxx));
				rm.rename(jxx, (String) roisNames.get(jxx));
			}

			ImagePlus channelAnalysis1 = null;
			channelAnalysis1 = channels[1];

			rm.deselect();
			rm.runCommand(channelAnalysis1, "Measure");
			ResultsTable rt1 = ResultsTable.getResultsTable();
			String[] columnHeaders1 = rt1.getHeadings();
			List<String> columnHeadersS1 = new ArrayList();

			for (int rxx = 0; rxx < columnHeaders1.length; ++rxx) {
				columnHeadersS1.add(columnHeaders1[rxx]);
			}

			String[] columnHeadersSS1 = new String[columnHeaders1.length];
			columnHeadersS1.toArray(columnHeadersSS1);
			int rows1 = rt1.size();
			List<List<String>> dataList1 = new ArrayList();

			int jxxxxxxxxxxx;
			for (int rxxx = 0; rxxx < rows1; ++rxxx) {
				List<String> strings1 = new ArrayList();

				for (jxxxxxxxxxxx = 0; jxxxxxxxxxxx < columnHeaders1.length; ++jxxxxxxxxxxx) {
					String values1 = rt1.getStringValue(columnHeaders1[jxxxxxxxxxxx], rxxx);
					strings1.add(values1);
				}

				dataList1.add(strings1);
			}

			data1 = new Double[dataList1.size()][];

			int jxxxxxx;
			for (jxxxxxx = 0; jxxxxxx < data1.length; ++jxxxxxx) {
				data1[jxxxxxx] = new Double[((List) dataList1.get(jxxxxxx)).size()];
			}

			for (jxxxxxx = 0; jxxxxxx < dataList1.size(); ++jxxxxxx) {
				for (jxxxxxxxxxxx = 1; jxxxxxxxxxxx < ((List) dataList1.get(jxxxxxx)).size(); ++jxxxxxxxxxxx) {
					data1[jxxxxxx][jxxxxxxxxxxx] = Double
							.parseDouble((String) ((List) dataList1.get(jxxxxxx)).get(jxxxxxxxxxxx));
				}
			}

			DefaultTableModel model1 = new DefaultTableModel(data1, columnHeaders1) {
				public Class<?> getColumnClass(int column) {
					if (this.getRowCount() > 0) {
						Object value = this.getValueAt(0, column);
						if (value != null) {
							return this.getValueAt(0, column).getClass();
						}
					}

					return super.getColumnClass(column);
				}
			};
			table1 = new JTable();
			table1.setModel(model1);

			for (jxxxxxxxxxxx = 0; jxxxxxxxxxxx < roisNames.size(); ++jxxxxxxxxxxx) {
				table1.setValueAt(roisNames.get(jxxxxxxxxxxx), jxxxxxxxxxxx, table1.convertColumnIndexToModel(0));
			}

			model1.addColumn("Class-Label");
			table1.moveColumn(table1.getColumnCount() - 1, 0);

			tableData1 = new Object[table1.getModel().getRowCount()][table1.getModel().getColumnCount()];
			int jxxxxxxxxxxxx;
			/*
			 * //String[] splitParameter = parameters.split(";"); List<String>
			 * mainParameters1 = new ArrayList<String>(); List<Double> minValue1 = new
			 * ArrayList<Double>(); List<Double> maxValue1 = new ArrayList<Double>(); for
			 * (int i = 0; i < splitParameter.length; i++) if
			 * (splitParameter[i].contains(StartPageModified.tfCh2.getText()) ==
			 * Boolean.TRUE) {
			 * 
			 * mainParameters1 .add(splitParameter[i].substring(0,
			 * splitParameter[i].indexOf(":")).replace("<br>", ""));
			 * minValue1.add(Double.parseDouble(splitParameter[i].substring(splitParameter[i
			 * ].indexOf("[") + 1, splitParameter[i].indexOf(","))));
			 * maxValue1.add(Double.parseDouble(splitParameter[i].substring(splitParameter[i
			 * ].indexOf(",") + 1, splitParameter[i].indexOf("]"))));
			 * 
			 * }
			 */
			List<Double> meanValues1 = new ArrayList<Double>();
			for (int u = 0; u < model1.getRowCount(); ++u)
				meanValues1.add((Double) table1.getModel().getValueAt(u, table1.getColumn("Mean").getModelIndex()));
			Double averageValue1 = meanValues1.stream().mapToDouble(val -> val).average().orElse(0.0);

			for (int u = 0; u < model1.getRowCount(); ++u)
				if ((Double) table1.getModel().getValueAt(u, table1.getColumn("Mean").getModelIndex()) >= (averageValue1
						- 10.0)) {

					model1.setValueAt("Labeled", u, table1.convertColumnIndexToModel(0));
					rt1.setValue("Class-Label", u, "Labeled");

					for (jxxxxxxxxxxxx = 0; jxxxxxxxxxxxx < table1.getModel().getColumnCount(); ++jxxxxxxxxxxxx) {
						tableData1[u][jxxxxxxxxxxxx] = table1.getModel().getValueAt(u,
								table1.convertColumnIndexToModel(jxxxxxxxxxxxx));
					}
				}
			model1.fireTableDataChanged();
			table1.repaint();

			try {
				rt1.saveAs(directFolderImage + File.separator + "TableData_p21" + ".xlsx");
			} catch (IOException var66) {
				var66.printStackTrace();
			}

			if (WindowManager.getFrame("Results") != null) {
				IJ.selectWindow("Results");
				IJ.run("Close");
			}
			ImagePlus channelAnalysis2 = null;

			channelAnalysis2 = channels[0];

			rm.reset();

			for (jxxxxxxxxxxxx = 0; jxxxxxxxxxxxx < roisInitial.length; ++jxxxxxxxxxxxx) {
				rm.addRoi(roisInitial[jxxxxxxxxxxxx]);
				rm.rename(jxxxxxxxxxxxx, (String) roisNames.get(jxxxxxxxxxxxx));
			}

			rm.deselect();
			rm.runCommand(channelAnalysis2, "Measure");
			ResultsTable rt2 = ResultsTable.getResultsTable();
			String[] columnHeaders2 = rt2.getHeadings();
			List<String> columnHeadersS2 = new ArrayList();

			for (int rxxxx = 0; rxxxx < columnHeaders2.length; ++rxxxx) {
				columnHeadersS2.add(columnHeaders2[rxxxx]);
			}

			String[] columnHeadersSS2 = new String[columnHeaders2.length];
			columnHeadersS2.toArray(columnHeadersSS2);
			int rows2 = rt2.size();
			List<List<String>> dataList2 = new ArrayList();

			int jxxxxxxxxxxxxxx;
			for (int rxxxxx = 0; rxxxxx < rows2; ++rxxxxx) {
				List<String> strings2 = new ArrayList();

				for (jxxxxxxxxxxxxxx = 0; jxxxxxxxxxxxxxx < columnHeaders2.length; ++jxxxxxxxxxxxxxx) {
					String values2 = rt2.getStringValue(columnHeaders2[jxxxxxxxxxxxxxx], rxxxxx);
					strings2.add(values2);
				}

				dataList2.add(strings2);
			}

			data2 = new Double[dataList2.size()][];

			int jxxxxxxxxxx;
			for (jxxxxxxxxxx = 0; jxxxxxxxxxx < data2.length; ++jxxxxxxxxxx) {
				data2[jxxxxxxxxxx] = new Double[((List) dataList2.get(jxxxxxxxxxx)).size()];
			}

			for (jxxxxxxxxxx = 0; jxxxxxxxxxx < dataList2.size(); ++jxxxxxxxxxx) {
				for (jxxxxxxxxxxxxxx = 1; jxxxxxxxxxxxxxx < ((List) dataList2.get(jxxxxxxxxxx))
						.size(); ++jxxxxxxxxxxxxxx) {
					data2[jxxxxxxxxxx][jxxxxxxxxxxxxxx] = Double
							.parseDouble((String) ((List) dataList2.get(jxxxxxxxxxx)).get(jxxxxxxxxxxxxxx));
				}
			}

			DefaultTableModel model2 = new DefaultTableModel(data2, columnHeaders2) {
				public Class<?> getColumnClass(int column) {
					if (this.getRowCount() > 0) {
						Object value = this.getValueAt(0, column);
						if (value != null) {
							return this.getValueAt(0, column).getClass();
						}
					}

					return super.getColumnClass(column);
				}
			};
			table2 = new JTable();
			table2.setModel(model2);

			for (jxxxxxxxxxxxxxx = 0; jxxxxxxxxxxxxxx < roisNames.size(); ++jxxxxxxxxxxxxxx)
				table2.setValueAt(roisNames.get(jxxxxxxxxxxxxxx), jxxxxxxxxxxxxxx, table2.convertColumnIndexToModel(0));

			model2.addColumn("Class-Label");
			table2.moveColumn(table2.getColumnCount() - 1, 0);
			int ux;
			int c;

			List<Double> meanValues2 = new ArrayList<Double>();
			for (int u = 0; u < model2.getRowCount(); ++u)
				meanValues2.add((Double) table2.getModel().getValueAt(u, table2.getColumn("Mean").getModelIndex()));
			Double averageValue2 = meanValues2.stream().mapToDouble(val -> val).average().orElse(0.0);

			for (int u = 0; u < model2.getRowCount(); ++u)
				if ((Double) table2.getModel().getValueAt(u, table2.getColumn("Mean").getModelIndex()) >= (averageValue2
						- 10.0)) {

					model2.setValueAt("Labeled", u, table2.convertColumnIndexToModel(0));
					rt2.setValue("Class-Label", u, "Labeled");

				}

			Object[][] dataPositiveDetection = new Object[model0.getRowCount()][2];

			for (c = 0; c < dataPositiveDetection.length; ++c) {
				dataPositiveDetection[c][0] = model1.getValueAt(c, table1.convertColumnIndexToModel(0));
				dataPositiveDetection[c][1] = model2.getValueAt(c, table2.convertColumnIndexToModel(0));
			}

			try {
				rt2.saveAs(directFolderImage + File.separator + "TableData_h2AX" + ".xlsx");
			} catch (IOException var66) {
				var66.printStackTrace();
			}

			if (WindowManager.getFrame("Results") != null) {
				IJ.selectWindow("Results");
				IJ.run("Close");
			}
			c = 0;
			int counterPositiveDef = 0;
			int counter1 = 0;
			int counter1Def = 0;
			int counter2 = 0;
			int counter2Def = 0;

			for (int rowx = 0; rowx < dataPositiveDetection.length; ++rowx) {
				if (dataPositiveDetection[rowx][0] == "Labeled") {
					counter1Def = counter1++;
				}

				if (dataPositiveDetection[rowx][1] == "Labeled") {
					counter2Def = counter2++;
				}

				if (dataPositiveDetection[rowx][0] == "Labeled" && dataPositiveDetection[rowx][1] == "Labeled") {
					counterPositiveDef = c++;
				}
			}

			counterTable0List[ix] = dataPositiveDetection.length;
			classCounterDefL[ix] = counter1Def;
			classCounterDef2L[ix] = counter2Def;
			counterPositiveDefL[ix] = counterPositiveDef;
			// adding it to JScrollPane

		}

		Object[] columHeadersFinal = new Object[] { "Image-Name", "DAPI-Count", "DAPI-%", "p21-Count", "p21-%",
				"h2AX-Count", "h2AX-%", "+ -Count", "+ -%" };

		ResultsTable rtSummary = new ResultsTable(listOfFiles.length);

		int o;
		for (o = 0; o < columHeadersFinal.length; ++o) {
			rtSummary.setHeading(o, (String) columHeadersFinal[o]);
		}

		for (o = 0; o < rtSummary.size(); ++o) {
			rtSummary.setValue(0, o, listOfFiles[o].getName().substring(0, listOfFiles[o].getName().lastIndexOf(".")));
			rtSummary.setValue(1, o, (Double) counterTable0List[o]);
			rtSummary.setValue(2, o,
					(double) Math.round((Double) counterTable0List[o] * 100.0D / (Double) counterTable0List[o] * 100.0D)
							/ 100.0D);
			rtSummary.setValue(3, o, (Double) classCounterDefL[o]);
			rtSummary.setValue(4, o,
					(double) Math.round((Double) classCounterDefL[o] * 100.0D / (Double) counterTable0List[o] * 100.0D)
							/ 100.0D);
			rtSummary.setValue(5, o, (Double) classCounterDef2L[o]);
			rtSummary.setValue(6, o,
					(double) Math.round((Double) classCounterDef2L[o] * 100.0D / (Double) counterTable0List[o] * 100.0D)
							/ 100.0D);
			rtSummary.setValue(7, o, (Double) counterPositiveDefL[o]);
			rtSummary.setValue(8, o,
					(double) Math
							.round((Double) counterPositiveDefL[o] * 100.0D / (Double) counterTable0List[o] * 100.0D)
							/ 100.0D);
		}

		try {
			rtSummary.saveAs(directFolder + File.separator + "TableData_Final_Results" + ".xlsx");
		} catch (IOException var65) {
			var65.printStackTrace();
		}

		rm.close();

	}
}
