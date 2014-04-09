/**
 * 
 */
package uk.bl.wa.solr;

/*
 * #%L
 * warc-indexer
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2013 - 2014 The UK Web Archive
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class SolrRecord implements Serializable {

	private static final long serialVersionUID = -4556484652176976470L;
	
	private SolrInputDocument doc = new SolrInputDocument();

	public String toXml() {
		return ClientUtils.toXML( doc );
	}
	
	private static int MAX_FIELD_LEN = 200;
	
	/**
	 * Remove control characters, nulls etc,
	 * 
	 * @param value
	 * @return
	 */
	private String removeControlCharacters( String value ) {
		return value.trim().replaceAll( "\\p{Cntrl}", "" );
	}
	
	/**
	 * Also shorten to avoid bad data filling 'small' fields with 'big' data.
	 * 
	 * @param value
	 * @return
	 */
	private String sanitizeString( String solr_property, String value ) {
		if( ! solr_property.equals( SolrFields.SOLR_EXTRACTED_TEXT ) ) {
			if( value.length() > MAX_FIELD_LEN ) {
				value = value.substring(0, MAX_FIELD_LEN);
			}
		}
		return removeControlCharacters(value);
	}

	/**
	 * Add any non-null string properties, stripping control characters if present.
	 * 
	 * @param solr_property
	 * @param value
	 */
	public void addField( String solr_property, String value ) {
		if( value != null )
			doc.addField( solr_property, sanitizeString(solr_property, value) );
	}

	/**
	 * Set instead of adding fields.
	 * 
	 * @param solr_property
	 * @param value
	 */
	public void setField( String solr_property, String value ) {
		if( value != null )
			doc.setField( solr_property, sanitizeString(solr_property, value) );
	}
	
	/**
	 * Like add, but also allows these values to merge with those in the index already.
	 * 
	 * @param solr_property
	 * @param value
	 */
	public void mergeField( String solr_property, String value ) {
		Map<String, String> operation = new HashMap<String, String>();
		operation.put("add", value );
		doc.addField( solr_property, operation);
	}

	/**
	 * @param fieldname
	 * @return
	 */
	public Object getFieldValue(String fieldname) {
		return doc.getFieldValue(fieldname);
	}

	/**
	 * @return
	 */
	public SolrInputDocument getSolrDocument() {
		return doc;
	}

	/**
	 * @param fieldname
	 */
	public void removeField(String fieldname) {
		doc.removeField(fieldname);
	}

	/**
	 * @param fieldname
	 * @return
	 */
	public SolrInputField getField(String fieldname) {
		return doc.getField(fieldname);
	}

	/**
	 * @param fieldname
	 * @return
	 */
	public boolean containsKey(String fieldname) {
		return doc.containsKey(fieldname);
	}

	/**
	 * @param newdoc
	 */
	public void setSolrDocument(SolrInputDocument newdoc) {
		doc = newdoc;
	}

}