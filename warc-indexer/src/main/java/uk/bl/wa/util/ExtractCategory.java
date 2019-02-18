package uk.bl.wa.util;

/*-
 * #%L
 * warc-indexer
 * %%
 * Copyright (C) 2013 - 2019 The webarchive-discovery project contributors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExtractCategory {

	public static final String SAMPLE_XLSX_FILE_PATH = "src/main/resources/teamhCategories.xlsx";

	public static void main(String[] args) throws IOException, InvalidFormatException {
//		getCatSubcatList();
		StringBuilder nameBuilder = new StringBuilder();
		String[] values = { "asdf", "fdsd" };
		for (String n : values) {
//				nameBuilder.append('\"').append(n).append('\",');
			nameBuilder.append('\"' + n + '\"' + ",");
			// can also do the following
			// nameBuilder.append("'").append(n.replace("'", "''")).append("',");
		}

		nameBuilder.deleteCharAt(nameBuilder.length() - 1);
		System.out.println(nameBuilder.toString());

	}

	/**
	 * @return
	 */
	public static Map<String, TeamhCategories> getCatSubcatList() {

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook;
		Map<String, TeamhCategories> teamhCategories = new HashMap<String, TeamhCategories>(102);
		try {
			workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

			// Retrieving the number of sheets in the Workbook
			System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

			/*
			 * ============================================================= Iterating over
			 * all the sheets in the workbook
			 * =============================================================
			 */
			System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
			workbook.forEach(sheet -> {
				System.out.println("=> " + sheet.getSheetName());
			});

			/*
			 * ================================================================== Iterating
			 * over all the rows and columns in a Sheet ================
			 * ==================================================
			 */

			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Create a DataFormatter to format and get each cell's value as
			// String
			DataFormatter dataFormatter = new DataFormatter();

			System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");
			sheet.forEach(row -> {
				if (row.getRowNum() != 0) {
					TeamhCategories teamhCategory = new TeamhCategories();
					row.forEach(cell -> {
						String cellValue = dataFormatter.formatCellValue(cell);

						if (cell.getColumnIndex() == 0) {
							teamhCategory.setWebsite(cellValue);
						} else if (cell.getColumnIndex() == 1) {
							teamhCategory.setTeamhMainCategories(cellValue.split(","));
						} else if (cell.getColumnIndex() == 2) {
							teamhCategory.setTeamhSubCategories(cellValue.split(","));
						} else if (cell.getColumnIndex() == 3) {
							teamhCategory.setTeamhpProfession(cellValue.split(","));
						} else if (cell.getColumnIndex() == 4) {
							teamhCategory.setTeamhLocation(cellValue.split(","));
						} else if (cell.getColumnIndex() == 5) {
							teamhCategory.setTeamhGender(cellValue.split(","));
						} else if (cell.getColumnIndex() == 6) {
							teamhCategory.setTeamhAgeGroup(cellValue.split(","));
						} else if (cell.getColumnIndex() == 7) {
							teamhCategory.setTeamhSkillLevel(cellValue.split(","));
						}
					});
//					System.out.println(
//							teamhCategory.getWebsite().substring(0, teamhCategory.getWebsite().indexOf(".")).trim());
					teamhCategories.put(
							teamhCategory.getWebsite().substring(0, teamhCategory.getWebsite().indexOf(".")).trim(),
							teamhCategory);
				}
			});

//			System.out.println(Arrays.toString(teamhCategories.get("answers").getTeamhGender()));
			workbook.close();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return teamhCategories;
	}
}
