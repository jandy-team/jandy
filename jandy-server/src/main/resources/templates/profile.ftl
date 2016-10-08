<#include "include/layouts.ftl">
<@layoutFully>
<script src="${root}/js/ProfileElementModifier.js"></script>

<link rel="stylesheet" href="${root}/css/profile.css">
<div id="profile" class="container-fluid">
  <div class="row">
    <aside id="left-menu" class="col-md-3 affix" role="navigation">
      <div class="panel panel-default visible-md visible-lg">
        <div class="panel-heading" style="background-color: #fff">
          <h3 class="panel-title" style="color: rgb(139, 139, 139); font-size: 20px;">Organizations</h3>
        </div>
        <ul id="org-list" class="list-group">
          <a href="#repos-${user.login}" class="list-group-item" style="border-left: 5px solid ${colors[user.login?lower_case]};">
            <div class="row">
              <span class="col-md-9">${user.login}</span>
              <span class="col-md-3" style="text-align: center; border-left: 1px #dddddd solid;">${user.publicRepos}</span>
            </div>
          </a>
            <#foreach org in organizations>
              <a href="#repos-${org.login?lower_case}" class="list-group-item" style="border-left: 5px solid ${colors[org['login']?lower_case]};">
                <div class="row">
                  <span class="col-md-9">${org.login}</span>
                  <span class="col-md-3" style="text-align: center; border-left: 1px #dddddd solid;">${org.publicRepos}</span>
                </div>
              </a>
            </#foreach>
        </ul>
      </div>
    </aside>
    <section class="col-md-9 col-md-offset-3" role="main">

      <ul class="nav nav-tabs">
          <#foreach ownerName in repositories?keys>
            <li><a ownerName=${ownerName} data-color="${colors[ownerName?lower_case]}" href="#" class="tablinks">${ownerName}</a></li>
          </#foreach>
      </ul>

      <div class="row visible-md visible-lg">
        <div id="repos" class="panel panel-default" style="border-top: 5px solid;">
          <div class="panel-body">
            <article id="repos-row" class="container-fluid">
            </article>
          </div>
        </div>
      </div>

    </section>
  </div>
