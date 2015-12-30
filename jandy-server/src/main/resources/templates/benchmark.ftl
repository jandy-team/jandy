<#include "include/layouts.ftl">
<@layoutFully>
<section class="container-fluid">
  <h2>Summary</h2>
  <p>
    Max Total Duration: ${prof.maxTotalDuration/1000000000}s
    Message: ${(build.commit.message)!"Merge pull-request"}
    Commit ID: ${(build.commit.id)!"asdasd1sd2"}
  </p>
</section>
<section class="container" style="padding-top: 10px;">
  <div id="canvas"></div>
</section>
<div class="container-fluid">
  <h2>Information</h2>
  <div id="menus">

  </div>
</div>
<script src="${root}/js/benchmark.js"></script>
<script id="tpl-menu" type="text/template">
<div id="info-<%= id %>" class="row">
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
    <div class="col-md-2"><span class="pull-right">Duration(s): </span></div>
    <div class="col-md-10"><%= duration %></div>
  </div>
  <hr>
</div>
</script>
<script>
  $(function () {
    var Benchmark = greem.Benchmark;
    new Benchmark({
      menus: _.template($("#tpl-menu").text())
    }, $("#menus")).start("${prof.id}");
  })
</script>
</@layoutFully>
