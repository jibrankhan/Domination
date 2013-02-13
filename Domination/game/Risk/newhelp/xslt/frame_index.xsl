<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 <xsl:template match="/">
  <html>
   <head>
    <title><xsl:value-of select="/book/title"/></title>
   </head>
   <frameset rows="30,*">
    <frame name="top" scrolling="no" noresize="noresize" target="bottom" src="frame_top.html"/>
    <frame name="bottom" src="ch01.html" scrolling="auto"/>
   </frameset>
  </html>
 </xsl:template>
</xsl:stylesheet>