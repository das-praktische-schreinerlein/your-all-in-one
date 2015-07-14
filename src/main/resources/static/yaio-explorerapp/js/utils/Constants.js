/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
'use strict';

// define pattern
yaioM.CONST_PATTERN_CSSCLASS = /^[ A-Za-z0-9\._-]+$/;
yaioM.CONST_PATTERN_NUMBERS = /^\d+$/;
yaioM.CONST_PATTERN_TEXTCONST = /^[A-Za-z0-9_]$/;
yaioM.CONST_PATTERN_TITLE = /^[A-Za-z0-9_]$/;

yaioM.CONST_PATTERN_SEG_TASK = /__[A-Za-z]+?[0-9]+?__/;
/** Pattern to parse Aufwand-segments */
yaioM.CONST_PATTERN_SEG_HOURS = /^[0-9]?\\.?[0-9.]+$/;
/** Pattern to parse Stand-segments */
yaioM.CONST_PATTERN_SEG_STAND = /^[0-9]?\\.?[0-9.]+$/;
/** Pattern to parse Date-segments */
yaioM.CONST_PATTERN_SEG_DATUM = /^\\d\\d\\.\\d\\d.\\d\\d\\d\\d$/;
/** Pattern to parse common String-segments */
yaioM.CONST_PATTERN_SEG_STRING = /^[-0-9\\p{L}/+_\\*\\. ]$/;
/** Pattern to parse Flag-segments */
yaioM.CONST_PATTERN_SEG_FLAG = /^[-0-9\\p{L}+_]$/;
/** Pattern to parse Integer-segments */
yaioM.CONST_PATTERN_SEG_INT = /^[0-9]$/;
/** Pattern to parse UID-segments */
yaioM.CONST_PATTERN_SEG_UID = /^[0-9A-Za-z]$/;
/** Pattern to parse ID-segments */
yaioM.CONST_PATTERN_SEG_ID = /^[0-9]$/;
/** Pattern to parse Tag-segments */
yaioM.CONST_PATTERN_SEG_TAGS = /^[-0-9\\p{L}+_\\*\\.;]$/;
/** Pattern to parse ID-Praefix-segments */
yaioM.CONST_PATTERN_SEG_PRAEFIX = /^[A-Za-z]$/;
/** Pattern to parse Checksum-segments */
yaioM.CONST_PATTERN_SEG_CHECKSUM = /^[0-9A-Za-z]$/;
/** Pattern to parse Time-segments */
yaioM.CONST_PATTERN_SEG_TIME = /^\\d\\d\\:\\d\\d$/;
