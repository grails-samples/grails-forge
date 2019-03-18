<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Grails Application Forge</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
    <asset:javascript src="applicationforge.js" />
    <asset:stylesheet src="applicationforge.css" />
    <script type="application/javascript" src='https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/1.7.1/clipboard.min.js'></script>
</head>
<body>
 <article class="post" style="margin-top: 50px;">
        <div id="loading"><g:message code="appgenerator.loading" default="Loading..."/></div>
         <g:form controller="generator" action="generate">
            <div class="twocolumns">
                <div class="column">
                    <ol>
                        <li>
                            <label for="profile"><g:message code="grails.profile.features" default="Pick your Features"/>:</label>
                            <ol id="featureList">
                                <g:each var="feature" in="${projectOptions.features.sort { a, b -> a <=> b }}">
                                    <li>
                                        <g:if test="${projectOptions.requiredSelectedFeatures.contains(feature)}">
                                            <g:checkBox onclick="onFeatureChange()" disabled="true" checked="true" name="features" value="${feature}" /><span>${feature}</span>
                                        </g:if>
                                        <g:elseif test="${projectOptions.defaultSelectedFeatures.contains(feature)}">
                                            <g:checkBox onclick="onFeatureChange()" checked="true" name="features" value="${feature}" /><span>${feature}</span>
                                        </g:elseif>
                                        <g:else>
                                            <g:checkBox onclick="onFeatureChange()" name="features" value="${feature}" checked="false"/><span>${feature}</span>
                                        </g:else>
                                    </li>
                                </g:each>
                            </ol>
                        </li>
                    </ol>
                </div>
                <div class="column">
                    <ol>
                        <li>
                            <label for="projectType"><g:message code="project.type" default="Project Type"/>:</label>
                            <g:select name="projectType" onChange="onProjectTypeChange();" id="projectType" from="${projectOptions.projectTypes}" value="${projectOptions.selectedProjectType}"/>
                        </li>
                        <li>
                            <label for="name"><g:message code="project.name" default="Name your application"/>:</label>
                            <g:textField onkeypress="onNameChanged();" onpaste="onNameChanged();" oninput="onNameChanged();" onchange="onNameChanged();" name="name" id="name" value="${projectOptions.name}"/>
                        </li>
                        <li>
                            <label for="version"><g:message code="grails.project.version" default="Version"/>:</label>
                            <g:select name="version" onChange="onVersionChange();" id="version" from="${projectOptions.versions}" value="${projectOptions.selectedVersion}"/>
                        </li>
                        <li>
                            <label for="profile"><g:message code="grails.profile" default="Profile"/>:</label>
                            <g:select name="profile" onChange="onSelectChange();" id="profile" from="${projectOptions.profiles}" value="${projectOptions.selectedProfile}"/>
                        </li>
                    </ol>
                </div><!-- /.column -->
            </div><!-- /.twocolumns -->
            <div class="align-center">
                <input type="submit" id="btn-generate" value="${g.message(code: 'app.generate', default: 'Generate')}" class="btn-large btn" /></div>
        </g:form>
        <div class="align-center">
            <span style="margin-bottom: 30px;display: block;">OR</span>
            <b>Execute the following command</b>
            <button type="button" style="background-color: transparent;border: 1px solid #f4f4f4;" class="clippy align-right" type="button" data-clipboard-target="#curlCommand">
                <asset:image src="clippy.svg" alt="Copy to clipboard" height="20"/>
            </button>
            <pre><code id="curlCommand">${curlCommand}</code></pre>

            <script>var clipboard = new Clipboard('.clippy');</script>
        </div>




<h2>API</h2>
<p>The Grails Application Forge includes an API which may be invoked using any http client, like curl for example.</p>

<h3>Create An Application Using The Latest Release</h3>
<pre>
<code>curl -O start.grails.org/myProject.zip</code>
</pre>

<h3>Create A Plugin Using The Latest Release</h3>
<pre>
<code>curl -O start.grails.org/myPlugin.zip -d type=plugin</code>
</pre>
<h3>Specify A Grails Version</h3>
<pre>
<code>curl -O start.grails.org/myProject.zip -d version=3.2.5</code>
</pre>

<h3>Specify A Grails Profile</h3>
<pre>
<code>curl -O start.grails.org/restPproject.zip -d profile=rest-api</code>
</pre>

<h3>Help</h3>
<p>A list of all available options will be returned if a request is sent to the application without any arguments.</p>
<pre><code>
curl start.grails.org

...
</code></pre>

     <div class="align-center">
         <span style="margin-top: 30px; margin-bottom: 30px;display: block;">OR</span>
     </div><!-- /.align-center -->

         <h3>IntelliJ IDEA</h3>

         <p><a href="https://www.jetbrains.com/help/idea/grails.html">IntelliJ IDEA has excellent support for Grails development</a>. You can
         access the Grails App Forge functionality directly from the <i>File &rarr; New &rarr; Project</i> wizard.</p>

        <a href="${assetPath(src: 'appforgeintellij.png')}"><asset:image src="appforgeintellijthumb.png" alt="Grails Application Forge in IntelliJ"/></a>



 </article>
</body>
</html>
