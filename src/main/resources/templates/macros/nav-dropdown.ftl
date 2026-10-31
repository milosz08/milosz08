<#-- @ftlvariable name="node" type="pl.miloszgilga.jargonaut.core.template.generator.dto.SitemapNodeView" -->

<#macro render node>
  <li class="dropdown__item">
    <#if node.hasChildren>
      <input id="${node.id}" class="dropdown__checkbox" type="checkbox" <#if node.expanded>checked</#if>>
      <label for="${node.id}" class="dropdown__sub-trigger">
        <#if node.current>
          <span class="dropdown__current">${node.label}</span>
        <#else>
          <a href="${node.url}" class="dropdown__link">${node.label}</a>
        </#if>
      </label>
      <ul class="dropdown__submenu">
        <#list node.children as child>
          <@render node=child />
        </#list>
      </ul>
    <#else>
      <#if node.current>
        <span class="dropdown__current">${node.label}</span>
      <#else>
        <a href="${node.url}" class="dropdown__link">${node.label}</a>
      </#if>
    </#if>
  </li>
</#macro>
