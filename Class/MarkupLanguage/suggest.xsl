<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:set="http://exslt.org/sets" xmlns:mina="http://mina/" version="1.0">
<xsl:output method="html" version="4.01" encoding="UTF-8" indent="yes" />

<xsl:param name="title"/>

<xsl:template match="/">
<table id="result" class="mdl-data-table mdl-js-data-table" style="width:100%;margin:0 auto;">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">書籍タイトル</th>
            <th class="mdl-data-table__cell--non-numeric">出版社</th>
            <th class="mdl-data-table__cell--non-numeric">出版日</th>
        </tr>
    </thead>
    <tbody>
        <xsl:apply-templates select="/books/item[@no=$title]/keywords/keyword"/>
    </tbody>
</table>
</xsl:template>

<xsl:template match="keyword">
    <xsl:variable name="sub" select="."/>
    <xsl:apply-templates select="mina:sample(/books/item[contains(keywords/keyword,$sub)],3)"/>
</xsl:template>

<xsl:template match="item">
  <tr>
    <xsl:if test="string-length(title) &lt; 25">
        <xsl:element name="td">
          <xsl:attribute name="id">
            <xsl:value-of select="@no" />
          </xsl:attribute>
          <xsl:attribute name="class">
            mdl-data-table__cell--non-numeric
          </xsl:attribute>
			    <xsl:value-of select="title" />
		    </xsl:element>
    </xsl:if>
    <xsl:if test="string-length(title) &gt;= 25">
        <xsl:element name="td">
          <xsl:attribute name="id">
            <xsl:value-of select="@no" />
          </xsl:attribute>
          <xsl:attribute name="class">
            mdl-data-table__cell--non-numeric
          </xsl:attribute>
			      <xsl:value-of select="concat(substring(title,1,25),'...')" />
		    </xsl:element>
    </xsl:if>

    <xsl:if test="string-length(publisher) &lt; 15">
        <td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="publisher" /></td>
    </xsl:if>
    <xsl:if test="string-length(publisher) &gt;= 15">
        <td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="concat(substring(publisher,1,15),'...')" /></td>
    </xsl:if>

  <td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="concat(date/year,'-',date/month,'-',date/day)" /></td>

  </tr>
</xsl:template>

</xsl:stylesheet>