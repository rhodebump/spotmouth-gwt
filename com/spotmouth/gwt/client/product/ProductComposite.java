package com.spotmouth.gwt.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/15/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ProductComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  //<b class="mp_ava _wiki"></b>
    //productSpan

    @UiField
    SpanElement productSpan;

    public void setProductClass(String x) {
        productSpan.setClassName("mp_ava");
        productSpan.addClassName(x);
    }


    @UiField
    SpanElement productDescription;

    public void setProductDescription(String desc) {
        productDescription.setInnerText(desc);
    }

    @UiField
        SpanElement spotName;

    public void setSpotName(String name) {
        spotName.setInnerText(name);
    }


    @UiField(provided = true)
    final TextField hostNameTextBox;

    @UiField(provided = true)
    final TextField domainNameTextBox;

    @UiField(provided = true)
    final Button activateButton;

    @UiField(provided = true)
    final SimpleCheckBox acceptTerms;



    public ProductComposite(TextField hostNameTextBox,TextField domainNameTextBox,Button activateButton,SimpleCheckBox acceptTerms ) {

        this.hostNameTextBox = hostNameTextBox;
        this.domainNameTextBox = domainNameTextBox;
        this.activateButton = activateButton;
        this.acceptTerms = acceptTerms;
        initWidget(uiBinder.createAndBindUi(this));

    }


}
