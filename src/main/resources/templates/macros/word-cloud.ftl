<#-- @ftlvariable name="langPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="page" type="java.lang.String" -->

<#macro render wordCloud page>
  <#if wordCloud?? && wordCloud?has_content>
    <div class="word-cloud">
      <#list wordCloud as word>
        <a href="${langPrefix}/${page}/${word.slug}" style="font-size: ${word.fontSize};"
           class="word-cloud__word">${word.name}</a>
      </#list>
    </div>
  </#if>
</#macro>
