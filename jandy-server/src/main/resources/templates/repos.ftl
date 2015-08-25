<#include "include/layouts.ftl">
<@layoutFully>
<link href="${root}/css/repos.css" type="text/css" rel="stylesheet">
<script src="${root}/js/repos.js"></script>
<section id="repos" class="container" style="text-align: center;">
  <div class="row">
    <div class="col-md-2">
      <a href="#" data-toggle="modal" data-target="#modal-badge">
        <img src="${root}/repos/${project.account}/${project.name}/${branch.name}.svg">
      </a>
    </div>
    <div class="col-md-10">
      <div class="row">
        <svg id="builds"></svg>
      </div>
      <div class="row">
        <svg id="methods"></svg>
      </div>
    </div>
  </div>
</section>
<script>
  $(function () {

    var graph = new jandy.ReposGraph({
      width: 800,
      height: 600,
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

    graph.createBuilds(${branch.id});
  });
</script>
<div id="modal-badge" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-body" style="padding-bottom: 0;">
        <div class="container-fluid">
          <form class="form-horizontal">
            <div class="form-group">
              <label for="text-badge-image" class="col-md-2 control-label">
                <img src="${root}/repos/${project.account}/${project.name}/${branch.name}.svg">
              </label>
              <div class="col-md-10">
                <input id="text-badge-image" class="form-control" type="text" readonly value="${url}/repos/${project.account}/${project.name}/${branch.name}.svg">
              </div>
            </div>
          </form>
        </div>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</@layoutFully>