</div>
<script>

  $(function () {

    var importedProjectsIds = [];
    var loadedProjectCount = 0;

    // TODO: change bootstrap to semantic-ui.com
    $(".nav-tabs li a").on("click", function() {

      var owner = $(this).attr("ownerName");
      var color = $(this).data("color");

      $(".panel-body").html('');

      requestProjects(owner, color);

    });

    $(window).scroll(function() {

      if($(window).scrollTop() + $(window).height() == $(document).height()) {

        loadNextPageProjects(importedProjectsIds);
      }

    });

    // Request projects
    function requestProjects(owner, color) {

      var windowHeight = $(window).height();
      var bodyHeight = $("body").height();

      $.when(
          // Load already imported project from DB
          $.ajax({
            url: "${root}/profile/projects/imported/" + owner,
            type: "GET"
          }),
          // Get projects from github
          $.ajax({
            url: "${root}/profile/projects/" + owner,
            type: "GET"
          })
      ).done(function(importedProjectsResponse, projectsResponse) {

            var importedProjects = importedProjectsResponse[0];
            var projects = projectsResponse[0];

            $(".panel-body").append(makeProjectDivs(importedProjects));

            importedProjectsIds = [];
            for(var index in importedProjects) {
              importedProjectsIds.push(importedProjects[index].github_id);
            }

            onSwitch(importedProjectsIds);

            $("#repos").css({
              'border-top-width': '5px',
              'border-top-color': color,
              'border-top-style': 'solid'
            });

            $(".panel-body").append(makeProjectDivs(projects, importedProjectsIds));

            activateBootstrapSwitch();

            windowHeight = $(window).height();
            bodyHeight = $("body").height();

            loadedProjectCount = $(".repo_row").length;

            var ownerProjectCount = getOwnerProjectCount(owner);

            if((bodyHeight <= windowHeight) && (loadedProjectCount < ownerProjectCount)) {
              loadNextPageProjects(importedProjectsIds);
            }

          });

    }

    function getOwnerProjectCount(owner) {

      var projectCount = 0;
      var owners = $(".list-group-item").find("div");

      owners.each(function(number, element) {

        if($(element).find(".col-md-9").text() === owner) {
          projectCount = $(element).find(".col-md-3").text();
        }

      });

      return projectCount;
    }

    // Request projects after current loaded projects
    function loadNextPageProjects(importedProjectsIds) {

      var owner = $(".repo_row").attr("data-full-name").split("/", 1)[0];
      var currentPageProjectCount = $("div[class='repo_row']").length;

      var windowHeight = $(window).height();
      var bodyHeight = $("body").height();

      $.ajax({
        url: "${root}/profile/projects/loadNext/" + owner + "/" + currentPageProjectCount,
        type: "GET"
      }).done(function (projects) {

        $(".panel-body").append(makeProjectDivs(projects, importedProjectsIds));
        activateBootstrapSwitch();

        windowHeight = $(window).height();
        bodyHeight = $("body").height();

        loadedProjectCount = $(".repo_row").length;

        var ownerProjectCount = getOwnerProjectCount(owner);

        if((bodyHeight <= windowHeight) && (loadedProjectCount < ownerProjectCount)) {
          loadNextPageProjects(importedProjectsIds);
        }

      });
    }

    function makeProjectDivs(projects, importedProjectsIds) {

      var projectDivs = [];

      for(var index in projects) {

        if($.inArray(projects[index].github_id, importedProjectsIds) === -1) {

          projectDivs.push(
              $("<div></div>")
                  .attr('class', 'repo_row')
                  .attr('style', 'vertical-align: middle; height: 30px;')
                  .attr('data-full-name', projects[index].full_name)
                  .attr('data-github-id', projects[index].github_id)
                  .append(
                  $("<div/>", {
                    'class': 'col-md-12',
                    'html': [

                      $("<input/>", {
                        'type': 'checkbox',
                        'role': 'bootstrap-switch',
                        'data-size': 'small',
                        'data-on-color': 'success',
                        'id': 'bootstrap-switch'
                      }),
                      $("<span/>", {
                        'data-toggle': 'tooltip',
                        'data-placement': 'right',
                        'title': projects[index].description,
                        'style': 'font-size: 16px;'
                      }).text(projects[index].name)
                    ]
                  })
              )
          );
        }
      }
      return projectDivs;
    }

    // change bootstrap-switch state to on
    function onSwitch(importedProjects) {

      importedProjects.forEach(function(importedProject, index) {
        $('[data-github-id=' + importedProject + ']').find('[role="bootstrap-switch"]').bootstrapSwitch('state', true, true);
      });

    }

    // change all checkbox to bootstrap-switch.
    function activateBootstrapSwitch() {
      $("[role='bootstrap-switch']").bootstrapSwitch().on('switchChange.bootstrapSwitch', function (event, state) {
        var $this = $(this);

        if (state == true) {
          var fullName = $this.parents('.repo_row[data-full-name]').data('full-name');

          waitingDialog.show('Importing...');
          $.ajax({
            url: "${root}/profile/project",
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
              fullName: fullName
            })
          }).fail(function () {
            $this.bootstrapSwitch('state', false, true);
          }).always(function () {
            waitingDialog.hide();
          })
        } else {
          var githubId = $this.parents('.repo_row[data-github-id]').data('github-id');

          waitingDialog.show('Disabling...');
          $.ajax({
            url: "${root}/profile/project/" + githubId,
            type: "DELETE"
          }).fail(function () {
            $this.bootstrapSwitch('state', true, true);
          }).always(function () {
            waitingDialog.hide();
          })
        }
      });

      $("[data-toggle='tooltip']").tooltip();
    }

  });

</script>
</@layoutFully>