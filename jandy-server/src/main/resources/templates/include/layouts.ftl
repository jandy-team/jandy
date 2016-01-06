<#assign root=rc.getContextPath()>
<#macro layout>
  <!DOCTYPE HTML>
  <html>
  <head>
    <title>Jandy</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="${root}/css/style.css">
    <link rel="stylesheet" href="${root}/webjars/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="${root}/css/bootstrap-theme.css">
    <link rel="stylesheet" href="${root}/webjars/bootstrap-switch/3.3.2/css/bootstrap3/bootstrap-switch.min.css">
    <link rel="stylesheet" href="${root}/webjars/font-awesome/4.5.0/css/font-awesome.min.css">
  </head>
  <body data-spy="scroll">
    <#nested/>
  </body>
  </html>
</#macro>
<#macro layoutFully>
  <@layout>
    <script>
      var ROOT_URL = '${root}';
    </script>
    <script src="${root}/webjars/jquery/2.1.4/jquery.min.js"></script>

    <script src="${root}/js/jandy.js"></script>
    <nav class="navbar navbar-fixed-top navbar-default">
      <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="${root}/">Jandy</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="navbar-nav nav">
            <li><a href="${root}/repos">Repositories</a></li>
          </ul>
          <ul class="navbar-nav nav navbar-right">
            <#if user??>
              <li class="dropdown">
                <a href="#" style="padding-top: 0;" class="navbar-brand" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  <span style="display: inline-block; margin-top: 15px;">${user.login}</span>
                  <#--<div style="display:inline-block; background-image: url('${user.avatarUrl}'); background-size: cover; width: 30px; height: 30px; padding-top: -10px; border-radius: 5px;">&nbsp;</div>-->
                  <img src="${user.avatarUrl}" style="display: inline-block; background-size: cover; border-radius: 5px;" width="40px" height="40px">
                  <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                  <li><a href="${root}/profile">Profile</a></li>
                  <li><a href="${root}/signout">Sign out</a></li>
                </ul>
              </li>
            <#else>
              <li>
                <form action="${root}/signin/github" method="POST">
                  <button type="submit" class="navbar-btn btn btn-default">Sign in with GitHub</button>
                  <input type="hidden" name="scope" value="${github_scopes}" />
                </form>
              </li>
            </#if>
          </ul>
        </div><!-- /.navbar-collapse -->
      </div><!-- /.container-fluid -->
    </nav>
    <#nested/>
    <script src="${root}/webjars/d3js/3.5.5/d3.min.js"></script>
    <script src="${root}/webjars/lodash/3.10.1/lodash.min.js"></script>
    <script src="${root}/webjars/json2/20140204/json2.min.js"></script>
    <script src="${root}/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="${root}/webjars/bootstrap-switch/3.3.2/js/bootstrap-switch.min.js"></script>
    <script src="${root}/js/bootstrap-waitingfor.js"></script>
    <script src="${root}/webjars/raphaeljs/2.1.4/raphael-min.js"></script>
  </@layout>
</#macro>