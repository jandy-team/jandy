<#include "include/layouts.ftl">
<@layoutFully>
  <link rel="stylesheet" href="${root}/css/profile.css">
  <div id="profile" class="container-fluid">
    <div class="row">
      <aside id="left-menu" class="col-md-3 affix" role="navigation">
        <div class="panel panel-default">
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
              <a href="#repos-${org['login']?lower_case}" class="list-group-item" style="border-left: 5px solid ${colors[org['login']?lower_case]};">
                <div class="row">
                  <span class="col-md-9">${org['login']}</span>
                  <span class="col-md-3" style="text-align: center; border-left: 1px #dddddd solid;">${org['publicRepos']}</span>
                </div>
              </a>
            </#foreach>
          </ul>
        </div>
      </aside>
      <section class="col-md-9 col-md-offset-3" role="main">
        <#foreach ownerName in repositories?keys>
          <div class="row">
            <div id="repos-${ownerName?lower_case}" class="panel panel-default" style="border-top: 5px ${colors[ownerName?lower_case]} solid;">
              <div class="panel-heading" style="background-color: #fff; border-bottom: none;">
                <h2 class="panel-title" style="font-size: 30px; padding-left: 15px;">${ownerName}</h2>
              </div>
              <div class="panel-body">
                <article class="container-fluid">
                  <#list repositories[ownerName] as repo>
                    <div class="row" style="vertical-align: middle; height: 30px;" data-full-name="${repo.owner.login}/${repo.name}"
                         data-github-id="${repo.id}">
                      <div class="col-md-12">
                        <input type="checkbox" role="bootstrap-switch" data-size="small"
                               data-on-color="success" ${imported?seq_contains(repo.id)?then("checked", "")}>
                        <span data-toggle="tooltip" data-placement="right" title="${repo.description!""}" style="font-size: 16px;">${repo.name}</span>
                      </div>
                    </div>
                  </#list>
                </article>
              </div>
            </div>
          </div>
        </#foreach>
      </section>
    </div>
  </div>
<script>

  $(function () {
    $("[role='bootstrap-switch']").bootstrapSwitch().on('switchChange.bootstrapSwitch', function (event, state) {
      var $this = $(this);

      if (state == true) {
        var fullName = $this.parents('.row[data-full-name]').data('full-name');
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
        var githubId = $this.parents('.row[data-github-id]').data('github-id');

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
    $("[data-toggle='tooltip']").tooltip()
  })
</script>
</@layoutFully>