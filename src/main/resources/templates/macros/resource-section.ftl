<#-- @ftlvariable name="icon" type="java.lang.String" -->
<#-- @ftlvariable name="items" type="java.util.List<pl.miloszgilga.jargonaut.page.project.model.Link>" -->
<#-- @ftlvariable name="projectName" type="java.lang.String" -->

<#macro render label icon items projectName>
  <#if items?? && items?has_content>
    <div class="project-resources__group">
      <span class="project-resources__label">${label}</span>
      <ul class="project-resources__list">
        <#list items as item>
          <li>
            <a href="${item.url}" class="project-resources__link">
              <span class="icon icon-${icon}"></span>
              ${item.name!projectName}
            </a>
          </li>
        </#list>
      </ul>
    </div>
  </#if>
</#macro>
