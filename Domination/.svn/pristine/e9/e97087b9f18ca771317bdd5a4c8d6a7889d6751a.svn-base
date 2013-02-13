<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 <xsl:variable name="default.encoding">utf-8</xsl:variable>
 <xsl:variable name="saxon.character.representation"></xsl:variable>
 <xsl:include href="chunker.xsl"/>


 <!--
 TODO:remove the frameset_index template and make a call to the fameset_chapter_index
 -->
 
 <xsl:template match="/">
  <!-- write frame index (top frameset) -->
  <xsl:call-template name="write.chunk">
   <xsl:with-param name="filename">frame_index.html</xsl:with-param>
   <xsl:with-param name="content"><xsl:call-template name="frameset_index"/></xsl:with-param>
  </xsl:call-template>
  
  <!-- write top frame contents -->
  <xsl:call-template name="write.chunk">
   <xsl:with-param name="filename">frame_top.html</xsl:with-param>
   <xsl:with-param name="content"><xsl:call-template name="frame_top"/></xsl:with-param>
  </xsl:call-template>
  
  
  <xsl:for-each select="/book/chapter">
   
   <!-- left frame contents -->
   <xsl:call-template name="write.chunk.with.doctype">
    <xsl:with-param name="filename">frame_<xsl:value-of select="@id"/>_c.html</xsl:with-param>
    <xsl:with-param name="content"><xsl:call-template name="frame_content"/></xsl:with-param>
    
    <xsl:with-param name="doctype-public">-//W3C//DTD XHTML 1.0 Transitional//EN</xsl:with-param>
    <xsl:with-param name="doctype-system">"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd</xsl:with-param>
   </xsl:call-template>
   
   <!-- frameset for the chapter -->
   <xsl:call-template name="write.chunk">
    <xsl:with-param name="filename">frame_<xsl:value-of select="@id"/>_f.html</xsl:with-param>
    <xsl:with-param name="content"><xsl:call-template name="frameset_chapter"/></xsl:with-param>
   </xsl:call-template>
 
   <!-- frameset for the chapter -->
   <xsl:call-template name="write.chunk">
    <xsl:with-param name="filename">frame_<xsl:value-of select="@id"/>.html</xsl:with-param>
    <xsl:with-param name="content"><xsl:call-template name="frameset_chapter_index"/></xsl:with-param>
   </xsl:call-template>
 
  </xsl:for-each>
  
 </xsl:template>
 
 
 
 
 <!-- the main frameset -->
 <xsl:template name="frameset_index">
  <html>
   <head>
    <title><xsl:value-of select="/book/title"/></title>
   </head>
   <frameset rows="30,*">
    <frame name="top" scrolling="no" noresize="noresize" target="bottom" src="frame_top.html"/>
    <frame name="bottom" scrolling="auto">
     <xsl:attribute name="src">frame_<xsl:value-of select="/book/chapter[position()=1]/@id"/>_f.html</xsl:attribute>
    </frame>
   </frameset>
  </html>
 </xsl:template>
 
 
 
 <!-- content for the top frameset -->
 <xsl:template name="frame_top">
  <html>
   <head>
    <title><xsl:value-of select="/book/title"/>: Navigation top frame</title>
    <base target="bottom" />
    <style type="text/css">
<![CDATA[
ul {
    padding:0px;
    margin:0px;
    border:none;
}
li {
    list-style-type:none;
    display:inline;
    margin-left:10px;
}
a {
    text-decoration: none;
    border-style: outset;
    border-width: 2px;
    border-color: #AAA;
    background-color: #CCC;
    color: #000;
    padding: 0px 4px;
}
a:active {
    border-style: inset;
}
]]>
    </style>
   </head>
   <body>
    <xsl:call-template name="chapters"/>
   </body>
  </html>
 </xsl:template>
 
 <xsl:template name="chapters">
  <ul>
  <xsl:for-each select="/book/chapter">
   <li>
    <a>
     <xsl:attribute name="href">frame_<xsl:value-of select="@id"/>_f.html</xsl:attribute>
     <xsl:value-of select="title"/>
    </a>
   </li>
  </xsl:for-each>
  </ul>
 </xsl:template>
 
 
 
 <!-- the main frameset for a special chapter -->
 <xsl:template name="frameset_chapter_index">
  <html>
   <head>
    <title><xsl:value-of select="/book/title"/>: <xsl:value-of select="title"/></title>
   </head>
   <frameset rows="30,*">
    <frame name="top" scrolling="no" noresize="noresize" target="bottom" src="frame_top.html"/>
    <frame name="bottom" scrolling="auto">
     <xsl:attribute name="src">frame_<xsl:value-of select="@id"/>_f.html</xsl:attribute>
    </frame>
   </frameset>
  </html>
 </xsl:template>
 
 

 
 <!-- frameset for each chapter -->
 <xsl:template name="frameset_chapter">
  <html>
   <head>
    <title><xsl:value-of select="title"/></title>
   </head>
   <frameset cols="140,*">
    <frame name="left" scrolling="no" target="main">
     <xsl:attribute name="src">frame_<xsl:value-of select="@id"/>_c.html</xsl:attribute>
    </frame>
    <frame name="main" scrolling="auto">
     <xsl:attribute name="src">ch0<xsl:value-of select="position()"/>.html</xsl:attribute>
    </frame>
   </frameset>
  </html>
 </xsl:template>
 
 
 <!-- content of chapter frame on the left-->
 <xsl:template name="frame_content">
  <html>
   <head>
    <title>Navigation for chapter <xsl:value-of select="position()"/>: <xsl:value-of select="title"/></title>
    <style type="text/css">
<![CDATA[
ul {
    padding:0px;
    margin:0px;
    border:none;
}
li {
    list-style-type:none;
    margin-bottom:10px;
    display:block;
    width:100%;
}
]]>
    </style>
   </head>
   <body>
    <ul>
     <xsl:variable name="chapterpos"><xsl:value-of select="position()"/></xsl:variable>
     <xsl:for-each select="section">
      <li><a target="main">
       <xsl:attribute name="href" xml:space="default">
         <xsl:call-template name="filename.section">
          <xsl:with-param name="chapterpos"><xsl:value-of select="$chapterpos"/></xsl:with-param>
         </xsl:call-template>
        </xsl:attribute>
       <xsl:value-of select="title"/>
      </a></li>
     </xsl:for-each>
    </ul>
   </body>
  </html>
 </xsl:template>

 
 
 <!-- some helper functions -->
 <xsl:template name="filename.section">
  <xsl:param name="chapterpos"/>
  <xsl:choose>
   <xsl:when test="position()=1">ch0<xsl:value-of select="$chapterpos"/>.html</xsl:when>
   <xsl:otherwise>ch0<xsl:value-of select="$chapterpos"/>s0<xsl:value-of select="position()"/>.html</xsl:otherwise>
  </xsl:choose>
 </xsl:template>
 
</xsl:stylesheet>
