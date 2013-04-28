package com.spotmouth.gwt.client.login;
//http://google-web-toolkit.googlecode.com/svn/javadoc/2.4/com/google/gwt/user/client/ui/SimpleCheckBox.html
import com.allen_sauer.gwt.log.client.Log;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.gwtfb.client.overlay.AuthResponse;
import com.phonegap.gwt.fbconnect.client.FBConnect;
import com.phonegap.gwt.fbconnect.client.OnConnectCallback;
import com.spotmouth.gwt.client.DataOperationDialog;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.Fieldset;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.LoginRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
//import com.gwtfb.client.Callback;
//import com.gwtfb.sdk.FBEvent;

/*
* Facebook login is a little tricky
* We need to invoke a childbrowser that will login to facebook and return to us the accesstoken
*
* I send the accesstoken to spotmouth that will then go to facebook and get the userid
*/
public class LoginForm extends SpotBasePanel implements SpotMouthPanel {
    private String historyToken = null;



    private SimpleCheckBox showTypingCheckbox = new SimpleCheckBox();
    ;
    private SimpleCheckBox rememberMeCheckbox = new SimpleCheckBox();

    public String getTitle() {
        return "Login";
    }

    private TextField usernameTextBox = new TextField();
    //clear text password
    private TextField passwordTextBox = new TextField();
    //masking text password
    private PasswordTextBox maskedPasswordTextBox = new PasswordTextBox();
    private Fieldset clearTextFieldset = null;
    private Fieldset maskedTextFieldset = null;
    private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";
    // This app's personal client ID assigned by the Facebook Developer App
    // (http://www.facebook.com/developers).
    private static final String FACEBOOK_CLIENT_ID = MyWebApp.FACEBOOK_APPID;
    // All available scopes are listed here:
    // http://developers.facebook.com/docs/authentication/permissions/
    // This scope allows the app to access the user's email address.
    private static final String FACEBOOK_EMAIL_SCOPE = "email";
    //publish_stream,read_stream
    // This scope allows the app to access the user's birthday.
    //private static final String FACEBOOK_BIRTHDAY_SCOPE = "user_birthday";
    // Adds a button to the page that asks for authentication from Facebook.
    // Note that Facebook does not allow localhost as a redirect URL, so while
    // this code will work when hosted, it will not work when testing locally.

    public LoginForm() {
    }
    //http://code.google.com/p/gwtfb/source/browse/trunk/GwtFB/src/com/gwtfb/client/GwtFB.java?r=13
    /*
   <div id="login">
       <input type="radio" checked="checked" id="spl-login" name="switcher" value="simple">
       <input type="radio" name="switcher" id="soc-login" value="social">
       <span class="switch first">Login</span>
       <span class="switch last">Social</span>
       <form>
           <input type="email" required placeholder="E-mail...">
           <input type="password" required placeholder="Password...">
           <span class="check-log">
           <label><input type="checkbox"><span>remember me</span></label>
           <label><input type="checkbox"><span>show typing</span></label>
           </span><br>
           <span class="sub-log">
               <input type="button" value="Sign Up">
               <input type="submit" value="Log in">
           </span>
       </form>
       <div id="soc-tab">
           <a href="#" id="twit"></a>
           <a href="#" id="fb"></a>
           <a href="#" id="gp"></a>
       </div>
   </div>

    */

