<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- COMPONENT SECTIONS TO IMPORT -->
	<!-- Unit data section -->
	<xsl:import href="wordxml_unit_la_import.xsl"/>	
	<xsl:import href="wordxml_unit_ac_import.xsl"/>
	<xsl:import href="wordxml_unit_cg_import.xsl"/>
	<xsl:import href="wordxml_unit_sao_import.xsl"/>
	<xsl:import href="wordxml_unit_tg_import.xsl"/>

	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:strip-space elements="*"/>
		
	<!-- GLOBAL VARIABLES -->
	<xsl:variable name="var_uan_str" select="specification/unit/uan"/>
	<xsl:variable name="var_uan"><xsl:value-of select="translate($var_uan_str, '/', '_')"/></xsl:variable>
	
		
	<!-- MAIN ROOT NODE -->
	<xsl:template match="/">
			<xsl:result-document href="{$var_uan}.xml">
		<xsl:apply-templates select="specification/unit"/>
			</xsl:result-document>
	</xsl:template>
	
	
	
	<!-- Unit Subnode -->	
	<xsl:template match="unit">
	<xsl:element name="unit">
	<xsl:attribute name="unitid"><xsl:value-of select="$var_uan"/></xsl:attribute>
	
	<!-- uan number -->
	<uan><xsl:value-of select="$var_uan_str"/></uan>
	
	<!-- unitnumber and title -->
	<xsl:apply-templates select="t"/>
	
	<!-- description -->
	<description>
		<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
			<xsl:for-each select="i_te | ol[@type='i_nl1']">
				<!-- markup the HTML tags to the content -->
				<xsl:apply-templates select="."/>
			</xsl:for-each>
		<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>		
	</description>
	
	<!-- glh -->
	<glh>
		<glhvalue><xsl:value-of select="glh"/></glhvalue>
	</glh>
	<level><xsl:value-of select="l"/></level>
	<assessmentmode id=""><xsl:value-of select="at"/></assessmentmode>
	
	
	
	<!-- learningobjective -->
		<!-- Filter out for Learning Objective tables only -->
		<xsl:choose>
			<xsl:when test="table/tr/td[1]/ac_h2">

				<xsl:for-each select="table">
					<xsl:choose>
						<!-- look for ac_h2 in 2nd row, first cell if it is assessment critieria table -->
						<xsl:when test="./tr/td[1]/ac_h2">
							<!--AC TABLE <xsl:copy-of select="."/> END-->
							<xsl:call-template name="learningobjective_with_assessment">
								<xsl:with-param name="var_ac_table" select="."/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:for-each>
		
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="table[tr/td[1]/la_h2]">
					<xsl:call-template name="learningobjective_only">
						<xsl:with-param name="var_la_table" select="."/>
					</xsl:call-template>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	<!--End learningobjective -->

	
	
	<!-- criteriagradegrid -->
		<!-- Approach here is 
		1. strip first 2 rows of all the assesment criteria tables
		2. merge into one long table
		3. call template to parse each column and output IQS. -->
		<xsl:variable name="mergetables">
		
			<!-- get only assessment criteria tables only -->
			<xsl:variable name="firstrow">
				<xsl:for-each select="table">
					<xsl:if test="./tr[1]/td[1]/ac_h1">
						<xsl:copy-of select="./tr[1]"/>
					</xsl:if>
				</xsl:for-each>						
			</xsl:variable>		
		
			<xsl:element name="table">
				<!-- Filter out for Assessment Critieria tables only -->
				<xsl:if test="table/tr[1]/td[1]/ac_h1">
					<!-- Take first header row -->
					<xsl:copy-of select="$firstrow/tr[1]"/>
					<xsl:apply-templates select="table" mode="criteriagradegrid"/>
				</xsl:if>
			</xsl:element>
		</xsl:variable>
		<!-- DEBUG: <xsl:copy-of select="$mergetables"/>-->
		
		<!--send mergedtables variable to IQS criteriagrid-->	
		<xsl:if test="table/tr[1]/td[1]/ac_h1">			
			<xsl:call-template name="criteriagradegrid">
				<xsl:with-param name="var_table" select="$mergetables"/>
			</xsl:call-template>
		</xsl:if>
	<!--End criteriagradegrid -->
		
		
		
	<!--assessmentcriteria-->
		<xsl:apply-templates select="table" mode="assessmentcriteria"/>

	
	<!--suggestedassignment-->		
		<xsl:apply-templates select="table" mode="suggestedassignment"/>
		
		
	<!-- unitcontent -->
	<unitcontent>
		<!-- unitsection - tables for Learning Aim -->
			<!-- test if table is for LA or AC by parsing the test in 1st row first column. Painfully! -->
			<xsl:if test="table/tr/td/la_h2">
				<xsl:apply-templates select="table" mode="unitsection"></xsl:apply-templates>
			</xsl:if>
	</unitcontent>
	

	<!--genericfreetext--><!--unitessentialguidance-->
	<xsl:apply-templates select="." mode="unitessentialguidance"/>


	<!-- printdocument -->
	<printdocument><xsl:value-of select="$var_uan"/></printdocument>
	

	</xsl:element><!-- End Unit -->
	</xsl:template>	
	<!-- End Unit Subnode -->	
	
	

	
	
	
	
	
	
	
	<!-- TEMPLATES -->
	

	<!-- unitnumber and title -->
	<xsl:template match="t">	
		<xsl:choose>
				  <xsl:when test="(starts-with(normalize-space(.),'Unit')) and position() = 1">
						<xsl:variable name="unitnumber" select="translate(., translate(., '0123456789', ''), '')"/>				  
						<!-- Assumption: unitnumber is after 'Unit ' and before ': ' and contains numbers only -->
						<unitnumber><xsl:value-of select="$unitnumber"/></unitnumber>
						
						<!-- Assumption: title is after 'unitnumber:' -->
						<title><xsl:value-of select="normalize-space(substring-after(., concat($unitnumber, ':')))"/></title>
						
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
		</xsl:choose>
	</xsl:template>				
				

	
	
	<!-- description -->
	<xsl:template match="i_te | ol[@type='i_nl1']">		
		
		<xsl:choose>
			<xsl:when test="name(.) = 'i_te'">
				<!-- Don't know what this does <xsl:apply-templates select="@*|node()"/>-->
				<xsl:text>
