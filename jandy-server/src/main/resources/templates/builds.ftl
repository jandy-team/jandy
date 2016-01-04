<#include "include/layouts.ftl">
<@layoutFully>
<div class="container">
  <h1>${project.account}/${project.name}
    <a href="https://github.com/${project.account}/${project.name}" style="color: inherit;"><i class="fa fa-github"></i></a>
  </h1>
  <a href="#">
    <img src="${root}/repos/${project.account}/${project.name}/${branch.name}.svg">
  </a>
</div>
<br/>
<section id="repos" class="container">
  <#--<div class="row">-->
    <#--<ul class="nav nav-tabs">-->
      <#--<li role="presentation" class="active"><a href="${root}/repos/${project.account}/${project.name}">Builds</a></li>-->
      <#--<li role="presentation"><a href="#">Benchmark</a></li>-->
    <#--</ul>-->
  <#--</div>-->
  <div class="row">
    <h1>Builds</h1>
  </div>
  <#foreach build in builds>
    <#assign elapsedDuration = build.profContextDump.elapsedDuration!0>
    <#assign color = (elapsedDuration <= 0)?then("green", "red")>
    <div class="row">
      <div class="panel panel-default" style="border-left: 10px ${color} solid;">
        <div class="panel-heading clearfix">
          <div class="panel-title">
            <h4>
              <span style="padding-right: 5px;"><a href="https://travis-ci.org/${project.account}/${project.name}/builds/${build.travisBuildId}">#${build.number}</a></span>
              <span style="padding-left: 5px;" class="glyphicon glyphicon-arrow-${(elapsedDuration <= 0)?then("down", "up")}">${elapsedDuration/1000000}ms</span>
              <span class="btn-group pull-right">
                <a href="https://github.com/${project.account}/${project.name}/commit/${(build.commit.sha)!"25a362115243352598617072f435c606658f14f1"}"
                   class="btn btn-primary btn-sm" role="button">${(build.commit.sha?substring(0, 7))!"c90978e"}</a>
                <a href="${root}/builds/${build.id}" class="btn btn-primary btn-sm" role="button">More</a>
              </span>
            </h4>
          </div>
        </div>
        <div class="panel-body">
          <p>
            Max Total Duration: ${(build.profContextDump.maxTotalDuration/10000000000)!"9999"}s<br>
            Commit Message: ${(build.commit.message)!"test"}
          </p>
        </div>
      </div>
    </div>
  </#foreach>
</section>
</@layoutFully>