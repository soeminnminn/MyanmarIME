﻿/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
// SMM {

package com.androidtweak.inputmethod.myanmar;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class MyanmarImeUtils {
	private static final int NULL_CHAR = 0x00;
	private static final int JBFIX_CHAR = 0x200C; // 0x200C || 0xFEFF;
	
	private static final int CHARINDEX_ThaWaiHtoe = 0;
	private static final int CHARINDEX_RaRitt = 1;
	private static final int CHARINDEX_Consonant = 2;
	private static final int CHARINDEX_DoubleConsonant = 3;
	private static final int CHARINDEX_LowerConsonant = 4;
	private static final int CHARINDEX_LoneGyiTin = 5; 
	private static final int CHARINDEX_HaHtoe = 6; 
	private static final int CHARINDEX_WaSwal = 7; 
	private static final int CHARINDEX_YaPint = 8;
	private static final int CHARINDEX_TeeTeeTin = 9;
	private static final int CHARINDEX_ChangNyin = 10; 
	private static final int CHARINDEX_NoutPyit = 11; 
	private static final int CHARINDEX_YeeKhya = 12;
	private static final int CHARINDEX_AThart = 13;
	private static final int CHARINDEX_OutNyint = 14;
	private static final int CHARINDEX_WittSaPout = 15;
	private static final int CHARINDEX_Other = 16;
	
	private static final String[] patterns;
	static {
		patterns = new String[] { "\u106A", "\u1009"
										, "\u1025(?=[\u1039\u102C])", "\u1009"
										, "\u1025\u102E", "\u1026"
										, "\u106B", "\u100A"
										, "\u1090", "\u101B"
										, "\u108F", "\u1014"
										//, "\u1040", "\u1040"
										//, "\u1012", "\u1012"
										//, "\u1013", "\u1013"
										, "[\u103D\u1087]", "\u103E"
										, "\u103C", "\u103D"
										, "[\u103B\u107E\u107F\u1080\u1081\u1082\u1083\u1084]", "\u103C"
										, "[\u103A\u107D]", "\u103B"
										, "\u103E\u103B", "\u103B\u103E"
										, "\u108A", "\u103D\u103E"
										, "\u103E\u103D", "\u103D\u103E"
										, "(\u1031)?(\u103C)?([\u1000-\u1021])\u1064", "\u1064$1$2$3"
										, "(\u1031)?(\u103C)?([\u1000-\u1021])\u108B", "\u1064$1$2$3\u102D"
										, "(\u1031)?(\u103C)?([\u1000-\u1021])\u108C", "\u1064$1$2$3\u102E"
										, "(\u1031)?(\u103C)?([\u1000-\u1021])\u108D", "\u1064$1$2$3\u1036"
										, "\u105A", "\u102B\u103A"
										, "\u108E", "\u102D\u1036"
										, "\u1033", "\u102F"
										, "\u1034", "\u1030"
										, "\u1088", "\u103E\u102F"
										, "\u1089", "\u103E\u1030"
										, "\u1039", "\u103A"
										, "[\u1094\u1095]", "\u1037"
										, "([\u1000-\u1021])([\u102C\u102D\u102E\u1032\u1036]){1,2}([\u1060\u1061\u1062\u1063\u1065\u1066\u1067\u1068\u1069\u1070\u1071\u1072\u1073\u1074\u1075\u1076\u1077\u1078\u1079\u107A\u107B\u107C\u1085])", "$1$3$2"
										, "\u1064", "\u1004\u103A\u1039"
										, "\u104E", "\u104E\u1004\u103A\u1038"
										, "\u1086", "\u103F"
										, "\u1060", "\u1039\u1000"
										, "\u1061", "\u1039\u1001"
										, "\u1062", "\u1039\u1002"
										, "\u1063", "\u1039\u1003"
										, "\u1065", "\u1039\u1005"
										, "[\u1066\u1067]", "\u1039\u1006"
										, "\u1068", "\u1039\u1007"
										, "\u1069", "\u1039\u1008"
										, "\u106C", "\u1039\u100B"
										, "\u1070", "\u1039\u100F"
										, "[\u1071\u1072]", "\u1039\u1010"
										, "[\u1073\u1074]", "\u1039\u1011"
										, "\u1075", "\u1039\u1012"
										, "\u1076", "\u1039\u1013"
										, "\u1077", "\u1039\u1014"
										, "\u1078", "\u1039\u1015"
										, "\u1079", "\u1039\u1016"
										, "\u107A", "\u1039\u1017"
										, "\u107B", "\u1039\u1018"
										, "\u107C", "\u1039\u1019"
										, "\u1085", "\u1039\u101C"
										, "\u106D", "\u1039\u100C"
										, "\u1091", "\u100F\u1039\u100D"
										, "\u1092", "\u100B\u1039\u100C"
										, "\u1097", "\u100B\u1039\u100B"
										, "\u106F", "\u100E\u1039\u100D"
										, "\u106E", "\u100D\u1039\u100D"
										, "(\u103C)([\u1000-\u1021])(\u1039[\u1000-\u1021])?", "$2$3$1"
										, "(\u103E)(\u103D)([\u103B\u103C])", "$3$2$1"
										, "([\u1000-\u101C\u101E-\u102A\u102C\u102E-\u103F\u104C-\u109F ])(\u1040)", "$1\u101D"
										, "([\u1000-\u101C\u101E-\u102A\u102C\u102E-\u103F\u104C-\u109F ])(\u1047)", "$1\u101B"
										, "(\u1047)([\u1000-\u101C\u101E-\u102A\u102C\u102E-\u103F\u104C-\u109F])", "\u101B$2"
										, "(\u103E)([\u103B\u103C])", "$2$1"
										, "(\u103D)([\u103B\u103C])", "$2$1"
										, "(\u1047)(?=[\u1000-\u101C\u101E-\u102A\u102C\u102E-\u103F\u104C-\u109F ])", "\u101B"
										, "(\u1031)?([\u1000-\u1021])(\u1039[\u1000-\u1021])?([\u102D\u102E\u1032])?([\u1036\u1037\u1038]{0,2})([\u103B-\u103E]{0,3})([\u102F\u1030])?([\u1036\u1037\u1038]{0,2})([\u102D\u102E\u1032])?", "$2$3$6$1$4$9$7$5$8"
										, "\u1036\u102F", "\u102F\u1036"
										//, "\u103A\u1037", "\u1037\u103A"
										, "\u1005\u103B", "\u1008"
										, "\u101E\u103C", "\u1029"
										, "\u101E\u103C\u1031\u102C\u103A", "\u102A" };
	};
		
	private MyanmarImeUtils() {
	}
	
	private static int[] toNormalChar(int code) {
		if(!isMyChar(code)) {
			return  new int[] {code};
		}
		
		switch(code) {
			case 0x107F:
			case 0x1081:
			case 0x1083:
			case 0x1080:
			case 0x1082:
			case 0x1084:
			case 0x107E:
				return new int[] {0x103B};
			case 0x108B:
				return new int[] {0x1064, 0x102D};
			case 0x108E:
				return new int[] {0x102D, 0x1036};
			case 0x108C:
				return new int[] {0x1004, 0x1039, 0x102E};
			case 0x108A:
				return new int[] {0x103C, 0x103D};
			case 0x1087:
				return new int[] {0x103D};
			case 0x107D:
				return new int[] {0x103A};
			case 0x1033:
				return new int[] {0x102F};
			case 0x1088:
				return new int[] {0x103D, 0x102F};
			case 0x1034:
				return new int[] {0x1030};
			case 0x1089:
				return new int[] {0x103D, 0x1030};
			case 0x1094:
			case 0x1095:
				return new int[] {0x1037};
			case 0x1064:
				return new int[] {0x1004, 0x1039};
			case 0x108D:
				return new int[] {0x1004, 0x1039, 0x1036};
			case 0x102B:
				return new int[] {0x102C};
			case 0x105A:
				return new int[] {0x102C, 0x1039};
			case 0x106A:
				return new int[] {0x1025};
			case 0x1026:
				return new int[] {0x1025, 0x102E};
			case 0x1029:
				return new int[] {0x107E, 0x101E};
			case 0x102A:
				return new int[] {0x1031, 0x107E, 0x101E, 0x102C, 0x1039};
			case 0x104E:
				return new int[] {0x1044, 0x1004, 0x1039, 0x1038};
			case 0x1008:
				return new int[] {0x1005, 0x103A};
			case 0x1009:
			case 0x106B:
				return new int[] {0x100A};
			case 0x108F:
				return new int[] {0x1014};
			case 0x1090:
				return new int[] {0x101B};
			case 0x106E:
				return new int[] {0x100D, 0x100D};
			case 0x106F:
				return new int[] {0x100E, 0x100D};
			case 0x1091:
				return new int[] {0x100F, 0x100D};
			case 0x1092:
				return new int[] {0x100B, 0x106D};
			case 0x1097:
				return new int[] {0x100B, 0x106C};
			case 0x1066:
				return new int[] {0x1067};
			case 0x1071:
				return new int[] {0x1072};
			case 0x1073:
				return new int[] {0x1074};
			case 0x107B:
				return new int[] {0x1093};
			default:
				return  new int[] {code};
		}
	}
	
	@SuppressWarnings("unused")
	private static int[] toUnicodeChar(int code) {
		if ((!isMyChar(code)) 
			|| ((code >= 0x1040) && (code <= 0x1049))) {
			return  new int[] {code};
		}
		
		switch (code) {
			case 0x1000:
			case 0x1001:
			case 0x1002:
			case 0x1003:
			case 0x1004:
			case 0x1005:
			case 0x1006:
			case 0x1007:
			case 0x1008:
			case 0x1009:
			case 0x100B:
			case 0x100C:
			case 0x100D:
			case 0x100E:
			case 0x100F:
			case 0x1010:
			case 0x1011:
			case 0x1012:
			case 0x1013:
			case 0x1015:
			case 0x1016:
			case 0x1017:
			case 0x1018:
			case 0x1019:
			case 0x101A:
			case 0x101C:
			case 0x101D:
			case 0x101E:
			case 0x101F:
			case 0x1020:
			case 0x1021:
			
			case 0x1023:
			case 0x1024:
			case 0x1026:
			case 0x1027:
			case 0x1029:
			case 0x102A:
			case 0x102B:
			case 0x102C:
			case 0x102D:
			case 0x102E:
			case 0x1031:
			case 0x1032:
			case 0x1036:
			case 0x1038:
			case 0x104A:
			case 0x104B:
			case 0x104C:
			case 0x104D:
			case 0x104F:
				return  new int[] {code};
			case 0x100A:
			case 0x106B:
				return  new int[] {0x100A};
			case 0x1014:
			case 0x108F:
				return  new int[] {0x1014};
			case 0x101B:
			case 0x1090:
				return  new int[] {0x101B};
			case 0x1025:
			case 0x106A:
				return  new int[] {0x1025};
			case 0x102F:
			case 0x1033:
				return  new int[] {0x102F};
			case 0x1030:
			case 0x1034:
				return  new int[] {0x1030};
			case 0x1037:
			case 0x1094:
			case 0x1095:
				return  new int[] {0x1037};
			case 0x1039:
				return  new int[] {0x103A};
			case 0x103A:
			case 0x107D:
				return  new int[] {0x103B};
			case 0x103B:
			case 0x107E:
			case 0x107F:
			case 0x1080:
			case 0x1081:
			case 0x1082:
			case 0x1083:
			case 0x1084:
				return  new int[] {0x103C};
			case 0x103C:
				return  new int[] {0x103C};
			case 0x103D:
			case 0x1087:
				return  new int[] {0X103E};
			case 0x103F: 
			case 0x1086:
				return  new int[] {0x103F}; // Tha Gyi 	0xXXXX
			case 0x104E:
				return  new int[] {0x104E, 0x1004, 0x103A, 0x1038};
			case 0x105A:
				return  new int[] {0x102B, 0x103A};
			case 0x1060:
				return  new int[] {0x1039, 0x1000};
			case 0x1061:
				return  new int[] {0x1039, 0x1001};
			case 0x1062:
				return  new int[] {0x1039, 0x1002};
			case 0x1063:
				return  new int[] {0x1039, 0x1003};
			case 0x1064: // King Sei
				return  new int[] {0x1004, 0x103A, 0x1039};
			case 0x1065:
				return  new int[] {0x1039, 0x1005};
			case 0x1066:
			case 0x1067:
				return  new int[] {0x1039, 0x1006};
			case 0x1068:
				return  new int[] {0x1039, 0x1007};
			case 0x1069:
				return  new int[] {0x1039, 0x1008};
			case 0x106C:
				return  new int[] {0x1039, 0x100B};
			case 0x106D:
				return  new int[] {0x1039, 0x100C};
			case 0x106E:
				return  new int[] {0x100D, 0x1039, 0x100D};
			case 0x106F:
				return  new int[] {0x100D, 0x1039, 0x100E};
			case 0x1070:
				return  new int[] {0x1039, 0x100F};
			case 0x1071:
			case 0x1072:
				return  new int[] {0x1039, 0x1010};
			case 0x1073:
			case 0x1074:
				return  new int[] {0x1039, 0x1011};
			case 0x1075:
				return  new int[] {0x1039, 0x1012};
			case 0x1076:
				return  new int[] {0x1039, 0x1013};
			case 0x1077:
				return  new int[] {0x1039, 0x1014};
			case 0x1078:
				return  new int[] {0x1039, 0x1015};
			case 0x1079:
				return  new int[] {0x1039, 0x1016};
			case 0x107A:
				return  new int[] {0x1039, 0x1017};
			case 0x107B:
			case 0x1093:
				return  new int[] {0x1039, 0x1018};
			case 0x107C:
				return  new int[] {0x1039, 0x1019};
			case 0x1085:
				return  new int[] {0x1039, 0x101C};
			case 0x1088:
				return  new int[] {0x103E, 0x102F};
			case 0x1089:
				return  new int[] {0x103E, 0x1030};
			case 0x108A:
				return  new int[] {0x103E, 0x103D};
			case 0x108B:
				return  new int[] {0x1004, 0x103A, 0x1039, 0x102D};
			case 0x108C:
				return  new int[] {0x1004, 0x103A, 0x1039, 0x102E};
			case 0x108D:
				return  new int[] {0x1004, 0x103A, 0x1039, 0x1036};
			case 0x108E:
				return  new int[] {0x102D, 0x1036};
			case 0x1091:
				return  new int[] {0x100F, 0x1039, 0x100D};
			case 0x1092:
				return  new int[] {0x100B, 0x1039, 0x100C};
			case 0x1096:
				return  new int[] {0x1039, 0x1010, 0x103D};
			case 0x1097:
				return  new int[] {0x100B, 0x1039, 0x100B};
			default:
				break;
		}
		
		return  new int[] {code};
	}
	
	private static int getZawgyiCharIndex(int code) {
		int result = CHARINDEX_Other;
		switch(code)
		{
            // Consonant
            case 0x1001:
            case 0x1002:
            case 0x1004:
            case 0x1005:
            case 0x1007:
            case 0x1008:
            case 0x100B:
            case 0x100C:
            case 0x100D:
            case 0x100E:
            case 0x1012:
            case 0x1013:
            case 0x1014:
            case 0x1015:
            case 0x1016:
            case 0x1017:
            case 0x1019:
            case 0x101B:
            case 0x101D:
            case 0x1020:
            // SymbolConsonant
            case 0x1025:
            case 0x1026:
            case 0x1027:
            case 0x104C:
            case 0x104D:
            case 0x104F:
            case 0x106A:
            case 0x106E:
            case 0x106F:
            case 0x108F:
            case 0x1090:
            case 0x1092:
            case 0x1097:
            // Digit
            case 0x1040:
            case 0x1041:
            case 0x1042:
            case 0x1043:
            case 0x1044:
            case 0x1045:
            case 0x1046:
            case 0x1047:
            case 0x1048:
            case 0x1049:
            // Sign
            case 0x104A:
            case 0x104B:
                result = CHARINDEX_Consonant;
                break;

            // DoubleConsonant
            case 0x1000:
            case 0x1003:
            case 0x1006:
            case 0x100A:
            case 0x100F:
            case 0x1010:
            case 0x1011:
            case 0x1018:
            case 0x101A:
            case 0x101C:
            case 0x101E:
            case 0x101F:
            case 0x1021:
            // SymbolConsonant
            case 0x1009:
            case 0x1023:
            case 0x1024:
            case 0x1029:
            case 0x102A:
            case 0x103F:
            case 0x104E:
            case 0x106B:
            case 0x1086:
            case 0x1091:
                result = CHARINDEX_DoubleConsonant;
                break;

            // LowerConsonant
            case 0x1060:
            case 0x1061:
            case 0x1062:
            case 0x1063:
            case 0x1065:
            case 0x1066:
            case 0x1067:
            case 0x1068:
            case 0x1069:
            case 0x106C:
            case 0x106D:
            case 0x1070:
            case 0x1071:
            case 0x1072:
            case 0x1073:
            case 0x1074:
            case 0x1075:
            case 0x1076:
            case 0x1077:
            case 0x1078:
            case 0x1079:
            case 0x107A:
            case 0x107B:
            case 0x107C:
            case 0x1085:
            case 0x1093:
            case 0x1096:
                result = CHARINDEX_LowerConsonant;
                break;

            case 0x1031:
                result = CHARINDEX_ThaWaiHtoe;
                break;

			case 0x103B:
			case 0x107E:
			case 0x107F:
			case 0x1080:
			case 0x1081:
			case 0x1082:
			case 0x1083:
			case 0x1084:
                result = CHARINDEX_RaRitt;
                break;
			
			case 0x102D:
			case 0x102E:
			case 0x108B:
			case 0x108C:
			case 0x108E:
                result = CHARINDEX_LoneGyiTin;
                break;

			case 0x103C:
			case 0x108A:
                result = CHARINDEX_WaSwal;
                break;

			case 0x103D:
			case 0x1087:
                result = CHARINDEX_HaHtoe;
                break;

			case 0x103A:
			case 0x107D:
                result = CHARINDEX_YaPint;
                break;

			case 0x102F:
			case 0x1030:
			case 0x1033:
			case 0x1034:
			case 0x1088:
			case 0x1089:
                result = CHARINDEX_ChangNyin;
                break;

			case 0x1036:
                result = CHARINDEX_TeeTeeTin;
                break;

			case 0x1037:
			case 0x1094:
			case 0x1095:
                result = CHARINDEX_OutNyint;
                break;

			case 0x1039:
			case 0x1064:
			case 0x108D:
                result = CHARINDEX_AThart;
                break;

			case 0x1032:
                result = CHARINDEX_NoutPyit;
                break;

			case 0x102B:
			case 0x102C:
			case 0x105A:
                result = CHARINDEX_YeeKhya;
                break;

			case 0x1038:
                result = CHARINDEX_WittSaPout;
                break;
			
			default:
                result = CHARINDEX_Other;
                break;
		}
		
		return result;
	}
	
	private static int getCharIndex(int code) {
		if ((code >= 0x1000 && code <= 0x102A) 
			|| (code >= 0x103F && code <= 0x1057) 
			|| (code >= 0xAA60 && code <= 0xAA7B))
		{
			return CHARINDEX_Consonant;
		}
		switch(code)
		{
			case 0x1031:
				return CHARINDEX_ThaWaiHtoe;
				
			case 0x103C:
				return CHARINDEX_RaRitt;
				
			case 0x102D:
			case 0x102E:
				return CHARINDEX_LoneGyiTin;
				
			case 0x103E:
				return CHARINDEX_HaHtoe;
				
			case 0x103D:
				return CHARINDEX_WaSwal;
				
			case 0x103B:
				return CHARINDEX_YaPint;
				
			case 0x1036:
				return CHARINDEX_TeeTeeTin;
				
			case 0x102F:
			case 0x1030:
				return CHARINDEX_ChangNyin;
				
			case 0x1032:
				return CHARINDEX_NoutPyit;
				
			case 0x102B:
			case 0x102C:
				return CHARINDEX_YeeKhya;
				
			case 0x103A:
				return CHARINDEX_AThart;
				
			case 0x1037:
				return CHARINDEX_OutNyint;
				
			case 0x1038:
				return CHARINDEX_WittSaPout;
				
			default:
                break;
		}
		return CHARINDEX_Other;
	}
	
	private static CharSequence RegexReplace(CharSequence data, CharSequence find, CharSequence replacement) {
		if ((data == null) || (data == "")) return data;
		StringBuffer result = new StringBuffer();
		Pattern pattern = Pattern.compile(find.toString());
		Matcher m = pattern.matcher(data);
		while(m.find()) {
			StringBuffer replacementBuffer = new StringBuffer();
			boolean foundGroup = false;
			for(int c = 0; c < replacement.length(); c++) {
				char ch = replacement.charAt(c);
				if(foundGroup) {
					switch(ch) {
						case '1':
							replacementBuffer.append((m.group(1) == null ? "" : "$1"));
							break;
						case '2':
							replacementBuffer.append((m.group(2) == null ? "" : "$2"));
							break;
						case '3':
							replacementBuffer.append((m.group(3) == null ? "" : "$3"));
							break;
						case '4':
							replacementBuffer.append((m.group(4) == null ? "" : "$4"));
							break;
						case '5':
							replacementBuffer.append((m.group(5) == null ? "" : "$5"));
							break;
						case '6':
							replacementBuffer.append((m.group(6) == null ? "" : "$6"));
							break;
						case '7':
							replacementBuffer.append((m.group(7) == null ? "" : "$7"));
							break;
						case '8':
							replacementBuffer.append((m.group(8) == null ? "" : "$8"));
							break;
						case '9':
							replacementBuffer.append((m.group(9) == null ? "" : "$9"));
							break;
						default:
							break;
					}
					
					foundGroup = false;
					continue;
				}
				
				if (ch == '$') {
					foundGroup = true;
					continue;
				}
				
				replacementBuffer.append(ch);
			}
			
			m.appendReplacement(result, replacementBuffer.toString());
		}
		m.appendTail(result);
		return result.toString();
	}
	
	public static boolean isMyChar(int code) {
		return (code >= 0x1000 && code <= 0x109F) || (code >= 0xAA60 && code <= 0xAA7B);
	}
	
	public static boolean isMyChar(int[] codes) {
		if(codes == null) return false;
		boolean isMyChar = false;
    	for(int i = 0; i < codes.length; i++) {
    		if(isMyChar(codes[i])) {
    			isMyChar = true;
    			break;
    		}
    	}
		return isMyChar;
	}
	
	public static boolean isMyChar(CharSequence label) {
		if(label == null) return false;
		boolean isMyChar = false;
    	for(int i = 0; i < label.length(); i++) {
    		if(isMyChar(label.charAt(i))) {
    			isMyChar = true;
    			break;
    		}
    	}
		return isMyChar;
	}
	
	public static boolean isAlphabet(int code) {
		int charIndex = getZawgyiCharIndex(code);
		switch(charIndex) {
			case CHARINDEX_ThaWaiHtoe:
			case CHARINDEX_RaRitt:
			case CHARINDEX_Consonant:
			case CHARINDEX_DoubleConsonant:
				return true;
				
			case CHARINDEX_Other:
			default:
				return Character.isLetter(code);
		}
	}
	
	public static boolean mayBeNextWord(int[] codes, int nextCode, boolean isZawGyi) {
		if(codes == null) return true;
		
		int nextCharIndex = isZawGyi ? getZawgyiCharIndex(nextCode) : getCharIndex(nextCode);
		if ((nextCharIndex == CHARINDEX_ThaWaiHtoe) || (nextCharIndex == CHARINDEX_RaRitt)) return true;
		
		boolean hasConsonant = false;
		boolean isMyChar = true;
		for(int ch : codes) {
			isMyChar = isMyChar && isMyChar(ch);
			if(isMyChar) {
				int charIndex = isZawGyi ? getZawgyiCharIndex(ch) : getCharIndex(ch);
				if ((charIndex == CHARINDEX_Consonant) || (charIndex == CHARINDEX_DoubleConsonant)) {
					hasConsonant = true;
				}
			}
		}
		
		if(!isMyChar && isMyChar(nextCode)) return true;
		if(isMyChar && !isMyChar(nextCode)) return true;
		
		if(isMyChar && isMyChar(nextCode)) {
			if ((nextCharIndex == CHARINDEX_Consonant) || (nextCharIndex == CHARINDEX_DoubleConsonant)) {
				return hasConsonant;
			}
		}
		return false;
	}
	
	public static int codePointCount(String text) {
		if (TextUtils.isEmpty(text)) return 0;
		if (text.length() > 1) {
			if((int)text.charAt(0) == 0x1039) {
				return text.length() - 1;
			} 
		}
        return text.codePointCount(0, text.length());
	}
	
	public static boolean isCharEqual(int code1, int code2) {
		if(isMyChar(code1) && isMyChar(code2)) {
			int[] codes1 = toNormalChar(code1);
			int[] codes2 = toNormalChar(code2);
			if(codes1.length == codes2.length) {
				for(int i = 0; i < codes1.length; i++) {
					if(codes1[i] != codes2[i]) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
		return code1 == code2;
	}
	
    private static void appendResult(int[] result, ArrayList<Integer> resultList) {
    	if(result == null) return;
    	for (int i : result) {
    		resultList.add(i);
    	}
    }
    
    public static CharSequence getZawgyiWord(CharSequence value) {
    	if(value == null) return value;
    	int[] iArray = new int[value.length()];
    	
    	for(int i = 0; i < value.length(); i++) {
    		iArray[i] = (int)value.charAt(i);
    	}
    	
    	char[] chArray = getZawgyiWord(iArray);
    	if(chArray != null) {
			return String.valueOf(chArray);	
    	}
    	
    	return value;
    }
    
    public static char[] getZawgyiWord(int[] value) {
    	if((value == null) || (value.length < 2)) return null;
    	
    	ArrayList<Integer> resultList = new ArrayList<Integer>();
    	MyanmarImeUtils utils = new MyanmarImeUtils();
    	Word word = utils.new Word();
		
		for(int i = 0; i < value.length; i++)
		{
            int code = value[i];
            if (code == NULL_CHAR) break;
			int charIndex = getZawgyiCharIndex(code);
            boolean firstChar = ((charIndex == CHARINDEX_ThaWaiHtoe) 
                || (charIndex == CHARINDEX_RaRitt) 
                || (charIndex == CHARINDEX_Consonant) 
                || (charIndex == CHARINDEX_DoubleConsonant)
                || (charIndex == CHARINDEX_Other));

            if (word.hasConsonant() && firstChar)
            {
                word.fix();
                appendResult(word.getResult(), resultList);
                word.reset();
            }

            if (charIndex == CHARINDEX_Other)
            	resultList.add(code);
            else
            	word.setZawgyiValue((int)charIndex, code);
		}

        word.fix();
        appendResult(word.getResult(), resultList);
        word.reset();
        
        char[] arrChar = new char[resultList.size()];
		int count = 0;
		for(Integer ch : resultList) {
			arrChar[count++] = (char)ch.intValue();
		}
		
		return arrChar;
    }
    
    public static int[] getZawgyiWordFix(int[] value) {
    	if((value == null) || (value.length < 2)) return null;
    	
    	ArrayList<Integer> resultList = new ArrayList<Integer>();
    	MyanmarImeUtils utils = new MyanmarImeUtils();
    	Word word = utils.new Word();
		
		for(int i = 0; i < value.length; i++)
		{
            int code = value[i];
            if (code == NULL_CHAR) break;
			int charIndex = getZawgyiCharIndex(code);
            boolean firstChar = ((charIndex == CHARINDEX_ThaWaiHtoe) 
                || (charIndex == CHARINDEX_RaRitt) 
                || (charIndex == CHARINDEX_Consonant) 
                || (charIndex == CHARINDEX_DoubleConsonant)
                || (charIndex == CHARINDEX_Other));

            if (word.hasConsonant() && firstChar)
            {
                word.fix();
                appendResult(word.getResult(), resultList);
                word.reset();
            }

            if (charIndex == CHARINDEX_Other)
            	resultList.add(code);
            else
            	word.setZawgyiValue((int)charIndex, code);
		}

        word.fix();
        appendResult(word.getResult(), resultList);
        word.reset();
        
        int[] arrChar = new int[resultList.size()];
		int count = 0;
		for(Integer ch : resultList) {
			arrChar[count++] = ch.intValue();
		}
		
		return arrChar;
    }
    
	public static CharSequence getZawGyiToUni(CharSequence zString) {
		/*
		String output = zString.toString();
		String[] strPatterns = patterns;
		int patCount = strPatterns.length / 2;
		for (int i = 0; i < patCount; i++) {
			int idx = i * 2;
			output = Pattern.compile(strPatterns[idx]).matcher(output).replaceAll(strPatterns[(idx + 1)]);
		}
		return output; */
		CharSequence data = zString;
		final String[] strPatterns = patterns;
		final int patCount = strPatterns.length / 2;
		for (int i = 0; i < patCount; i++) {
			final int idx = i * 2;
			data = RegexReplace(data, strPatterns[idx], strPatterns[(idx + 1)]);
		}
	
		return data;
	}
	
	public static CharSequence getJellyBeanFix(CharSequence input) {
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		
		for(int i = 0; i < input.length(); i++) {
    		int iValue = (int)input.charAt(i);
    		if(iValue == 0x1031) {
    			resultList.add(JBFIX_CHAR);
    			resultList.add(iValue);
    		} else if(iValue == 0x1039) {
    			resultList.add(iValue);
    			resultList.add(JBFIX_CHAR);
    		} else {
    			resultList.add(iValue);
    		}
    	}
		
		char[] chArray = new char[resultList.size()];
		int count = 0;
		for(Integer ch : resultList) {
			chArray[count++] = (char)ch.intValue();
		}
		
		return String.valueOf(chArray);
		//String output = input.toString();
		//output = output.replaceAll("\u1031", String.valueOf((char)JBFIX_CHAR) + "\u1031");
		//output = output.replaceAll("\u1039", "\u1039" + String.valueOf((char)JBFIX_CHAR));
		//return output;
	}
		
	public static CharSequence ZawGyiDrawFix(CharSequence input) {
		return ZawGyiDrawFix(input, 0xEA00);
	}
		
	public static CharSequence ZawGyiDrawFix(CharSequence input, int fixCode) {
		if (fixCode== 0x0) return input;
		String output = input.toString();
		int index = 0;
		char[] chArray = new char[output.length()];
		for(int i = 0; i < output.length(); i++) {
			int ch = (int)output.charAt(i);
			if((ch != NULL_CHAR) && (isMyChar(ch))) {
				chArray[index++] = (char)(ch + fixCode); // 0xEA00
			}
			else {
				chArray[index++] = (char)ch;
			}
    	}
		return String.valueOf(chArray);
	}
	
	class Word {
		private static final int WORD_LENGTH = 17;
        private int[] mWord = null;
        private int[] mResult = null;
        
        public Word() {
            this.mWord = new int[WORD_LENGTH];
        }
        
        private void fixThaWaiHtoe() {
            if (this.mWord[(int)CHARINDEX_ThaWaiHtoe] == NULL_CHAR) return;
            // No Change
        }

		private void fixRaYitt() {
            if (this.mWord[(int)CHARINDEX_RaRitt] == NULL_CHAR) return;

			boolean upper = ((this.mWord[(int)CHARINDEX_LoneGyiTin] != NULL_CHAR) 
                                || (this.mWord[(int)CHARINDEX_NoutPyit] != NULL_CHAR)
                                || (this.mWord[(int)CHARINDEX_TeeTeeTin] != NULL_CHAR)
                                || (this.mWord[(int)CHARINDEX_AThart] == 0x1064)
                                || (this.mWord[(int)CHARINDEX_AThart] == 0x108D));

			boolean lower = ((this.mWord[(int)CHARINDEX_WaSwal] != NULL_CHAR) 
							|| (this.mWord[(int)CHARINDEX_LowerConsonant] != NULL_CHAR));

			if (this.mWord[(int)CHARINDEX_Consonant] != NULL_CHAR)
			{
				if (upper && lower)
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x1083;
				}
				else if (upper)
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x107F;
				}
				else if (lower)
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x1081;
				}
				else
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x103B;
				}
			}
			else
			{
				if (upper && lower)
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x1084;
				}
				else if (upper)
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x1080;
				}
				else if (lower)
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x1082;
				}
				else
				{
					this.mWord[(int)CHARINDEX_RaRitt] = 0x107E;
				}
			}
		}
		
		private void fixConsonant() {
            if ((this.mWord[(int)CHARINDEX_Consonant] == NULL_CHAR) && (this.mWord[(int)CHARINDEX_DoubleConsonant] == NULL_CHAR)) return;

			if ((this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x100A) 
			    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x106B))
			{
				if((this.mWord[(int)CHARINDEX_WaSwal] != NULL_CHAR)
				  || (this.mWord[(int)CHARINDEX_LowerConsonant] != NULL_CHAR))
				{
					this.mWord[(int)CHARINDEX_DoubleConsonant] = 0x106B;
				}
				else
				{
					this.mWord[(int)CHARINDEX_DoubleConsonant] = 0x100A;
				}
			}
			
			if ((this.mWord[(int)CHARINDEX_Consonant] == 0x1014) 
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x108F))
			{
				if((this.mWord[(int)CHARINDEX_RaRitt] != NULL_CHAR)
				   || (this.mWord[(int)CHARINDEX_WaSwal] != NULL_CHAR)
				   || (this.mWord[(int)CHARINDEX_HaHtoe] != NULL_CHAR)
				   || (this.mWord[(int)CHARINDEX_YaPint] != NULL_CHAR)
				   || (this.mWord[(int)CHARINDEX_ChangNyin] != NULL_CHAR)
				   || (this.mWord[(int)CHARINDEX_LowerConsonant] != NULL_CHAR)
				   )
				{
					this.mWord[(int)CHARINDEX_Consonant] = 0x108F;
				}
				else
				{
					this.mWord[(int)CHARINDEX_Consonant] = 0x1014;
				}
			}
			if ((this.mWord[(int)CHARINDEX_Consonant] == 0x101B) 
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1090))
			{
				if(this.mWord[(int)CHARINDEX_ChangNyin] != NULL_CHAR)
				{
					this.mWord[(int)CHARINDEX_Consonant] = 0x1090;
				}
				else
				{
					this.mWord[(int)CHARINDEX_Consonant] = 0x101B;
				}
			}
			if ((this.mWord[(int)CHARINDEX_Consonant] == 0x1025) 
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x106A))
			{
				if((this.mWord[(int)CHARINDEX_WaSwal] != NULL_CHAR)
				  || (this.mWord[(int)CHARINDEX_LowerConsonant] != NULL_CHAR))
				{
					this.mWord[(int)CHARINDEX_Consonant] = 0x106A;
				}
				else
				{
					this.mWord[(int)CHARINDEX_Consonant] = 0x1025;
				}
			}
		}

        private void fixLowerConsonant() {
            if (this.mWord[(int)CHARINDEX_LowerConsonant] == NULL_CHAR) return;

            if ((this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1066)
                || (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1067))
            {
                if (this.mWord[(int)CHARINDEX_DoubleConsonant] != NULL_CHAR)
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1066;
				}
                else
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1067;
				}
            }

            if ((this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1071)
                || (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1072))
            {
                if (this.mWord[(int)CHARINDEX_DoubleConsonant] != NULL_CHAR)
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1071;
				}
                else
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1072;
				}
            }

            if ((this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1073)
                || (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1074))
            {
                if (this.mWord[(int)CHARINDEX_DoubleConsonant] != NULL_CHAR)
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1073;
				}
                else
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1074;
				}
            }

            if ((this.mWord[(int)CHARINDEX_LowerConsonant] == 0x107B)
                || (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1093))
            {
                if (this.mWord[(int)CHARINDEX_DoubleConsonant] != NULL_CHAR)
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x107B;
				}
                else
				{
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1093;
				}
            }
        }
		
		private void fixLoneGyiTin() {
			if (this.mWord[(int)CHARINDEX_LoneGyiTin] == NULL_CHAR) return;
			
			if (this.mWord[(int)CHARINDEX_AThart] == 0x1064)
			{
				if (this.mWord[(int)CHARINDEX_LoneGyiTin] == 0x102D)
				{
					this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x108B;
				}
				else
				{
					this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x108C;
				}
				this.mWord[(int)CHARINDEX_AThart] = NULL_CHAR;
			}
			else if (this.mWord[(int)CHARINDEX_TeeTeeTin] != NULL_CHAR)
			{
				this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x108E;
			}
			else if ((this.mWord[(int)CHARINDEX_LoneGyiTin] == 0x102D) 
			         || (this.mWord[(int)CHARINDEX_LoneGyiTin] == 0x108B)
			         || (this.mWord[(int)CHARINDEX_LoneGyiTin] == 0x108E))
			{
				this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x102D;
				this.mWord[(int)CHARINDEX_AThart] = NULL_CHAR;
			}
			else if ((this.mWord[(int)CHARINDEX_LoneGyiTin] == 0x102E)
			         || (this.mWord[(int)CHARINDEX_LoneGyiTin] == 0x108C))
			{
				this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x102E;
			}
		}
		
		private void fixHaHtoe() {
			if (this.mWord[(int)CHARINDEX_HaHtoe] == NULL_CHAR) return;
			
			if ((this.mWord[(int)CHARINDEX_RaRitt] != NULL_CHAR)
					|| (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x100A)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1009)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x106B)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x100C)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1029)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x102A)
                    )
			{
				this.mWord[(int)CHARINDEX_HaHtoe] = 0x1087;
			}
			else
			{
				this.mWord[(int)CHARINDEX_HaHtoe] = 0x103D;
			}
		}
		
		private void fixWaSwal() {
			if (this.mWord[(int)CHARINDEX_WaSwal] == NULL_CHAR) return;
			
			if (this.mWord[(int)CHARINDEX_HaHtoe] != NULL_CHAR)
			{
				this.mWord[(int)CHARINDEX_WaSwal] = 0x108A;
				this.mWord[(int)CHARINDEX_HaHtoe] = NULL_CHAR;
			}
			else
			{
				this.mWord[(int)CHARINDEX_WaSwal] = 0x103C;
			}
		}
		
		private void fixYaPint() {
			if (this.mWord[(int)CHARINDEX_YaPint] == NULL_CHAR) return;

            if (this.mWord[(int)CHARINDEX_WaSwal] != NULL_CHAR)
			{
				this.mWord[(int)CHARINDEX_YaPint] = 0x107D;
			}
			else
			{
				this.mWord[(int)CHARINDEX_YaPint] = 0x103A;
			}
		}
		
		private void fixChangNyin() {
			if (this.mWord[(int)CHARINDEX_ChangNyin] == NULL_CHAR) return;
			
			boolean isLong = ((this.mWord[(int)CHARINDEX_RaRitt] != NULL_CHAR)
                    || (this.mWord[(int)CHARINDEX_YaPint] != NULL_CHAR)
                    || (this.mWord[(int)CHARINDEX_WaSwal] != NULL_CHAR)
                    || (this.mWord[(int)CHARINDEX_LowerConsonant] != NULL_CHAR)
                    
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1008)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x100B)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x100C)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x100D)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1020)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1025)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1026)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x106A)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x104C)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x104D)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1092)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x106E)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x106F)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1097)

                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1042)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1043)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1044)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1045)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1046)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1047)
                    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1049)

                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x100A)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1009)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x106B)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1023)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1024)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1029)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x102A)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x104E)
                    || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1091)
                    );
			
			if ((this.mWord[(int)CHARINDEX_ChangNyin] == 0x102F) 
				|| (this.mWord[(int)CHARINDEX_ChangNyin] == 0x1033))
			{
				if (isLong)
				{
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x1033;
				}
				else if(this.mWord[(int)CHARINDEX_HaHtoe] != NULL_CHAR) 
				{
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x1088;
					this.mWord[(int)CHARINDEX_HaHtoe] = NULL_CHAR;
				}
				else
				{
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x102F;
				}
			}
			else // 0x1030 | 0x1034
			{
				if (isLong)
				{
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x1034;
				}
				else if(this.mWord[(int)CHARINDEX_HaHtoe] != NULL_CHAR) 
				{
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x1089;
					this.mWord[(int)CHARINDEX_HaHtoe] = NULL_CHAR;
				}
				else
				{
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x1030;
				}
			}
		}
		
		private void fixTeeTeeTin() {
			if (this.mWord[(int)CHARINDEX_TeeTeeTin] == NULL_CHAR) return;
			// No Change 
		}
		
		private void fixNoutPyit() {
			if (this.mWord[(int)CHARINDEX_NoutPyit] == NULL_CHAR) return;
			// No Change
		}
		
		private void fixYeeKhya() {
			if (this.mWord[(int)CHARINDEX_YeeKhya] == NULL_CHAR) return;
			
			if((   (this.mWord[(int)CHARINDEX_Consonant] == 0x1001)
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1002)
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1004)
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1012)
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x1015)
			    || (this.mWord[(int)CHARINDEX_Consonant] == 0x101D))
		        && 
		   		(  (this.mWord[(int)CHARINDEX_RaRitt] == NULL_CHAR)
		    	&& (this.mWord[(int)CHARINDEX_LowerConsonant] == NULL_CHAR)
                && (this.mWord[(int)CHARINDEX_LoneGyiTin] == NULL_CHAR)
                && (this.mWord[(int)CHARINDEX_HaHtoe] == NULL_CHAR)
                && (this.mWord[(int)CHARINDEX_WaSwal] == NULL_CHAR)
                && (this.mWord[(int)CHARINDEX_YaPint] == NULL_CHAR)
                && (this.mWord[(int)CHARINDEX_ChangNyin] == NULL_CHAR)
		    	))
			{
				if ((this.mWord[(int)CHARINDEX_YeeKhya] != NULL_CHAR)
                    && (this.mWord[(int)CHARINDEX_AThart] == 0x1039))
				{
					this.mWord[(int)CHARINDEX_YeeKhya] = 0x105A;
                    this.mWord[(int)CHARINDEX_AThart] = NULL_CHAR;
				}
				else
				{
					this.mWord[(int)CHARINDEX_YeeKhya] = 0x102B;
				}
			}
			else
			{
				this.mWord[(int)CHARINDEX_YeeKhya] = 0x102C;
			}
		}

        private void fixAThart() {
            if (this.mWord[(int)CHARINDEX_AThart] == NULL_CHAR) return;
            if (this.mWord[(int)CHARINDEX_AThart] == 0x1064) 
            {
                if (this.mWord[(int)CHARINDEX_TeeTeeTin] != NULL_CHAR) 
                {
                    this.mWord[(int)CHARINDEX_AThart] = 0x108D;
					this.mWord[(int)CHARINDEX_TeeTeeTin] = NULL_CHAR;
                } 
                else 
                {
                    this.mWord[(int)CHARINDEX_AThart] = 0x1064;
                }
            }
        }

        private void fixOutNyint() {
            if (this.mWord[(int)CHARINDEX_OutNyint] == NULL_CHAR) return;
            if ((this.mWord[(int)CHARINDEX_YeeKhya] == NULL_CHAR) && ( 
				(this.mWord[(int)CHARINDEX_RaRitt] != NULL_CHAR)
                || (this.mWord[(int)CHARINDEX_WaSwal] != NULL_CHAR)
                || (this.mWord[(int)CHARINDEX_YaPint] != NULL_CHAR)
                || ((this.mWord[(int)CHARINDEX_ChangNyin] != NULL_CHAR) && (this.mWord[(int)CHARINDEX_ChangNyin] != 0x102F))
                || (this.mWord[(int)CHARINDEX_LowerConsonant] != NULL_CHAR)
                
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1008)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x100B)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x100C)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x100D)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1020)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x101B)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1090)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x104C)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x104D)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1092)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x106E)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x106F)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1097)

                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1042)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1043)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1044)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1045)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1046)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1047)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1049)
				
                || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1023)
                || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1024)
                || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1029)
                || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x102A)
                || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x104E)
                || (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x1091)
                ))
            {
                this.mWord[(int)CHARINDEX_OutNyint] = 0x1095;
            } 
            else if ((this.mWord[(int)CHARINDEX_YeeKhya] == NULL_CHAR) && ( 
				(this.mWord[(int)CHARINDEX_HaHtoe] != NULL_CHAR)
                || (this.mWord[(int)CHARINDEX_ChangNyin] == 0x102F)
                || (this.mWord[(int)CHARINDEX_Consonant] == 0x1014)) 
				)
            {
                this.mWord[(int)CHARINDEX_OutNyint] = 0x1094;
            } 
            else 
            {
                this.mWord[(int)CHARINDEX_OutNyint] = 0x1037;
            }
        }

        private void fixWittSaPout() {
			if (this.mWord[(int)CHARINDEX_WittSaPout] == NULL_CHAR) return;
			// No Change
		}
        
        private void fixMixChar() {
			if((this.mWord[(int)CHARINDEX_Consonant] == 0x100D)
				&& (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x106E)) 
			{
				this.mWord[(int)CHARINDEX_Consonant] = 0x106E;
				this.mWord[(int)CHARINDEX_LowerConsonant] = NULL_CHAR;
			}
			
        	if((this.mWord[(int)CHARINDEX_Consonant] == 0x100E) 
        		&& (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x106F)) 
    		{
        		this.mWord[(int)CHARINDEX_Consonant] = 0x106F;
        		this.mWord[(int)CHARINDEX_LowerConsonant] = NULL_CHAR;
    		}
        	
        	if((this.mWord[(int)CHARINDEX_Consonant] == 0x100B) 
    			&& (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1097))
        	{
        		this.mWord[(int)CHARINDEX_Consonant] = 0x1097;
        		this.mWord[(int)CHARINDEX_LowerConsonant] = NULL_CHAR;
        	}
        	
        	if((this.mWord[(int)CHARINDEX_ThaWaiHtoe] == 0x1031)
        		&& (this.mWord[(int)CHARINDEX_RaRitt] == 0x107E)
            	&& (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x101E)
            	&& (this.mWord[(int)CHARINDEX_YeeKhya] == 0x102C)
            	&& (this.mWord[(int)CHARINDEX_AThart] == 0x1039)) 
        	{
        		this.mWord[(int)CHARINDEX_ThaWaiHtoe] = NULL_CHAR;
                this.mWord[(int)CHARINDEX_RaRitt] = NULL_CHAR;
                this.mWord[(int)CHARINDEX_DoubleConsonant] = 0x102A;
                this.mWord[(int)CHARINDEX_YeeKhya] = NULL_CHAR;
                this.mWord[(int)CHARINDEX_AThart] = NULL_CHAR;
        	}
        	
        	if((this.mWord[(int)CHARINDEX_RaRitt] == 0x107E)
    			&& (this.mWord[(int)CHARINDEX_DoubleConsonant] == 0x101E)) 
        	{
        		this.mWord[(int)CHARINDEX_RaRitt] = NULL_CHAR;
        		this.mWord[(int)CHARINDEX_DoubleConsonant] = 0x1029;
        	}
        	
        	if((this.mWord[(int)CHARINDEX_LoneGyiTin] == 0x102E)
    			&& (this.mWord[(int)CHARINDEX_Consonant] == 0x1025)) 
        	{
        		this.mWord[(int)CHARINDEX_LoneGyiTin] = NULL_CHAR;
        		this.mWord[(int)CHARINDEX_Consonant] = 0x1026;
        	}
        	
        	if((this.mWord[(int)CHARINDEX_Consonant] == 0x1044)
        		&& (this.mWord[(int)CHARINDEX_LowerConsonant] == 0x1004)
        		&& (this.mWord[(int)CHARINDEX_AThart] == 0x1039)
        		&& (this.mWord[(int)CHARINDEX_WittSaPout] == 0x1038))
        	{
        		this.mWord[(int)CHARINDEX_Consonant] = 0x104E;
                this.mWord[(int)CHARINDEX_LowerConsonant] = NULL_CHAR;
                this.mWord[(int)CHARINDEX_AThart] = NULL_CHAR;
                this.mWord[(int)CHARINDEX_WittSaPout] = NULL_CHAR;
        	}
		}
        
        private void fixResult() {
        	boolean fixAThart = ((this.mWord[(int)CHARINDEX_ThaWaiHtoe] == NULL_CHAR)
                    && (this.mWord[(int)CHARINDEX_YaPint] != NULL_CHAR)
                    && (this.mWord[(int)CHARINDEX_AThart] != NULL_CHAR));
        	
			if ((this.mWord[(int)CHARINDEX_AThart] == 0x1064)
				|| (this.mWord[(int)CHARINDEX_AThart] == 0x108D))
			{
				fixAThart = true;
			}
			
        	final boolean fixLoneGyiTin = (this.mWord[(int)CHARINDEX_YaPint] != NULL_CHAR);
        	
        	ArrayList<Integer> list = new ArrayList<Integer>();
        	
        	this.addToList(this.mWord[(int)CHARINDEX_ThaWaiHtoe], list);
            this.addToList(this.mWord[(int)CHARINDEX_RaRitt], list);
            this.addToList(this.mWord[(int)CHARINDEX_Consonant], list);
            this.addToList(this.mWord[(int)CHARINDEX_DoubleConsonant], list);
            this.addToList(this.mWord[(int)CHARINDEX_LowerConsonant], list);
            if(!fixLoneGyiTin) this.addToList(this.mWord[(int)CHARINDEX_LoneGyiTin], list);
            this.addToList(this.mWord[(int)CHARINDEX_HaHtoe], list);
            this.addToList(this.mWord[(int)CHARINDEX_WaSwal], list);
            this.addToList(this.mWord[(int)CHARINDEX_YaPint], list);
            if (fixAThart) this.addToList(this.mWord[(int)CHARINDEX_AThart], list);
            this.addToList(this.mWord[(int)CHARINDEX_TeeTeeTin], list);
            this.addToList(this.mWord[(int)CHARINDEX_ChangNyin], list);
            if(fixLoneGyiTin) this.addToList(this.mWord[(int)CHARINDEX_LoneGyiTin], list);
            this.addToList(this.mWord[(int)CHARINDEX_NoutPyit], list);
            this.addToList(this.mWord[(int)CHARINDEX_YeeKhya], list);
            if (!fixAThart) this.addToList(this.mWord[(int)CHARINDEX_AThart], list);
            this.addToList(this.mWord[(int)CHARINDEX_OutNyint], list);
            this.addToList(this.mWord[(int)CHARINDEX_WittSaPout], list);
            this.addToList(this.mWord[(int)CHARINDEX_Other], list);
        	
            mResult = new int[list.size()];
            int count = 0;
            for (Integer n : list) {
            	mResult[count++] = n.intValue();
            }
        }
        
        private void addToList(int code, ArrayList<Integer> list) {
        	if(code != NULL_CHAR) {
        		list.add(code);
        	}
        }
        
        public void fix() {
            if (this.mWord == null) return;
			if((this.mWord[(int)CHARINDEX_Consonant] == NULL_CHAR) && (this.mWord[(int)CHARINDEX_DoubleConsonant] == NULL_CHAR)) return;

			this.fixThaWaiHtoe();
			this.fixRaYitt();
			this.fixConsonant();
			this.fixLowerConsonant();
			this.fixLoneGyiTin();
			this.fixHaHtoe();
			this.fixWaSwal();
			this.fixYaPint();
			this.fixChangNyin();
			this.fixTeeTeeTin();
			this.fixNoutPyit();
			this.fixYeeKhya();
			this.fixAThart();
			this.fixOutNyint();
			this.fixWittSaPout();
			this.fixMixChar();
			
			this.fixResult();
        }

        public void reset() {
            this.mWord = new int[WORD_LENGTH];
            this.mResult = null;
        }

        public boolean hasConsonant() {
            return (this.mWord[(int)CHARINDEX_Consonant] != NULL_CHAR) || (this.mWord[(int)CHARINDEX_DoubleConsonant] != NULL_CHAR);
        }
        
		public void setValue(int charIndex, int code) {
			if(charIndex < 0) return;
        	if(charIndex > CHARINDEX_Other) return;
			this.mWord[(int)charIndex] = code;
		}
		
        public void setZawgyiValue(int charIndex, int code) {
        	if(charIndex < 0) return;
        	if(charIndex > CHARINDEX_Other) return;
        	
        	switch (code)
            {
                case 0x106E:
                    this.mWord[(int)CHARINDEX_Consonant] = 0x100D;
                    this.mWord[(int)CHARINDEX_LowerConsonant] = code;
                    break;

                case 0x106F:
                    this.mWord[(int)CHARINDEX_Consonant] = 0x100E;
                    this.mWord[(int)CHARINDEX_LowerConsonant] = code;
                    break;

                case 0x1097:
                    this.mWord[(int)CHARINDEX_Consonant] = 0x100B;
                    this.mWord[(int)CHARINDEX_LowerConsonant] = code;
                    break;

                case 0x102A:
                    this.mWord[(int)CHARINDEX_ThaWaiHtoe] = 0x1031;
                    this.mWord[(int)CHARINDEX_RaRitt] = 0x107E;
                    this.mWord[(int)CHARINDEX_DoubleConsonant] = 0x101E;
                    this.mWord[(int)CHARINDEX_YeeKhya] = 0x102C;
                    this.mWord[(int)CHARINDEX_AThart] = 0x1039;
                    break;
                    
                case 0x1029:
                    this.mWord[(int)CHARINDEX_RaRitt] = 0x107E;
                    this.mWord[(int)CHARINDEX_DoubleConsonant] = 0x101E;
                    break;

                case 0x1026:
                    this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x102E;
                    this.mWord[(int)CHARINDEX_Consonant] = 0x1025;
                    break;

                // TODO Need to fix up
                case 0x104E:
                    this.mWord[(int)CHARINDEX_Consonant] = 0x1044;
                    this.mWord[(int)CHARINDEX_LowerConsonant] = 0x1004;
                    this.mWord[(int)CHARINDEX_AThart] = 0x1039;
                    this.mWord[(int)CHARINDEX_WittSaPout] = 0x1038;
                    break;

                case 0x105A:
                    this.mWord[(int)CHARINDEX_YeeKhya] = 0x102C;
                    this.mWord[(int)CHARINDEX_AThart] = 0x1039;
                    break;

                case 0x1088:
                    this.mWord[(int)CHARINDEX_HaHtoe] = 0x103D;
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x102F;
                    break;

                case 0x1089:
                    this.mWord[(int)CHARINDEX_HaHtoe] = 0x103D;
                    this.mWord[(int)CHARINDEX_ChangNyin] = 0x1030;
                    break;

                case 0x108A:
                    this.mWord[(int)CHARINDEX_WaSwal] = 0x103C;
                    this.mWord[(int)CHARINDEX_HaHtoe] = 0x103D;
                    break;

                case 0x108B:
                    this.mWord[(int)CHARINDEX_AThart] = 0x1064;
                    this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x102D;
                    break;

                case 0x108C:
                    this.mWord[(int)CHARINDEX_AThart] = 0x1064;
                    this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x102E;
                    break;

                case 0x108D:
                    this.mWord[(int)CHARINDEX_AThart] = 0x1064;
                    this.mWord[(int)CHARINDEX_TeeTeeTin] = 0x1036;
                    break;

                case 0x108E:
                    this.mWord[(int)CHARINDEX_LoneGyiTin] = 0x102D;
                    this.mWord[(int)CHARINDEX_TeeTeeTin] = 0x1036;
                    break;

                default:
                    this.mWord[(int)charIndex] = code;
                    break;
            }
        }
	
        public int[] getResult() {
        	return this.mResult;
        }
	}
}
// } SMM