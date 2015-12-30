<#include "include/layouts.ftl">
<@layoutFully>
<section id="repos" class="container">
  <br/>
  <#foreach build in builds>
    <div class="row">
      <div class="panel panel-success">
        <div class="panel-heading clearfix">
          <h4 class="panel-title pull-left" style="padding-top: 7.5px;">Build#${build.number} - ${(build.commit.message)!"test"}</h4>
          <div class="btn-group pull-right">
            <a href="#" class="btn btn-primary btn-sm" role="button">c90968e</a>
            <a href="${root}/builds/${build.id}" class="btn btn-primary btn-sm" role="button">More</a>
          </div>
        </div>
        <div class="panel-body">
          <div>Max Total Duration: ${(build.profContextDump.maxTotalDuration/10000000000)!"9999"}s</div>
        </div>
      </div>
    </div>
  </#foreach>
</section>
</@layoutFully>