    public LoginForm(MyWebApp mywebapp) {
        super(mywebapp, false, false, true);
        DOM.setElementAttribute(maskedPasswordTextBox.getElement(), "placeholder", "Password...");
        addRequired(maskedPasswordTextBox);
        Button signUp = new Button();
        signUp.addClickHandler(registerHandler);
        Button loginButton = new Button();
        loginButton.setStyleName("loginButton");
        loginButton.addClickHandler(loginHandler);
        showTypingCheckbox.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (showTypingCheckbox.getValue()) {
                    passwordTextBox.setVisible(true);
                    maskedPasswordTextBox.setVisible(false);
                } else {
                    passwordTextBox.setVisible(false);
                    maskedPasswordTextBox.setVisible(true);
                }
            }
        });
        passwordTextBox.setVisible(false);
        Anchor facebookAnchor = new Anchor();
        DOM.setElementAttribute(facebookAnchor.getElement(), "id", "fb");
        if (FBConnect.isSupported()) {
            mywebapp.log("FBConnect is  supported, adding facebookloginHandlerMobile");
            facebookAnchor.addClickHandler(facebookloginHandlerMobile);
        } else {
            mywebapp.log("FBConnect is not supported, using  facebookloginHandlerWeb");
            facebookAnchor.addClickHandler(doFacebookLoginClickHandler);
        }
        Anchor resetAnchor = new Anchor("reset");
        DOM.setElementAttribute(resetAnchor.getElement(), "id", "reset");
        resetAnchor.addClickHandler(resetPasswordHandler);
        Anchor googleAnchor = new Anchor();
        DOM.setElementAttribute(googleAnchor.getElement(), "id", "gp");
        googleAnchor.addClickHandler(goggleSignin);
        Anchor twitterAnchor = new Anchor();
        DOM.setElementAttribute(twitterAnchor.getElement(), "id", "twit");
        twitterAnchor.addClickHandler(twitterHandler);
        passwordTextBox.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(com.google.gwt.event.dom.client.FocusEvent focusEvent) {
                if (showTypingCheckbox.getValue()) {
                    //no need to do anything
                } else {
                    maskedPasswordTextBox.setVisible(true);
                    passwordTextBox.setVisible(false);
                    maskedPasswordTextBox.setFocus(true);
                }
            }
        });
        maskedPasswordTextBox.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (maskedPasswordTextBox.getValue().isEmpty()) {
                    maskedPasswordTextBox.setVisible(false);
                    passwordTextBox.setVisible(true);
                }
            }
        });
        passwordTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performLogin();
                }
            }
        });
        maskedPasswordTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performLogin();
                }
            }
        });
        makeNoMessing(usernameTextBox);
        makeNoMessing(passwordTextBox);
        //be default, maskedPasswordTextBox is going to be the default, but we make passwordTextBox become invisible when it gets focus
        maskedPasswordTextBox.setVisible(false);
        passwordTextBox.setVisible(true);
        Login login = new Login(usernameTextBox, passwordTextBox, maskedPasswordTextBox, signUp, loginButton, showTypingCheckbox, rememberMeCheckbox, facebookAnchor, resetAnchor, googleAnchor, twitterAnchor);
        add(login);
    }

    ClickHandler twitterHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            ask();
        }
    };
    //http://code.google.com/p/gwt-examples/source/browse/trunk/DemoGWTGDataOAuth/src/org/gonevertical/demo/client/layout/AskForAccessButton.java

    private void ask() {
        //hNote.setHTML("Close New OAuth Window To Move Forward");
        //String url = GWT.getHostPageBaseURL() + "askforaccess?do=ask&scope=" + scope;
        String url = GWT.getHostPageBaseURL() + "twitter/signin";
        open(url);
    }

    /**
     * open window, then start a process to watch for it to close
     *
     * @param url - oauth approval url
     */
    private void open(String url) {
        openWindow(url);
        loop();
    }

    /**
     * open new window
     *
     * @param url - oauth approval url
     * @return
     */
    private final native void openWindow(String url) /*-{
        $wnd.winHandle = $wnd.open(url, "_blank", null);
    }-*/;

    /**
     * is popup window still open
     *
     * @return - boolean
     */
    private final native boolean isWindowClosed() /*-{
        var b = false;
        // TODO check for undefined window
        if ($wnd.winHandle.closed == true) {
            b = true;
        }
        return b;
    }-*/;

    /**
     * loop over and over watching for window to close
     * TODO - add a recursive boundary, like only check 1000 times
     */
    private void loop() {
        Timer t = new Timer() {
            public void run() {
                if (isWindowClosed() == true) {
                    setWindowClosed();
                    return;
                }
                loop();
            }
        };
        t.schedule(100);
    }

    public void setWindowClosed() {
        //hNote.setHTML(SafeHtmlUtils.fromTrustedString("Checking."));
        performTwitterLogin();
    }

    private void performTwitterLogin() {
        getMessagePanel().clear();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        String accessToken = "not Used";
        myService.doTwitterLogin(accessToken, new AsyncCallback() {
            DataOperationDialog validateLoginDialog = new DataOperationDialog(
                    "Validating login");

            public void onFailure(Throwable caught) {
                validateLoginDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                validateLoginDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.setLoginMobileResponse(mobileResponse);
                    mywebapp.setTwitterUser(true);
                    mywebapp.setLoginForm(LoginForm.this);
                    if (LoginForm.this.callback != null) {
                        callback.onSuccess(null);
                    } else {
                        mywebapp.setApplicationMenuPanel(null);
                        mywebapp.toggleMenu();
                        mywebapp.getMessagePanel().displayMessage("Logged in via Twitter");
                    }
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void doGoogleAuth() {
        String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
        String CLIENT_ID = "524125573205.apps.googleusercontent.com"; // available from the APIs console
        String READONLY_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
        String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
        String GPLUS = "https://www.googleapis.com/auth/plus.me";
        /*
       Client ID:
       524125573205.apps.googleusercontent.com
       Email address:
       524125573205@developer.gserviceaccount.com
       Client secret:
       bQocrv_mVw5uPBWZh0TFnsBn
       Redirect URIs: 	https://www.spotmouth.com/oauth2callback
       JavaScript origins: 	https://www.spotmouth.com
        */
        AuthRequest req = new AuthRequest(AUTH_URL, CLIENT_ID)
                .withScopes(READONLY_SCOPE, EMAIL_SCOPE, GPLUS); // Can specify multiple scopes here
        Auth.get().login(req, new Callback<String, Throwable>() {
            @Override
            public void onSuccess(String token) {
                // You now have the OAuth2 token needed to sign authenticated requests.
                performGoogleLogin(token);
            }

            @Override
            public void onFailure(Throwable caught) {
                // The authentication process failed for some reason, see caught.getMessage()
                getMessagePanel().displayError("Error:\n" + caught.getMessage());
            }
        });
    }
//http://code.google.com/p/gwt-oauth2/
    ClickHandler loginHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("adding login button");
            //add(new HTML("<fb:login-button autologoutlink='true' perms='publish_stream,read_stream' /> "));
            performLogin();
        }
    };
    ClickHandler registerHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleRegister(true);
        }
    };
    /*
    the gwt-oauth2 project doesn't do signoff well at all, we
    are using gwtFB project to integrate to facebook
     */
    AsyncCallback facebookLoggedInStatusCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            mywebapp.log("facebookLoggedInStatusCallback.onSuccess");
