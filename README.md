

## Table of Contents  
- [General](#general)
- [Overview of Procedure](#overview-of-procedure)
- [Installation](#installation)
- [References](#references)

# CellTypeAnalyzer
<!-- toc -->

<a name="general"></a>
## General
> This is an ImageJ or Fiji [[1]](#1) plugin to identify, characterize, quantify and classify cells according to user-defined conditions.
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/122888698-5e665880-d342-11eb-8fff-561892d8203a.jpg">
</p>

**_Note_:** This plugin (currently) only supports 2D images from microscope three-channel acquisition in RGB form with each channel with same dimension and magnification: 24-bit RGB (24-bit with 8-bits per channel red, green and blue) or Color Composite (each channel can be kept as 16-bit and more than 3 channels can be merged and kept separate). This plugin may work with RGB images while it does not work with RGB stack.
<a name="overview-of-procedure"></a>
## Overview of Procedure
The main procedure implemented in **_Cell Type Analyzer_** was split into separate stages, each corresponding to pre-processing and post-processing actions involved. The workflow is performed navigating through wizards until appropriate outputs are achieved.

**I.** RGB images are loaded from `absolute path` to be displayed as a multi-channel stack and then processed, channel correspondence between source image and software just as labeling for each marker is configured, spatial calibration based on physical units (microns, nm...) is set, drawing a sub-region (closed area) of interest for analysis and RGB-clickable histogram for mean intensity pixel value. 
 
<p align="center">
  <img width="900" height="450" src="https://user-images.githubusercontent.com/83207172/122892387-a2a72800-d345-11eb-8f0b-610b49fa33d1.jpg">
</p>

**II.** ***Single Cell Segmentation, Detection and Identification through DAPI.*** Most appropriate `Auto-Threshold Global Method` to isolate objects of interest from background, `Watershed` filtering or not whose aim is to separate connected objects to be identified as individuals, single cell detection along with `Data Table` showing physical features together with identifiers associated with each nucleus detected and a `Summary Table` based on selected metric (marker-label, N, Sum, Mean, Median, Variance, stdDev, min, max, Q1, Q3 and IQR), possibility to apply an initial filtering based on a collection of threshold metrics to select a subset from initial detections and lastly, delineating of detections along with visualization through `ROIManager` tool.

######   **Scriptable Pre-Processing Actions.** 
Additionally, by clicking on `Script Button`, a dialog window will be displayed in which user will be prompted to browse for either `macros (.txt or .ijm for ImageJ 1.x macros)` or `scripts files (.js for javascript, .bsh for beanshell or .py for python)` to be opened and then, using the `Run button`, run it. It may be worth noting either macros or script files must contain a `"_"` (underscore) character, e.g. `"HelloWorld_.py"` to be recognised. In addition, users are provided by a script editor to write their own code in any of ImageJ's supported languages `(BeanShell, JavaScript, Macro or Python)` without saving or even, likewise, copying it to the clipboard `(crtl-a, ctrl-c)` and paste `(ctrl-v)` on script editor area, then run it. Either way, whether running is successful, pre-processing actions under the code will be applied to objects belonging DAPI channel.
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/122895187-282bd780-d348-11eb-80af-23ead625be16.jpg">
</p>

**III.** ***Customized Analysis for marker I.*** Applying morphological filters (erosion or dilation) for single detection outlines, check to get `Foci per Nucleus Analysis` for counting small bright dots per nucleus, `Data Table` showing physical features together with identifiers for single-cell nevertheless mean intensity values are tailored for selected marker, a `Summary Table` based on selected metric is supplied and a selection of physical features along with a threshold associated is required to customize classification in marker I for single cell positive identification
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/122896388-51993300-d349-11eb-9f25-8ce303ee5bac.jpg">
</p>

**IV.** ***Customized Analysis for marker II.*** Same actions as aforementioned step but now, user is allow to configure a class defined by a set of physical features for both marker I and II whereby cells that are double-labeled and hence overlaps, are considered as double-positive cells.
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/122896978-e1d77800-d349-11eb-8995-ec00fd5a34cc.jpg">
</p>

**V.** ***Configurable dynamic scatter-plot.*** Plot any kind of physical feature related to each marker as a function of another, providing the option to apply curve fitting models that best fits data and then get some statistical analysis.
######   **Run CellTypeAnalyzer in Batch-Mode.** 
Load `.XML Configuration file` which may be saved from single analysis to run ***CellTypeAnalyzer*** for large sets of images. This file contains the whole pre-processing actions and most suitable parameters picked by user throughout single image analysis.  
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/122899688-53182a80-d34c-11eb-8475-e1b1b91b7008.jpg">
</p>

<a name="installation"></a>
## Installation

The ***CellTypeAnalyzer*** plugin may be installed in Fiji or ImageJ by following these steps:

1. In the event of not having ImageJ or Fiji already installed, please navigate through [https://imagej.nih.gov/ij/download.html](https://imagej.nih.gov/ij/download.html), download it and then, install it on a computer with Java pre-installed in either Windows, Mac OS or Linux systems.
2.  Once done, download the plugin JAR file named as [CellTypeAnalyzer_.jar](https://github.com/QuantitativeImageAnalysisUnitCNB/CellTypeAnalyzer/blob/master/CellTypeAnalyzer_.jar) from repository.
3.  Move this file into the `ImageJ/Fiji "plugins" subfolder`, or differently, by dragging and dropping into the `ImageJ/Fiji main window` or optionally, running through menu bar `"Plugins"` **→** `"Install"` **→**  `‘Path to File’`. Then restart either ImageJ or Fiji and it is about time to start using "CellTypeAnalyzer".

<a name="references"></a>
## References
<a id="1">[1]</a> 
Schindelin, J., Arganda-Carreras, I., Frise, E., Kaynig, V., Longair, M., Pietzsch, T., … Cardona, A. (2012).  
Fiji: an open-source platform for biological-image analysis. 
Nature Methods, 9(7), 676–682. [![DOI:10.1038/nmeth.2019](http://img.shields.io/badge/DOI-10.1101/2021.01.08.425840-B31B1B.svg)](https://doi.org/10.1038/nmeth.2019)

