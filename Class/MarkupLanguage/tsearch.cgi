#!/usr/bin/env ruby
# encoding: utf-8

require 'cgi'
require 'xml/xslt'

cgi = CGI.new

xslt = XML::XSLT.new
xslt.xml = "data0509.xml"
xslt.xsl = "title_search.xsl"
keyw = ""

if cgi.include?('keyword') then
  keyw = cgi["keyword"]
end
xslt.parameters = { "title" => keyw }
out = xslt.serve()

print cgi.header("text/html; charset=UTF-8")

print <<EOS
<html>
<head>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.teal-red.min.css">
    <link rel="stylesheet" href="./styles.css">
    <script type="module">
    'use strict';
    window.onload = () => {
      const dialog = document.querySelector('#dialog_selector');

      const table = document.getElementById("result");
      const rows = table.getElementsByTagName("tr");

      const miframe = document.getElementById("output_frame");

      for (let i = 0; i < rows.length; i++) {
        let currentRow = table.rows[i];
        let createClickHandler = (row) => {
          return () => {
            let id = row.getElementsByTagName("td")[0].id;
            miframe.src="http://cgi.u.tsukuba.ac.jp/~s1611468/files/bdetail.cgi?id="+id;
            dialog.showModal();
          };
        };
        currentRow.onclick = createClickHandler(currentRow);
      }

      if (!dialog.showModal) {
        dialogPolyfill.registerDialog(dialog);
      }
      dialog.querySelector('.close').addEventListener('click', function() {
          miframe.src="";
          dialog.close();
      });
    }
    </script>
    </head>
    <body>
    <div class="demo-layout mdl-layout mdl-layout--fixed-header mdl-js-layout mdl-color--grey-100">
      <header class="demo-header mdl-layout__header mdl-layout__header--scroll mdl-color--grey-100 mdl-color-text--grey-800">
        <div class="mdl-layout__header-row">
          <span class="mdl-layout-title">書誌情報検索ツール</span>
          <div class="mdl-layout-spacer"></div>
          <div class="mdl-textfield mdl-js-textfield mdl-textfield--expandable">
            <label class="mdl-button mdl-js-button mdl-button--icon" for="search">
              <i class="material-icons">search</i>
            </label>
            <div class="mdl-textfield__expandable-holder">
              <input class="mdl-textfield__input" type="text" id="search">
              <label class="mdl-textfield__label" for="search">Enter your query...</label>
            </div>
          </div>
        </div>
      </header>
      <div class="demo-ribbon"></div>
      <main class="demo-main mdl-layout__content">
        <div class="demo-container mdl-grid">
          <div class="mdl-cell mdl-cell--2-col mdl-cell--hide-tablet mdl-cell--hide-phone"></div>
          <div class="demo-content mdl-color--white mdl-shadow--4dp content mdl-color-text--grey-800 mdl-cell mdl-cell--8-col">
            <div class="demo-crumbs mdl-color-text--grey-500">
            <a href="./index.html">TOP</a> &gt; 書籍タイトル検索
            </div>
            <h3>書籍タイトル検索</h3>

            <div class="mdl-grid">
              <div class="mdl-cell mdl-cell--2-col"></div>
              <div class="mdl-cell mdl-cell--8-col">
              <form method="post" action="tsearch.cgi">
              <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="key1" name="keyword">
                <label class="mdl-textfield__label" for="key1">検索キーワード...</label>
              </div>
              <button class="mdl-button mdl-js-button mdl-js-ripple-effect" type="submit">
              検索
              </button>
              </form>
              </div>
              <div class="mdl-cell mdl-cell--2-col"></div>
            </div>  
EOS
if keyw != "" then
print <<EOS
<div class="mdl-typography--headline">
「#{keyw}」 の検索結果
</div>
EOS
end
print out
print <<EOS
          </div>
        </div>
      </main>
    </div>

    <dialog id="dialog_selector" class="mdl-dialog" style="position: absolute;width:80%">
        <h4 class="mdl-dialog__title">書籍詳細</h4>
        <div class="mdl-dialog__content">
          <iframe id="output_frame" width="80%" height="50%" style="max-width: 950px;margin: auto;margin-top:50px;">
          </iframe>
        </div>
        <div class="mdl-dialog__actions">
            <button type="button" class="mdl-button mdl-js-button mdl-js-ripple-effect close">Close</button>
        </div>
    </dialog>

    <script src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    </body>
</html>
EOS