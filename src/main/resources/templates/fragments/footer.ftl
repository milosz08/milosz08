<#-- @ftlvariable name="i18n" type="java.util.Map" -->
<#-- @ftlvariable name="generator" type="pl.miloszgilga.jargonaut.config.model.Generator" -->
<#-- @ftlvariable name="lastUpdatedAt" type="java.lang.String" -->
<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->
<#-- @ftlvariable name="currentYear" type="java.lang.Integer" -->
<#-- @ftlvariable name="langPrefix" type="java.lang.String" -->

<footer class="footer">
  <div class="footer__container">
    <div class="footer__left">
      <div class="footer__item">
        &copy; ${currentYear?c} by <a href="${seo.domain}" class="footer__link">${seo.authorName}</a>
      </div>
      <div class="footer__item">
        <a href="${langPrefix}/${seo.rssFeedUrl}" class="footer__link">RSS</a>
        &bullet;
        <a href="${langPrefix}/privacy-policy">${i18n.privacyPolicy}</a>
      </div>
    </div>
    <div class="footer__right">
      <div class="footer__item">
        ${i18n.footer.generatedBy}: <a href="${generator.url}" class="footer__link">${generator.name}</a>
      </div>
      <div class="footer__item">
        ${i18n.footer.lastUpdatedAt} (UTC): <span class="footer__last-update">${lastUpdatedAt}</span>
      </div>
    </div>
  </div>
</footer>
