<#-- @ftlvariable name="technologies" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->

<#macro render technologies>
  <#if technologies?has_content>
    <h2>${i18n.project.technologies}</h2>
    <ul>
      <#list technologies as technology>
        <li>${technology}</li>
      </#list>
    </ul>
  </#if>
</#macro>
