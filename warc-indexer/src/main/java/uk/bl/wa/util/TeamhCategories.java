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


public class TeamhCategories {

	public TeamhCategories() {
	}

	private String website;

	private String[] teamhMainCategories;

	private String[] teamhSubCategories;

	private String[] teamhpProfession;

	private String[] teamhLocation;

	private String[] teamhGender;

	private String[] teamhAgeGroup;

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String[] getTeamhMainCategories() {
		return teamhMainCategories;
	}

	public void setTeamhMainCategories(String[] teamhMainCategories) {
		this.teamhMainCategories = teamhMainCategories;
	}

	public String[] getTeamhSubCategories() {
		return teamhSubCategories;
	}

	public void setTeamhSubCategories(String[] teamhSubCategories) {
		this.teamhSubCategories = teamhSubCategories;
	}

	public String[] getTeamhpProfession() {
		return teamhpProfession;
	}

	public void setTeamhpProfession(String[] teamhpProfession) {
		this.teamhpProfession = teamhpProfession;
	}

	public String[] getTeamhLocation() {
		return teamhLocation;
	}

	public void setTeamhLocation(String[] teamhLocation) {
		this.teamhLocation = teamhLocation;
	}

	public String[] getTeamhGender() {
		return teamhGender;
	}

	public void setTeamhGender(String[] teamhGender) {
		this.teamhGender = teamhGender;
	}

	public String[] getTeamhAgeGroup() {
		return teamhAgeGroup;
	}

	public void setTeamhAgeGroup(String[] teamhAgeGroup) {
		this.teamhAgeGroup = teamhAgeGroup;
	}

}
