<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Content Stylesheet for "jakarta-site2" Documentation -->

<!--
	*****************************
	
	N.B. any tags with more than one attribute need to be coded using xsl:attribute 
	for all but the first attribute.
	
	This is to ensure that JDK 1.4 and 1.5 generate the attributes in the same order,
	and thus avoid huge numbers of irrelevant differences when the JDK changes.
	
	For example:
	
	<img alt="Alt">
		<xsl:attribute name="border">0</xsl:attribute>
		<xsl:attribute name="src">xxx.gif</xsl:attribute>				
	</img>
	
	*****************************
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <!-- Output method -->

  <!--xsl:output method="xml"
        omit-xml-declaration = "yes"
            encoding="iso-8859-1"
            doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
            doctype-system="http://www.w3.org/TR/html4/loose.dtd"            
              indent="yes"/-->
	
  <xsl:output method="xml"
        omit-xml-declaration = "yes"
            encoding="iso-8859-1"
            doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
            doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
             indent="yes"/>
	

  <!-- Defined parameters (overrideable) -->
  <xsl:param    name="relative-path" select="'.'"/>

  <!-- Defined variables (non-overrideable) -->

  <!-- Process an entire document into an HTML page -->
  <xsl:template match="document">   
    <xsl:variable name="site"
                select="document('site.xml')/site"/>

    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
	<meta http-equiv="Content-Type">
    <xsl:attribute name="content">text/html; charset=iso-8859-1</xsl:attribute>
	</meta>	
    <xsl:apply-templates select="meta"/>
    <title><xsl:value-of select="$site/title"/> - <xsl:value-of select="properties/title"/></title>
    <link rel="stylesheet">
    <xsl:attribute name="href">http://jakarta.apache.org/style/style.css</xsl:attribute>
    <xsl:attribute name="type">text/css</xsl:attribute>
    </link>
    <xsl:for-each select="properties/author">
      <xsl:variable name="name">
        <xsl:value-of select="."/>
      </xsl:variable>
      <xsl:variable name="email">
        <xsl:value-of select="@email"/>
      </xsl:variable>
      <meta name="author">
      <xsl:attribute name="content"><xsl:value-of select="$name"/></xsl:attribute>
      <!-- meta -->
      </meta>
      <meta name="email">
      <xsl:attribute name="content"><xsl:value-of select="$email"/></xsl:attribute>
      <!-- meta -->
      </meta>
    </xsl:for-each>
    <xsl:if test="properties/base">
      <base href="{properties/base/@href}"/>
    </xsl:if>
    </head>

    <body>

    <table class="page-header">
      <xsl:attribute name="border">0</xsl:attribute>
      <xsl:attribute name="width">100%</xsl:attribute>
      <xsl:attribute name="cellspacing">0</xsl:attribute>

      <xsl:comment>PAGE HEADER</xsl:comment>
      <tr><td colspan="2">

        <xsl:comment>JAKARTA LOGO</xsl:comment>        
        <div id="logo" style="background-image:url(http://code.google.com/images/sprites08132008.png);
		background-position:-28px -36px;cursor:pointer;height:55px;width:153px;"><a>
         <img style="background-image:url(http://code.google.com/images/sprites08132008.png);
		background-position:-28px -36px;cursor:pointer;height:55px;width:153px;"/>    
		</a>
		</div>
        
        </td><td>

        <xsl:if test="$site/logo">
          <xsl:variable name="alt">
            <xsl:value-of select="$site/logo"/>
          </xsl:variable>
          <xsl:variable name="home">
            <xsl:value-of select="$site/@href"/>
          </xsl:variable>
          <xsl:variable name="src">
            <xsl:value-of select="$site/logo/@href"/>
          </xsl:variable>

          <xsl:comment>PROJECT LOGO</xsl:comment>
          <a href="{$home}">
			<img><!-- Fix this so attributes always occur in same order -->
				<xsl:attribute name="alt"><xsl:value-of select="$alt"/></xsl:attribute>
				<xsl:attribute name="border">0</xsl:attribute>
				<xsl:attribute name="src"><xsl:value-of select="$src"/></xsl:attribute>
			</img>
          </a>
        </xsl:if>

      </td></tr>

      </table>
      <table border="0">
      <xsl:attribute name="width">100%</xsl:attribute>
      <xsl:attribute name="cellspacing">4</xsl:attribute>

      <tr>

        <xsl:comment>LEFT SIDE NAVIGATION</xsl:comment>
        <td class="left-navbar">
          <xsl:attribute name="valign">top</xsl:attribute>
          <xsl:attribute name="nowrap">nowrap</xsl:attribute>
          <xsl:apply-templates select="$site/body/navbar[@name='lhs']/menu"/>
        </td>

        <xsl:comment>MAIN BODY</xsl:comment>
        <td class="main-body">
          <xsl:attribute name="valign">top</xsl:attribute>
          <xsl:attribute name="align">left</xsl:attribute>
          <xsl:apply-templates select="body/section"/>
        </td>

        <xsl:comment>RIGHT SIDE NAVIGATION</xsl:comment>
        <td class="right-navbar">
          <xsl:attribute name="valign">top</xsl:attribute>
          <xsl:attribute name="nowrap">nowrap</xsl:attribute>
          <xsl:apply-templates select="$site/body/navbar[@name='rhs']/menu"/>
        </td>

      </tr>

      <xsl:comment>FOOTER SEPARATOR</xsl:comment>
      <tr>
        <td colspan="3">
          <hr noshade="noshade">
          <xsl:attribute name="size">1</xsl:attribute>
          </hr>
        </td>
      </tr>

      <tr><td colspan="3">
        <div class="page-footer"><em>
        Copyright <xsl:text disable-output-escaping = "yes">&amp;</xsl:text>copy; 2007-2009, The ??? . <a href="http://code.google.com/p/krank/">Legal information</a>.
        </em></div>
      </td></tr>

    </table>

    </body>
    </html>	
  </xsl:template>


  <!-- Process a menu for the navigation bar -->
  <xsl:template match="menu">
    <p><strong><xsl:value-of select="@name"/></strong></p>
    <xsl:if test="item">
      <ul>
        <xsl:apply-templates select="item"/>
      </ul>
    </xsl:if>
    <xsl:if test="div">
      <xsl:apply-templates select="div"/>
    </xsl:if>
  </xsl:template>


  <!-- Process a menu item for the navigation bar -->
  <xsl:template match="item">
    <xsl:variable name="href">
      <xsl:choose>
            <xsl:when test="starts-with(@href, 'http://')">
                <xsl:value-of select="@href"/>
            </xsl:when>
            <xsl:when test="starts-with(@href, '/site')">
                <xsl:text>http://jakarta.apache.org</xsl:text><xsl:value-of select="@href"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$relative-path"/><xsl:value-of select="@href"/>
            </xsl:otherwise>    
      </xsl:choose>
    </xsl:variable>
    <li><a href="{$href}"><xsl:value-of select="@name"/></a></li>
  </xsl:template>


  <!-- Process a documentation section -->
  <xsl:template match="section">
    <xsl:variable name="name">
      <xsl:value-of select="@name"/>
    </xsl:variable>
    <div class="section">
          <div class="section-header">
          <a name="{$name}">
          <strong><xsl:value-of select="@name"/></strong></a>
          </div>
      <p><div class="section-body">
        <xsl:apply-templates/>
      </div></p>
    </div>
  </xsl:template>


  <!-- Process a documentation subsection -->
  <xsl:template match="subsection">
    <xsl:variable name="name">
      <xsl:value-of select="@name"/>
    </xsl:variable>
    <div class="subsection">
      <!-- Subsection heading -->
          <div class="subsection-header">
          <a name="{$name}">
          <strong><xsl:value-of select="@name"/></strong></a>
          </div>
      <!-- Subsection body -->
      <div class="subsection-body">
        <xsl:apply-templates/>
      </div>
    </div>
  </xsl:template>


  <!-- Process a source code example -->
  <xsl:template match="source">
    <div class="source">
        <xsl:value-of select="."/>
    </div>
  </xsl:template>

  <!-- specially process td tags ala site.vsl -->
  <xsl:template match="table[@class='detail-table']/tr/td">
    <td class="detail-table-content">
        <xsl:attribute name="valign">top</xsl:attribute>
        <xsl:attribute name="align">left</xsl:attribute>
        <xsl:if test="@colspan"><xsl:attribute name="colspan"><xsl:value-of select="@colspan"/></xsl:attribute></xsl:if>
        <xsl:if test="@rowspan"><xsl:attribute name="rowspan"><xsl:value-of select="@rowspan"/></xsl:attribute></xsl:if>
        <xsl:apply-templates/>
    </td>
  </xsl:template>
  
  <!-- handle th ala site.vsl -->
  <xsl:template match="table[@class='detail-table']/tr/th">
    <td class="detail-table-header">
        <xsl:attribute name="valign">top</xsl:attribute>
        <xsl:if test="@colspan"><xsl:attribute name="colspan"><xsl:value-of select="@colspan"/></xsl:attribute></xsl:if>
        <xsl:if test="@rowspan"><xsl:attribute name="rowspan"><xsl:value-of select="@rowspan"/></xsl:attribute></xsl:if>
        <xsl:apply-templates />
    </td>
  </xsl:template>
  
  <!-- Process everything else by just passing it through -->
  <xsl:template match="*|@*">
    <xsl:copy>
      <xsl:apply-templates select="@*|*|text()"/>
    </xsl:copy>
  </xsl:template>
  
    <xsl:template match="svn">
    <div style="padding: 5px; border: 1px solid #ddd;background-color:#eee;font-family:Courier New;margin-bottom:10px;">
        <xsl:apply-templates />
    </div>
  </xsl:template>

    <xsl:template match="src">
    <div style="padding: 5px; border: 1px solid #ddd;background-color:#eee;font-family:Courier New;margin-bottom:10px;text-align: left; margin: 0em 1em 0em 1em;
    padding: 1em;
    border: 2px solid #023264;
    white-space: pre;
    text-align: left;">
        <xsl:apply-templates />
    </div>     
  </xsl:template>
  

</xsl:stylesheet>
