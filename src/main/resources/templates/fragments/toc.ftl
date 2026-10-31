<#-- @ftlvariable name="toc" type="java.util.List<pl.miloszgilga.jargonaut.postprocessor.toc.TocItem>" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->

<#import "../macros/toc.ftl" as tocMacro>

<#if toc?has_content>
  <nav class="toc">
    <ul class="toc__list">
      <li class="toc__item">
        <a class="toc__link" href="#">${i18n.toc.top}</a>
      </li>
      <#list toc as item>
        <li class="toc__item">
          <a class="toc__link" href="#${item.id}">${item.text}</a>
          <@tocMacro.renderTocItems item.children />
        </li>
      </#list>
    </ul>
  </nav>
</#if>
