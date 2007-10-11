package org.crank.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This is a "global" controller bean.
 * 
 * This will most likely be refactored into the crud framework at some point.
 * 
 * @author Paul Tabor
 */
public class ExcelExportControllerBean {

	private boolean showForm = true;
	private boolean showListing = true;
	private boolean showDetailForm = true;
	private boolean showDetailListing = true;
	private String htmlBuffer = "";
	private int numRows = 10;
	

    public void exportHtmlTableToExcel() throws IOException{        
        
        //Set the filename
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd_HHmmss");
        String filename = dt.toString(fmt) + ".xls";
        
        
        //Setup the output
        String contentType = "application/vnd.ms-excel";
        FacesContext fc = FacesContext.getCurrentInstance();
        filename = "list-export-"+ filename;
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        response.setContentType(contentType);
        
        //Write the table back out
        PrintWriter out = response.getWriter();
        out.print(htmlBuffer);
        out.close();
        fc.responseComplete();
    }    
    
    @SuppressWarnings("static-access")
	public void exportHtmlTableAsExcel() throws IOException{
        int rowCount = 0;
        int colCount = 0;
        
        //Set the filename
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd_HHmmss");
        String filename = dt.toString(fmt);
        
        
        // Create Excel Workbook and Sheet
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(filename);
        HSSFHeader header = sheet.getHeader();
        header.setCenter(filename);
        
        
        //Setup the output
        String contentType = "application/vnd.ms-excel";
        FacesContext fc = FacesContext.getCurrentInstance();
        filename = "list-export-"+ filename + ".xls";
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        response.setContentType(contentType);

        ServletOutputStream out = response.getOutputStream();
        
        try{

            htmlBuffer = "<html>" + htmlBuffer + "</html>";
            Parser parser = new Parser();
            parser.setInputHTML(htmlBuffer);
            NodeList nodelist = parser.parse(null);

            NodeList tableList = nodelist.extractAllNodesThatMatch(new TagNameFilter("TABLE"), true);
            NodeList  headList = tableList.extractAllNodesThatMatch(new TagNameFilter("THEAD"), true);
            NodeList  footList = tableList.extractAllNodesThatMatch(new TagNameFilter("TFOOT"), true);
            NodeList  rowList = tableList.extractAllNodesThatMatch(new TagNameFilter("TR"), true);
            
            //Create a ParserUtils var
            ParserUtils pu = new ParserUtils();
            //Set rowCount to size of rowList
            rowCount = rowList.size();
            
            
            HSSFFont boldFont = wb.createFont();
            boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFCellStyle boldStyle = wb.createCellStyle();
            boldStyle.setFont(boldFont);
            boldStyle.setWrapText(true);
            
            //Loop through excel 'Rows'
            for ( int i = 0; i < rowList.size(); i++ ) {
                HSSFRow row;
                String htmlRow = rowList.elementAt(i).toHtml().trim();
                String[] splitHtmlRow;
                List<String> elements = new ArrayList<String>();
                
                if(i == 0 && headList.size() == 1){
                    row = sheet.createRow((short) i);
                    htmlRow = htmlRow.replace("<th></th>","<th> </th>");
                    htmlRow = htmlRow.replace("<TH></TH>","<th> </th>");
                    htmlRow = htmlRow.replace("</span>", " </span>");
                    elements.add("tr");
                    elements.add("th");
                    if (htmlRow.indexOf("<a href=") > -1) {
                    	elements.add("a");
                    }
                    if (htmlRow.indexOf("<span ") > -1) {
                    	elements.add("span");
                    }
                } else if(i == 1 && footList.size() == 1){
                    row = sheet.createRow((short) rowList.size() - 1);
                    htmlRow = pu.trimTags(htmlRow, new String[]{"span"},false,false);
                    htmlRow = htmlRow.replace("<td></td>","<td> </td>");
                    htmlRow = htmlRow.replace("<TD></TD>","<td> </td>");
                    elements.add("tr");
                    elements.add("td");
                   
                } else {
                    
                    if (footList.size() == 1){
                        row = sheet.createRow((short) i - 1);
                    } else {
                        row = sheet.createRow((short) i);
                    }
                    
                    htmlRow = htmlRow.replace("<td></td>","<td> </td>");
                    htmlRow = htmlRow.replace("<TD></TD>","<td> </td>");
                    htmlRow = htmlRow.replace("</span>", " </span>");
                    elements.add("tr");
                    elements.add("td");
                    if (htmlRow.indexOf("<span ") > -1) {
                    	elements.add("span");
                    }
                }
                
                String[] splitElements = elements.toArray(new String[elements.size()]);
                splitHtmlRow = pu.splitTags(htmlRow, splitElements, true, false); 
                colCount = splitHtmlRow.length;
                
                //Loop through excel 'Columns'
                for (int j = 0; j < splitHtmlRow.length; j++){
                    HSSFCell cell = row.createCell((short) j);
                    
                    
                    // Calculate what the column width should be.
                    // Increase if the current width is samller than
                    // the calculated width.
                    int width = splitHtmlRow[j].length() * 325;
                    if(width > sheet.getColumnWidth((short)j)){
                        sheet.setColumnWidth((short)j, (short)width);
                    }
                    
                    
                    
                    //Wrap Text in the Cell for the Header Row
                    if(i == 0 && headList.size() == 1){
                        
                        // Determine the width of the column head
                        Pattern p = Pattern.compile(" ");
                        String[] splitHead = p.split(splitHtmlRow[j]);
                        int wordCnt = splitHead.length + 1; // +1 due to the addition of the new line to the cell below
                        for (int q = 0; q < splitHead.length; q++){
                            if(splitHead[q].length() * 325 > width)
                                width = splitHead[q].length() * 325;
                            sheet.setColumnWidth((short)j, (short)width);
                        }
                        
                        // Determine the height of the column head
                        int height = wordCnt * 275;
                        if(row.getHeight() < height){
                            row.setHeight((short)height);
                        }
                        
                        
                        // Add new line to cell content and make the cell
                        // word wrap
                        splitHtmlRow[j] = splitHtmlRow[j].replaceAll(" ", " \n");
                        
                        //Set Cell to boldStyle
                        cell.setCellStyle(boldStyle);
                        
                    }
                    
                    //Populate Cell
                    if(splitHtmlRow[j] == null){
                        cell.setCellValue("");
                    }else{
                        cell.setCellValue(splitHtmlRow[j]);
                    }
                    
                }
            }
            
            
            
        }catch(ParserException p){ p.printStackTrace(); }
        
        
        // Do stuff the Excel SpreaSheet
        // Freeze Panes on First Row
        sheet.createFreezePane(0,1);
        // Row 1 Repeats on each page
        wb.setRepeatingRowsAndColumns(0,0,0,0,1);
        
        // Set Print Area, Footer
        wb.setPrintArea(0, 0, colCount, 0, rowCount);
        HSSFFooter footer = sheet.getFooter();
        footer.setCenter("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
        // Fit Sheet to 1 page wide but very long
        sheet.setAutobreaks(true);
        HSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setFitWidth((short)1);
        ps.setFitHeight((short)9999);
        sheet.setGridsPrinted(true);
        sheet.setHorizontallyCenter(true);
        ps.setPaperSize(HSSFPrintSetup.LETTER_PAPERSIZE);
        if(colCount > 5){ps.setLandscape(true);}
        if(colCount > 10){ps.setPaperSize(HSSFPrintSetup.LEGAL_PAPERSIZE);}
        if(colCount > 14){ps.setPaperSize(HSSFPrintSetup.EXECUTIVE_PAPERSIZE);}
        // Set Margins
        ps.setHeaderMargin((double) .35);
        ps.setFooterMargin((double) .35);
        sheet.setMargin(HSSFSheet.TopMargin, (double) .50);
        sheet.setMargin(HSSFSheet.BottomMargin, (double) .50);
        sheet.setMargin(HSSFSheet.LeftMargin, (double) .50);
        sheet.setMargin(HSSFSheet.RightMargin, (double) .50);
        
        //Write out the spreadsheet
        wb.write(out);
        out.close();
        
        fc.responseComplete();
    }	
    	
	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public synchronized String updateNumRows() {
    	return null;
    }

	public synchronized String toggleShowForm() {
    	showForm = !showForm;
    	return null;
    }

	public synchronized String toggleShowListing() {
		showListing = !showListing;
    	return null;
    }

	public synchronized String toggleShowDetailForm() {
    	showDetailForm = !showDetailForm;
    	return null;
    }

	public synchronized String toggleShowDetailListing() {
		showDetailListing = !showDetailListing;
    	return null;
    }

	public boolean isShowDetailForm() {
		return showDetailForm;
	}

	public void setShowDetailForm(boolean showDetailForm) {
		this.showDetailForm = showDetailForm;
	}

	public boolean isShowDetailListing() {
		return showDetailListing;
	}

	public void setShowDetailListing(boolean showDetailListing) {
		this.showDetailListing = showDetailListing;
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}

	public boolean isShowListing() {
		return showListing;
	}

	public void setShowListing(boolean showListing) {
		this.showListing = showListing;
	}

	public String getHtmlBuffer() {
		return htmlBuffer;
	}

	public void setHtmlBuffer(String htmlBuffer) {
		this.htmlBuffer = htmlBuffer;
	}
	
	
}
