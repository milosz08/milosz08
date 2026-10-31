<#macro renderTocItems items>
  <#if items?has_content>
    <ul class="toc__list toc__list--nested">
      <#list items as item>
        <li class="toc__item">
          <a class="toc__link" href="#${item.id}">${item.text}</a>
          <@renderTocItems item.children />
        </li>
      </#list>
    </ul>
  </#if>
</#macro>
