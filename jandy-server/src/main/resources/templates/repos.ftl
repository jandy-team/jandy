<#include "include/layouts.ftl">
<@layoutFully>
<link href="${root}/css/repos.css" type="text/css" rel="stylesheet">
<script src="${root}/js/repos.js"></script>
<section id="repos" class="container" style="text-align: center;">
  <div class="row">
    <svg id="builds"></svg>
  </div>
  <div class="row">
    <svg id="methods"></svg>
  </div>
</section>
<script>
  $(function () {

    var branchId = ${master.id};

    var graph = new ReposGraph({
      width: 960,
      height: 500,
      margin: {
        top: 20,
        right: 20,
        bottom: 30,
        left: 40
      },
      el: {
        builds: '#builds',
        methods: '#methods'
      }
    });

    graph.createBuilds(branchId);
  });
</script>
<script>
  $(function () {

  });
</script>
</@layoutFully>