package com.github.searls.jasmine.driver;

import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * The default web driver - overridden to tweak a few things.
 */
public class QuietHtmlUnitDriver extends HtmlUnitDriver {

  private final boolean debug;

  public QuietHtmlUnitDriver(Capabilities capabilities, boolean debug) {
    super(capabilities);
    this.debug = debug;
    this.setJavascriptEnabled(true);
  }

  @Override
  protected WebClient modifyWebClient(WebClient client) {
    client.setAjaxController(new NicelyResynchronizingAjaxController());

    //Disables stuff like this "com.gargoylesoftware.htmlunit.IncorrectnessListenerImpl notify WARNING: Obsolete content type encountered: 'text/javascript'."
    if (!this.debug) {
      client.setIncorrectnessListener(new IncorrectnessListener() {
        @Override
        public void notify(String message, Object origin) {
        }
      });
    }
    return client;
  }
}
