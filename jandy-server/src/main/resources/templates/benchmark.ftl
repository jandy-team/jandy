<#include "include/layouts.ftl">
<#include "include/project-list.ftl">
<@layoutFully>
<#assign project = build.branch.project/>
<br>
<div class="container-fluid">
  <div class="row">
    <div class="col-md-3 hidden-xs hidden-sm">
      <@drawProjectList projects project.id/>
    </div>
    <div class="col-xs-offset-1 col-md-7 col-xs-10">
      <div class="row">
        <h2>Build <a href="https://travis-ci.org/${project.account}/${project.name}/builds/${build.travisBuildId}">#${build.number}</a></h2>
        <p>
          Max Total Duration: ${prof.maxTotalDuration/1000000}ms<br>
          Message: ${(build.commit.message)!"Merge pull-request"}<br>
          Commit ID: <a href="https://github.com/${project.account}/${project.name}/commit/${(build.commit.sha)!"25a362115243352598617072f435c606658f14f1"}">${(build.commit.sha)!"asdasd1sd2"}</a><br>
          Author: <img width="30" height="30"
                       src="${(committerAvatarUrl)!"https://avatars.githubusercontent.com/u/1345314?v=3"}">
        </p>
      </div>
      <div class="row">
        <h2>Summary</h2>
        <p id="summary"></p>
        <h3>Timeline of 2 calls</h3>
        <div id="summary-canvas"></div>
      </div>
      <section class="row" style="padding-top: 10px;">
        <h2>Profiling Results</h2>
        <h3>Timeline</h3>
        <div id="canvas"></div>
        <h3>Information</h3>
        <div id="menus"></div>
      </section>
    </div>
  </div>
</div>
<script src="${root}/js/benchmark.js"></script>
<script id="tpl-func-summary" type="text/template">
  Function: <%= func %> <br>
  Duration: <%= duration %>ms <br>
  Call By: <a href="#<%= parentId %>"><%= parentName %></a>
</script>
<script id="tpl-menu" type="text/template">
  <div id="info-<%= id %>">
    <div class="row">
      <div class="col-md-2"><span class="pull-right">Package: </span></div>
      <div class="col-md-10"><%= package %></div>
    </div>
    <div class="row">
      <div class="col-md-2"><span class="pull-right">Class: </span></div>
      <div class="col-md-10"><%= className %></div>
    </div>
    <div class="row">
      <div class="col-md-2"><span class="pull-right">Method: </span></div>
      <div class="col-md-10"><%= method %></div>
    </div>
    <div class="row">
      <div class="col-md-2"><span class="pull-right">Parameter: </span></div>
      <div class="col-md-10"><%= parameter %></div>
    </div>
    <div class="row">
      <div class="col-md-2"><span class="pull-right">Duration(ms): </span></div>
      <div class="col-md-10"><%= duration %></div>
    </div>
    <hr>
  </div>
</script>
<script>
  $(function () {
    var Benchmark = greem.Benchmark;
    new Benchmark({
      menus: _.template($("#tpl-menu").text()),
      summary: _.template($("#tpl-func-summary").text())
    }, $("#menus"), $("#summary")).start("${prof.id}");
  })
</script>
</@layoutFully>
