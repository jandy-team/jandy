<#include "include/layouts.ftl">
<#include "include/project-list.ftl">
<@layoutFully>
<br>
<div class="container-fluid">
  <div class="row">
    <nav class="col-md-3 hidden-xs hidden-sm" role="navigation">
      <@drawProjectList projects project.id/>
    </nav>
    <div class="col-xs-offset-1 col-md-7 col-xs-10">
      <div class="row">
        <h1>${project.account}/${project.name}
          <a href="https://github.com/${project.account}/${project.name}" style="color: inherit;"><i
              class="fa fa-github"></i></a>
        </h1>
        <a href="#" data-toggle="modal" data-target="#badge-modal">
          <img src="${root}/repos/${project.account}/${project.name}/${branch.name}.svg">
        </a>
      </div>
      <br/>
      <section id="repos">
        <div class="row">
          <h1>Builds</h1>
        </div>
        <div class="row">
          <div id="timeline" style="border: 1px solid #ddd;"></div>
        </div>
        <br>
        <#foreach build in builds>
          <#assign elapsedDuration = build.profContextDump.elapsedDuration!0>
          <#assign color = (elapsedDuration <= 0)?then("green", "red")>
          <#assign after = (elapsedDuration <= 0)?then(" faster than", " slower than")>
          <div class="row">
            <div class="panel panel-default" style="border-left: 10px ${color} solid;">
              <div class="panel-heading clearfix">
                <div class="panel-title">
                  <h4>
                <span style="padding-right: 5px;"><a
                    href="https://travis-ci.org/${project.account}/${project.name}/builds/${build.travisBuildId}">#${build.number}</a></span>
                    <span style="padding-left: 5px; color: ${color};">${(elapsedDuration/1000000)?abs}ms ${after} before</span>
                    <span class="btn-group pull-right">
                      <a href="https://github.com/${project.account}/${project.name}/commit/${(build.commit.sha)!"25a362115243352598617072f435c606658f14f1"}"
                         class="btn btn-primary btn-sm" role="button">${(build.commit.sha?substring(0, 7))!"c90978e"}</a>
                      <a href="${root}/builds/${build.id}" class="btn btn-primary btn-sm" role="button">More</a>
                    </span>
                    <img class="pull-right" src="${user.avatarUrl}"
                         style="margin-right: 5px; display: inline-block; background-size: cover; border-radius: 5px;"
                         width="30px" height="30px">
                  </h4>
                  <div>
              <span style="font-size: 12px; color: gray;">
                about ${(build.buildAt)!"1 hour ago"}
              </span>
                  </div>
                </div>
              </div>
              <div class="panel-body">
                <p>
                  Max Total Duration: ${(build.profContextDump.maxTotalDuration/1000000)!"9999"}ms<br>
                  Commit Message: ${(build.commit.message)!"test"}<br>
                </p>
              </div>
            </div>
          </div>
        </#foreach>
      </section>
    </div>
  </div>
</div>
<!-- Modal -->
<div class="modal fade" id="badge-modal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-body">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <br>
        <div class="row">
          <div class="col-md-3">
            <select class="form-control">
              <option value="img" selected>Image URL</option>
              <option value="markdown">Markdown</option>
            </select>
          </div>
          <div class="col-md-9 badge-val">
            <textarea name="img" class="form-control">http://greem.io/repos/${project.account}/${project.name}/${branch.name}.svg</textarea>
            <textarea name="markdown" class="form-control hidden">[![Build Status](http://greem.io/repos/${project.account}/${project.name}/${branch.name}.svg)](http://greem.io/repos/${project.account}/${project.name})</textarea>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${root}/js/builds.js"></script>
<script>
$(function () {
  var $modal = $("#badge-modal"),
      $select = $modal.find('select'),
      $badges = $modal.find('.badge-val');

  $select.on('change', function () {
    $badges.find('*[name="'+$(this).val()+'"]').removeClass('hidden');
    $badges.find('*[name!="'+$(this).val()+'"]').addClass('hidden');
  });

  new jandy.TimelineGraph()
      .start("${branch.id}");
});
</script>
</@layoutFully>