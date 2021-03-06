#!/usr/bin/env ruby
# encoding: utf-8

require 'cgi'
require 'xml/xslt'

cgi = CGI.new

xslt = XML::XSLT.new
xslt.xml = "data0509.xml"
xslt.xsl = "book_detail.xsl"
keyw = ""

xslt_s = XML::XSLT.new
xslt_s.xml = "data0509.xml"
xslt_s.xsl = "suggest.xsl"
XML::XSLT.registerExtFunc("http://mina/","sample") do |items,n|
    items.sample(n)
end


if cgi.include?('id') then
  keyw = cgi["id"]
end
xslt.parameters = { "title" => keyw }
xslt_s.parameters = { "title" => keyw }
out = xslt.serve()
out_s = xslt_s.serve()

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
EOS
print out
print <<EOS
<h4>キーワードから推測されたあなたへのおすすめ</h4>
EOS
print out_s
print <<EOS
    <script src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    </body>
</html>
EOS