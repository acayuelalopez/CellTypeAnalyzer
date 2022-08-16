

## Table of Contents  
- [General](#general)
- [Overview of Procedure](#overview-of-procedure)
- [Installation](#installation)
- [Video Tutorials](#video-tutorials)
- [References](#references)


# Cell-TypeAnalyzer
<!-- toc -->

<a name="general"></a>
## General
> A flexible Fiji/ImageJ plugin to classify cells according to user-defined criteria.
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/131348355-36f60ca4-0dd2-40ca-9c69-ec45196fef28.png">
</p>

**_Note_:** This plugin (currently) only supports 2D images from microscope three-channel acquisition in RGB form with each channel with same dimension and magnification: 24-bit RGB (24-bit with 8-bits per channel red, green and blue) or Color Composite (each channel can be kept as 16-bit and more than 3 channels can be merged and kept separate). This plugin may work with RGB images while it does not work with RGB stack.
<a name="overview-of-procedure"></a>
## Overview of Procedure
  **_Cell-TypeAnalyzer_** can work with images with up to three color channels. One of the channels, called Marker I, defines what a cell is and what is not. This channel can be a marker of cytoplasm, nuclei, or any other cellular structure of interest. Once we have identified cells with Marker I, Markers II and III will define the cell types. 
 
 A high-level overview of the Cell-TypeAnalyzer procedure involved is shown (see Fig.~\ref{fig:overview}). The processing actions consists of six major stages:  

**(Step I).** After loading the raw RGB images, we need to establish the correspondence between the color channels and the marker names and roles. At this point, we may perform a spatial calibration (give the pixel size in physical units) to get measurements in real length units or pixels otherwise. We may also restrict the analysis to a Region-of-interest (ROI) that must be a closed shape. The plugin shows a histogram of the pixel values in each one of the channels as visual feedback.
 
<p align="center">
  <img width="900" height="450" src="https://user-images.githubusercontent.com/83207172/131348505-22ec1651-d1cb-4487-a98f-6cd4cecbf967.png">
</p>

**(Step II).** ***The next step is the identification of the cells based on Marker I.*** To isolate cells from their background, we offer multiple possibilities. All of them respond to an auto-thresholding with different methods to binarize the image, then a watershed filtering may be applied to separate connected cells. Then single-cell contours are detected and boundaries traced. Once done, geometrical and image features are extracted from each cell, and each cell obtains a unique ID number. The plugin shows at this point a summary of the detected features through some descriptive statistics (mean, median, variance, standard deviation, minimum, maximum, quantiles, and inter-quantile range, etc.). The user may now apply filters based on these features to keep only the relevant cells for their study.
<p align="center">
  <img width="900" height="450" src="https://user-images.githubusercontent.com/83207172/131348646-9eb52f06-f984-44a2-867c-c19498dc7177.png">
</p>

**(Step III).** ***Morphological operators (erosion or dilation) may be applied to the cell contours to alter their original size.*** These operations allow the measurements on Marker II to be performed in a region that coincides with the area detected by Marker I (no operation), a smaller region (erosion), or a larger region (dilation). We may also perform a ''Bright Spots'' analysis to count small bright dots within each cell. Then we will compute geometrical and image features from Marker II in the selected regions. Finally, we may create cell types and, to each one, add as many constraints based on the Marker II features as needed.

<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/131348971-2afce5e4-03ec-41cf-9d76-c6607047fdba.png">
</p>
######   **Scriptable Pre-Processing Actions.** 
Additionally, by clicking on `Script Button`, a dialog window will be displayed in which  users are provided by a script editor to write their own code in any of ImageJ's macro supported language without saving or even, likewise, copying it to the clipboard `(crtl-a, ctrl-c)` and paste `(ctrl-v)` on script editor area, then run it. Either way, whether running is successful, pre-processing actions under the code will be applied to objects belonging DAPI channel.

**(Step IV).** ***We repeat the same actions as in Step III, but now on Marker III.*** Then, we can add the constraints on Marker III to the definition of each cell type. Cells are assigned to each one of the types if they meet all the conditions on Markers II and III. Note that cell types can also involve conditions solely on Marker II or Marker III.
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/131349089-02344753-27ec-4175-9de5-80317b6676b4.png">
</p>

 **(Step V).** ***The last interactive step allows us to configure a dynamic scatter plot to display any cell feature as a function of any other.*** Data points will represent relevant cells (those passing the criteria of a valid cell according to Marker I) being colored depending on their cell type or in gray if they do not fulfill the criteria of any defined cell type. Finally, we may save an XML configuration file that will allow us to run this analysis in batch mode for many images (Step VI).
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/131349143-1beca0b9-3f01-4153-bc1f-ed2c1ffa14e6.png">
</p>

**(Step VI).** In this step, we apply the image analysis steps defined above (cell segmentation, region operations, etc.) and classify the detected cells into the user-defined cell types to a large number of images that have been acquired with similar characteristics as the one that served to set up the analysis. This execution is performed in batch mode and produces text or Excel files with the results for each image and a summary for the whole set.
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/131349231-7256837a-ef6a-4468-b428-94f10db5b451.png">
</p>

<a name="installation"></a>
## Installation

The ***CellTypeAnalyzer*** plugin may be installed in Fiji or ImageJ by following these steps:

1. In the event of not having ImageJ or Fiji already installed, please navigate through [https://imagej.nih.gov/ij/download.html](https://imagej.nih.gov/ij/download.html), download it and then, install it on a computer with Java pre-installed in either Windows, Mac OS or Linux systems.
2.  Once done, download the plugin JAR file named as [CellTypeAnalyzer_.jar](https://github.com/QuantitativeImageAnalysisUnitCNB/CellTypeAnalyzer/blob/master/CellTypeAnalyzer_.jar) from repository.
3.  Move this file into the `ImageJ/Fiji "plugins" subfolder`, or differently, by dragging and dropping into the `ImageJ/Fiji main window` or optionally, running through menu bar `"Plugins"` **→** `"Install"` **→**  `‘Path to File’`. Then restart either ImageJ or Fiji and it is about time to start using "CellTypeAnalyzer".

<a name="video-tutorials"></a>
## Video Tutorials 
### Running Cell-TypeAnalyzer in batch-mode

https://user-images.githubusercontent.com/83207172/145421615-7b3b9a0e-96b2-4395-bd72-3c3dfbd8d550.mp4

### Running Cell-TypeAnalyzer for single-image
https://user-images.githubusercontent.com/83207172/145422218-4af40a00-ff3c-4953-a64f-bd457004c3ce.mp4

<a name="references"></a>
## References
<a id="1">[1]</a> 
Cayuela López, A., Gómez-Pedrero, J., Blanco, A., & Sorzano, C. (2022).  
 Cell-TypeAnalyzer: A flexible Fiji/ImageJ plugin to classify cells according to user-defined criteria. 
Biological Imaging, 2,E5. [![DOI:10.1017/S2633903X22000058](http://img.shields.io/badge/DOI-10.1101/2021.01.08.425840-B31B1B.svg)](https://doi.org/10.1038/nmeth.2019)

