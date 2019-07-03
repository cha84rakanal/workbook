<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:set="http://exslt.org/sets" version="1.0">
<xsl:output method="html" version="4.01" encoding="UTF-8" indent="yes" />

<xsl:param name="author"/>

<xsl:template match="/">

<xsl:if test="string-length($author) = 0">
<div class="docs-text-styling" align="right"><xsl:value-of select="count(set:distinct(/books/item/creator))" />件</div>
</xsl:if>
<xsl:if test="string-length($author) != 0">
<div class="docs-text-styling" align="right"><xsl:value-of select="count(set:distinct(/books/item/creator[contains(.,$author)]))" />件</div>
</xsl:if>
<table id="result" class="mdl-data-table mdl-js-data-table" style="width:100%;margin:0 auto;">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">著者</th>
        </tr>
    </thead>
    <tbody>
        <xsl:if test="string-length($author) = 0">
        <xsl:for-each select="set:distinct(/books/item/creator)">
            <tr><td class="mdl-data-table__cell--non-numeric">
                <xsl:if test="string-length(.) &lt; 25">
                    <xsl:value-of select="."/>
                </xsl:if>
                <xsl:if test="string-length(.) &gt;= 25">
                    <xsl:value-of select="concat(substring(.,1,25),'...')" />
                </xsl:if>
            </td></tr>
        </xsl:for-each>
        </xsl:if>
        <xsl:if test="string-length($author) != 0">
        <xsl:for-each select="set:distinct(/books/item/creator[contains(.,$author)])">
            <tr><td class="mdl-data-table__cell--non-numeric">
                <xsl:if test="string-length(.) &lt; 25">
                    <xsl:value-of select="."/>
                </xsl:if>
                <xsl:if test="string-length(.) &gt;= 25">
                    <xsl:value-of select="concat(substring(.,1,25),'...')" />
                </xsl:if>
            </td></tr>
        </xsl:for-each>
        </xsl:if>
    </tbody>
</table>

</xsl:template>

</xsl:stylesheet>