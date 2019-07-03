#!/usr/bin/env ruby
# encoding: utf-8

require 'cgi'
require 'xml/xslt'

cgi = CGI.new

xslt = XML::XSLT.new
xslt.xml = "data0509.xml"
xslt.xsl = "author_detail.xsl"
keyw = ""

if cgi.include?('akey') then
  keyw = cgi["akey"]
end
xslt.parameters = { "akey" => keyw }
out = xslt.serve()

print cgi.header("text/html; charset=UTF-8")

print <<EOS
<html>
<head>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.teal-red.min.css">
    <link rel="stylesheet" href="./styles.css">
    </head>
    <body>
<div class="mdl-typography--headline">
#{keyw}の著書 
</div>
EOS
print out
print <<EOS
    <script src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    </body>
</html>
EOS