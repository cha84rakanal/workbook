<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" version="4.01" encoding="UTF-8" indent="yes" />

<xsl:param name="title"/>

<xsl:template match="/">
<!--
<h1><xsl:value-of select="books/metadata/title" /></h1>
<div>作成日：<xsl:value-of select="books/metadata/date_created" /></div>
<div><xsl:value-of select="count(books/item)" />件のメタデータ</div>
-->


<table class="mdl-data-table mdl-js-data-table" style="width:100%;margin:0 auto;">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">書籍タイトル</th>
            <th class="mdl-data-table__cell--non-numeric">出版社</th>
            <th class="mdl-data-table__cell--non-numeric">出版日</th>
        </tr>
    </thead>
    <tbody>
      <xsl:apply-templates select="/books/item[contains(normalize-space(title),$title)]"/>
    </tbody>
</table>

</xsl:template>

<xsl:template match="@resource">
  <tr><td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="." /></td></tr>
</xsl:template>

<xsl:template match="url">
  <tr><td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="@resource" /></td></tr>
</xsl:template>

<xsl:template match="item">
  <tr>
    <xsl:if test="string-length(title) &lt; 25">
        <td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="title" /></td>
    </xsl:if>
    <xsl:if test="string-length(title) &gt;= 25">
        <td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="concat(substring(title,1,25),'...')" /></td>
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

<xsl:template match="title">
  <tr>
    <td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="." /></td>
    <td class="mdl-data-table__cell--non-numeric"><xsl:value-of select="../publisher" /></td>
  </tr>
</xsl:template>

</xsl:stylesheet>