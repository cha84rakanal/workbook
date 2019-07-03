<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" version="4.01" encoding="UTF-8" indent="yes" />

<xsl:param name="title"/>

<xsl:template match="/">
    <xsl:apply-templates select="/books/item[@no=$title]"/>
</xsl:template>

<xsl:template match="keyword">
    <xsl:value-of select="."/>
    <xsl:element name="br">
    </xsl:element>
</xsl:template>

<xsl:template match="item">
   <h3><xsl:value-of select="title" /></h3>
   <p><xsl:value-of select="concat(date/year,'年')" />&#160;<xsl:value-of select="creator" />&#160;<xsl:value-of select="publisher" /></p>
   <h4>内容</h4>
   <p><xsl:value-of select="description" /></p>
   <h4>価格</h4>
   <p><xsl:value-of select="concat(price,'円')" /></p>
   <h4>キーワード</h4>
   <p>
    <xsl:for-each select="keywords">
        <xsl:apply-templates select="keyword"/>
    </xsl:for-each>
    </p>

    <xsl:element name="a">
          <xsl:attribute name="href">
            <xsl:value-of select="url/@resource" />
          </xsl:attribute>
          <xsl:attribute name="class">
            mdl-button mdl-js-button mdl-js-ripple-effect
          </xsl:attribute>
          <xsl:attribute name="target">
            _blank
          </xsl:attribute>
	詳細を見る
	</xsl:element>

</xsl:template>

</xsl:stylesheet>