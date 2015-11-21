/*
 * ------------------------------------------------------------------------
 * Copyright by MMI Agency, Houston, Texas, USA
 * Website: http://www.mmiagency.com; Contact: 713-929-6900
 *
 * The MMI KNIME Node is Copyright (C) 2015, MMI Agency The KNIME Nodes 
 * are free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your 
 * option) any later version. 
 * 
 * The KNIME Nodes are distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details. You should have received a 
 * copy of the GNU General Public License along with the KNIME Nodes. If 
 * not, see <http://www.gnu.org/licenses/>.
 * ------------------------------------------------------------------------
 */
package com.mmiagency.knime.nodes.moz.api.constants;

/**
 * 
 * A constants class for AnchorText Service
 * 
 * @author Radeep Solutions
 */
public class AnchorTextConstants 
{
	public static final String ANCHOR_SCOPE_PHRASE_TO_PAGE = "phrase_to_page";
	public static final String ANCHOR_SCOPE_PHRASE_TO_SUBDOMAIN = "phrase_to_subdomain";
	public static final String ANCHOR_SCOPE_PHRASE_TO_DOMAIN = "phrase_to_domain";
	public static final String ANCHOR_SCOPE_TERM_TO_PAGE = "term_to_page";
	public static final String ANCHOR_SCOPE_TERM_TO_SUBDOMAIN = "term_to_subdomain";
	public static final String ANCHOR_SCOPE_TERM_TO_DOMAIN = "term_to_domain";
	
	public static final String ANCHOR_SORT_DOMAINS_LINKING_PAGE = "domains_linking_page";
	
	public static final long ANCHOR_COL_ALL = 0;
	public static final long ANCHOR_COL_TERM_OR_PHRASE = 2;
	public static final long ANCHOR_COL_INTERNAL_PAGES_LINK = 8;
	public static final long ANCHOR_COL_INTERNAL_SUBDMNS_LINK = 16;
	public static final long ANCHOR_COL_EXTERNAL_PAGES_LINK = 32;
	public static final long ANCHOR_COL_EXTERNAL_SUBDMNS_LINK = 64;
	public static final long ANCHOR_COL_EXTERNAL_ROOTDMNS_LINK = 128;
	public static final long ANCHOR_COL_INTERNAL_MOZRANK = 256;
	public static final long ANCHOR_COL_EXTERNAL_MOZRANK = 512;
	public static final long ANCHOR_COL_FLAGS = 1024;
}
