/*******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.spiffyui.hellospiffylocalization.client;

import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.MainFooter;
import org.spiffyui.client.MainHeader;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTility;
import org.spiffyui.client.widgets.LongMessage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;


/**
 * This class is the main entry point for our GWT module.
 */
public class Index implements EntryPoint, ClickHandler, KeyPressHandler 
{
    private static final Strings STRINGS = (Strings) GWT.create(Strings.class);
    private static final ProjStrings PROJ_STRINGS = (ProjStrings) GWT.create(ProjStrings.class);

    private static Index g_index;
    private TextBox m_text = new TextBox();
    private LongMessage m_longMessage = new LongMessage("longMsgPanel");

    /**
     * The Index page constructor
     */
    public Index()
    {
        g_index = this;
    }


    @Override
    public void onModuleLoad()
    {
        /*
         We need to set the title from a localized string
         */
        Window.setTitle(PROJ_STRINGS.hello());
        
        /*
         This is where we load our module and create our dynamic controls.  The MainHeader
         displays our title bar at the top of our page.
         */
        MainHeader header = new MainHeader();
        header.setHeaderTitle(PROJ_STRINGS.hello());
        
        /*
         The main footer shows our message at the bottom of the page.
         */
        MainFooter footer = new MainFooter();
        footer.setFooterString(PROJ_STRINGS.footer());
        
        /*
         This HTMLPanel holds most of our content.
         MainPanel_html was built in the HTMLProps task from MainPanel.html, which allows you to use large passages of html
         without having to string escape them.
         */
        HTMLPanel panel = new HTMLPanel(STRINGS.MainPanel_html())
        {
            @Override
            public void onLoad()
            {
                super.onLoad();
                /*
                 Let's set focus into the text field when the page first loads
                 */
                m_text.setFocus(true);
            }
        };
        
        RootPanel.get("mainContent").add(panel);
        
        /*
         These dynamic controls add interactivity to our page.
         */
        panel.add(m_longMessage, "longMsg");
        panel.add(m_text, "nameField");
        final Button button = new Button(PROJ_STRINGS.submit());
        panel.add(button, "submitButton");
        
        button.addClickHandler(this);
        m_text.addKeyPressHandler(this);
        
    }
    
    @Override
    public void onClick(ClickEvent event)
    {
        sendRequest();
    }

    @Override
    public void onKeyPress(KeyPressEvent event)
    {
        /*
         We want to submit the request if the user pressed enter
         */
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            sendRequest();
        }
    }
    
    /**
     * Send the REST request to the server and read the response back.
     */
    private void sendRequest()
    {
        String q = m_text.getValue().trim();
        if (q.equals("")) {
            MessageUtil.showWarning(PROJ_STRINGS.nameError(), false);
            return;
        }
        RESTility.callREST("simple/" + q, new RESTCallback() {
            
            @Override
            public void onSuccess(JSONValue val)
            {
                showSuccessMessage(val);
            }
            
            @Override
            public void onError(int statusCode, String errorResponse)
            {
                MessageUtil.showError(PROJ_STRINGS.error(statusCode, errorResponse));
            }
            
            @Override
            public void onError(RESTException e)
            {
                MessageUtil.showError(e.getReason());
            }
        });
        
    }
    
    /**
     * Show the successful message result of our REST call.
     * 
     * @param val    the value containing the JSON response from the server
     */
    private void showSuccessMessage(JSONValue val)
    {
        JSONObject obj = val.isObject();
        String name = JSONUtil.getStringValue(obj, "user");
        String browser = JSONUtil.getStringValue(obj, "userAgent");
        String server = JSONUtil.getStringValue(obj, "serverInfo");
        
        String message = PROJ_STRINGS.message(name, browser, server);
        m_longMessage.setHTML(message);
    }
}
