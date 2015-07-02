<#include "include/layouts.ftl">
<@layoutFully>
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-3" role="navigation">
        <div class="panel panel-success">
          <div class="panel-heading">
            <h3 class="panel-title">Organizations</h3>
          </div>
          <ul class="list-group">
            <li class="list-group-item">
              <span class="badge">${user.publicRepos}</span>
              <a href="#repos-${user.login}">${user.login}</a>
            </li>
            <#foreach org in organizations>
              <li class="list-group-item">
                <span class="badge">${org['publicRepos']}</span>
                <a href="#repos-${org['login']?lower_case}">${org['login']}</a>
              </li>
            </#foreach>
          </ul>
        </div>
      </div>
      <section class="col-md-9" role="main">
        <#foreach ownerName in repositories?keys>
          <div class="row">
            <div id="repos-${ownerName}" class="panel panel-default">
              <div class="panel-heading">
                <h2 class="panel-title">${ownerName}</h2>
              </div>
              <div class="panel-body">
                <div class="container-fluid">
                  <#list repositories[ownerName] as repo>
                    <div class="row" style="vertical-align: middle; height: 30px;" data-full-name="${repo.owner.login}/${repo.name}" data-github-id="${repo.id?c}">
                      <div class="col-md-1">
                        <input type="checkbox" role="bootstrap-switch" data-size="mini" <#if repo.imported>checked</#if>>
                      </div>
                      <div class="col-md-11" title="${repo.description}">${repo.name}</div>
                    </div>
                  </#list>
                </div>
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
          url: "${root}/profile/project/" + fullName,
          type: "PUT"
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
    $("[title]").tooltip()
  })
</script>
</@layoutFully>