//            java.util.Collection<String> cookies = Cookies.getCookieNames();
//            java.util.Iterator<String> vals = cookies.iterator();
//            String accessToken = "";
//            while (vals.hasNext()) {
//                String val = vals.next();
//                mywebapp.log("val=" + val);
//                if (val.startsWith("fbs_")) {
//                    accessToken = Cookies.getCookie(val);
//                }
//            }
//            //we need to strip accessToken
//            //access_token
//            mywebapp.log("accessToken1=" + accessToken);
//            String[] args = accessToken.split("&");
//            mywebapp.log("accessToken2=" + accessToken);
//            mywebapp.log("args=" + args);
//            if (args.length > 0) {
//                accessToken = args[0];
//            }
//            mywebapp.log("accessToken2=" + accessToken);
//            //http://developers.facebook.com/docs/authentication/
//            accessToken = accessToken.replaceAll("access_token=", "");
//            mywebapp.log("accessToken3=" + accessToken);
//            //wrapped with quotes
//            accessToken = accessToken.replaceAll("\"", "");
//            mywebapp.log("accessToken=" + accessToken);
            AuthResponse authResponse = mywebapp.getFbCore().getAuthResponse();
            //.authresponse status=undefined
            String accessToken = authResponse.getAccessToken();
            Log.warn("facebookLoggedInStatusCallback.accessToken=" + accessToken);
            Log.warn("facebookLoggedInStatusCallback.authresponse expires=" + authResponse.getExpiresIn());
            Log.warn("facebookLoggedInStatusCallback.authresponse status=" + authResponse.getStatus());
            Log.warn("acebookLoggedInStatusCallback.authresponse2=" + mywebapp.getFbCore().getStatus());
            //facebookLoggedInStatusCallback.authresponse status=undefined
            performFacebookLogin(accessToken);
        }
    };

    AsyncCallback facebookLoginStatusCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            mywebapp.log("facebookLoginStatusCallback.onSuccess");
            //JavaScriptObject.
            Log.warn("facebookLoginStatusCallback.onSuccess " + response.toString());
            if (mywebapp.getFbCore().getAuthResponse() == null) {
                mywebapp.log("getFbCore.getAuthResponse is null");
                mywebapp.getFbCore().login(facebookLoggedInStatusCallback);
            } else {
                mywebapp.log("getFbCore.getAuthResponse is not null");
                AuthResponse authResponse = mywebapp.getFbCore().getAuthResponse();
                Log.warn("authresponse expires=" + authResponse.getExpiresIn());
                Log.warn("mywebapp.getFbCore()getstatus=" + mywebapp.getFbCore().getStatus());
                facebookLoggedInStatusCallback.onSuccess(null);
            }
        }
    };
    ClickHandler doFacebookLoginClickHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //we can't do a login if we are already logged in
            mywebapp.getFbCore().getLoginStatus(facebookLoginStatusCallback,true);
        }
    };
    ClickHandler facebookloginHandlerMobile = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.fbConnect.setOnConnectCallback(new OnConnectCallback() {
                public void onFBConnected() {
                    String accessToken = mywebapp.fbConnect.getAccessToken();
                    performFacebookLogin(accessToken);
                }
            });
            String redir_url = "http://www.facebook.com/connect/login_success.html";
            mywebapp.fbConnect.connect(MyWebApp.FACEBOOK_APPID, redir_url, "touch", "publish_stream");
        }
    };

    //this will register the facebook token with spotmouth serverside
    private void performFacebookLogin(String accessToken) {
        getMessagePanel().clear();
        GWT.log("performFacebookLogin " + accessToken);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.dofacebooklogin(accessToken, new AsyncCallback() {
            DataOperationDialog validateLoginDialog = new DataOperationDialog(
                    "Validating login");

            public void onFailure(Throwable caught) {
                validateLoginDialog.hide();
                getMessagePanel().displayError("Facebook login error");

            }

            public void onSuccess(Object result) {
                validateLoginDialog.hide();
                Log.warn("performFacebookLogin.onSuccess");
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.setLoginMobileResponse(mobileResponse);
                    mywebapp.setFacebookUser(true);
                    mywebapp.setLoginForm(LoginForm.this);
                    if (LoginForm.this.callback != null) {
                        callback.onSuccess(null);
                    } else {
                        mywebapp.setApplicationMenuPanel(null);
                        mywebapp.toggleMenu();
                        mywebapp.getMessagePanel().displayMessage("Logged in via Facebook");
                    }
                } else {
                    //mywebapp.getFbCore().logout(facebookLoginStatusCallback2);
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void performGoogleLogin(String accessToken) {
        getMessagePanel().clear();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.doGoogleLogin(accessToken, new AsyncCallback() {
            DataOperationDialog validateLoginDialog = new DataOperationDialog(
                    "Validating login");

            public void onFailure(Throwable caught) {
                validateLoginDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                validateLoginDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.setLoginMobileResponse(mobileResponse);
                    mywebapp.setFacebookUser(true);
                    mywebapp.setLoginForm(LoginForm.this);
                    if (LoginForm.this.callback != null) {
                        callback.onSuccess(null);
                    } else {
                        mywebapp.setApplicationMenuPanel(null);
                        mywebapp.toggleMenu();
                        mywebapp.getMessagePanel().displayMessage("Logged in via Google");
                    }
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    //
//    AsyncCallback loginMessageCallback = new AsyncCallback() {
//        public void onFailure(Throwable throwable) {
//            getMessagePanel().displayError(throwable.getMessage());
//        }
//
//        public void onSuccess(Object response) {
//            getMessagePanel().displayMessage("Logged in");
//        }
//    };
    ClickHandler resetPasswordHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleResetPasswordRequest(true);
        }
    };
    ClickHandler goggleSignin = new ClickHandler() {
        public void onClick(ClickEvent event) {
            doGoogleAuth();
        }
    };

    public void performLogin() {
        mywebapp.getMessagePanel().clear();
        if (usernameTextBox.getValue().isEmpty()) {
            mywebapp.getMessagePanel().displayMessage("Username is required.");
            return;
        }
        if (showTypingCheckbox.getValue()) {
            if (passwordTextBox.getValue().isEmpty()) {
                mywebapp.getMessagePanel().displayMessage("Password is required.");
                return;
            }
        } else {
            if (maskedPasswordTextBox.getValue().isEmpty()) {
                mywebapp.getMessagePanel().displayMessage("Password is required.");
                return;
            }
        }
        //user wants to save the login and this is a successful login, let's save it
        if (mywebapp.isLocalStorageSupported()) {
            if (rememberMeCheckbox.getValue()) {
                mywebapp.pushLocalStorage("username", usernameTextBox.getValue());
                mywebapp.pushLocalStorage("password", passwordTextBox.getValue());
            } else {
                mywebapp.pushLocalStorage("username", "");
                mywebapp.pushLocalStorage("password", "");
            }
        }
        //let's clear out old messages.  How can we do this uniformly everywhere??
        getMessagePanel().clear();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(usernameTextBox.getValue());
        if (showTypingCheckbox.getValue()) {
            loginRequest.setPassword(passwordTextBox.getValue());
        } else {
            loginRequest.setPassword(maskedPasswordTextBox.getValue());
        }
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.dologin(loginRequest, new AsyncCallback() {
            DataOperationDialog validateLoginDialog = new DataOperationDialog(
                    "Validating login");

            public void onFailure(Throwable caught) {
                validateLoginDialog.hide();
                getMessagePanel().displayError("Login Failure:" + caught.getMessage());
            }

            public void onSuccess(Object result) {
                validateLoginDialog.hide();
                MobileResponse loginResponse = (MobileResponse) result;
                if (loginResponse.getStatus() == 1) {
                    //make sure we set the authenticated user before we call the callbacks next
                    mywebapp.setLoginForm(LoginForm.this);
                    mywebapp.setLoginMobileResponse(loginResponse);
                    getMessagePanel().displayMessage("Login was successful");
                    if (callback != null) {
                        GWT.log("loginCallback not null");
                        callback.onSuccess(null);
                    } else {
                        //we do not
                        GWT.log("loginCallback is null");
                        //need to go to login m
                        mywebapp.setApplicationMenuPanel(null);
                        //the following may take use to the home page, etc. whatever the url of the page is
                        //would like to indicate user is logged in, but how can we do this?
                        if (historyToken != null) {
                            History.newItem(historyToken);
                            History.fireCurrentHistoryState();
                        } else {
                            mywebapp.toggleMenu();
                        }
                        mywebapp.getMessagePanel().displayMessage("Logged in");
                    }
                } else {
                    //getMessagePanel().displayError("Login error");
                    GWT.log("login status was zero");
                    mywebapp.getMessagePanel().displayErrors(loginResponse.getErrorMessages());
                }
            }
        });
    }

    public void toggleFirst() {
        //usernameTextBox.setFocus(true);
    }
}
