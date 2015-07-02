<#assign root=rc.getContextPath()>
<#macro layout>
  <!DOCTYPE HTML>
  <html>
  <head>
    <title>Jandy</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="${root}/css/style.css"/>
    <link rel="stylesheet" href="${root}/webjars/bootstrap/3.3.5/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${root}/webjars/bootstrap-switch/3.3.2/css/bootstrap3/bootstrap-switch.min.css"/>
  </head>
  <body data-spy="scroll">
    <script src="${root}/webjars/jquery/2.1.4/jquery.min.js"></script>
    <#nested/>
    <script src="${root}/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="${root}/webjars/bootstrap-switch/3.3.2/js/bootstrap-switch.min.js"></script>
    <script src="${root}/js/bootstrap-waitingfor.js"></script>
  </body>
  </html>
</#macro>
<#macro layoutFully>
  <@layout>
    <nav class="navbar navbar-default">
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
          <ul class="navbar-nav nav navbar-right">
            <#if user??>
              <li class="dropdown">
                <a href="${root}/profile" class="navbar-brand" style="background-image: url('${user.avatarUrl}'); background-size: cover; width: 50px;" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></a>
                <ul class="dropdown-menu">
                  <li><a href="#">Profile</a></li>
                  <li><a href="${root}/j_spring_security_logout">Sign out</a></li>
                </ul>
              </li>
            <#else>
              <li><button onclick="javascript:window.location.href='${root}/login'" class="navbar-btn btn btn-default">Sign in with GitHub</button></li>
            </#if>
          </ul>
        </div><!-- /.navbar-collapse -->
      </div><!-- /.container-fluid -->
    </nav>
    <#nested/>
  </@layout>
</#macro>