</xsl:text><p><xsl:value-of select="."/></p><xsl:text>
</xsl:text>
			</xsl:when>
			
			<xsl:when test="name(.) = 'ol'">
				<ol>
					<xsl:copy-of select="./*"/>
				</ol><xsl:text>
</xsl:text>
			</xsl:when>
		</xsl:choose>
			
	</xsl:template>
	
	
	
	<!-- unitsection -->
	<xsl:template match="table" mode="unitsection">
		<!-- Assumption: Test if tr/td/la_h2 element contains the text "Learning Aim" -->
		<xsl:if test="starts-with(normalize-space(tr/td/la_h2),'Learning')">
		
		<xsl:element name="unitsection">
			<xsl:attribute name="sid"><xsl:value-of select="$var_uan"/>.LO<xsl:value-of select="substring-after( substring-before( normalize-space(tr/td/la_h2) ,':' ) , 'Learning aim ')"/></xsl:attribute>

			<title><xsl:value-of select="tr/td/la_h2"/></title>
			<sectiontext>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
					<xsl:apply-templates select="tr[position() = last()]/td" mode="unitsection"/>
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</sectiontext>
			
		</xsl:element>
		
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="tr[position() = last()]/td" mode="unitsection">
		<xsl:for-each select="*">
			<xsl:choose>
				<xsl:when test="name() = 'la_te_s'"><p><b><xsl:value-of select="normalize-space(.)"/></b></p></xsl:when>
				<xsl:when test="name() = 'la_te'"><p><xsl:value-of select="normalize-space(.)"/></p></xsl:when>
                <xsl:when test="name() = 'la_te_e'"><p><b><xsl:value-of select="normalize-space(.)"/></b></p></xsl:when>
				<xsl:otherwise>
				
					<!-- strip out attribute of html lists -->
					<xsl:choose>
						<xsl:when test="name() = 'ul'">
							<ul>
								<xsl:copy-of select="./*"/>
							</ul>
						</xsl:when>
						<xsl:when test="name() = 'ol'">
							<ol>
								<xsl:copy-of select="./*"/>
							</ol>
						</xsl:when>
						<xsl:otherwise><xsl:copy-of select="."/></xsl:otherwise>
					</xsl:choose>
				
				</xsl:otherwise>
			</xsl:choose>
		
		</xsl:for-each>
	
	</xsl:template>
	<!-- End unitsection -->
	
	
</xsl:stylesheet>
