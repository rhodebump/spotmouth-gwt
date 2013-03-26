package com.spotmouth.gwt.client.coupon;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ImageAnchor;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SpotHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 9/13/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Coupons8Panel extends SpotBasePanel implements SpotMouthPanel {



    public String getPageTitle() {
        return getTitle();
    }




    public String getTitle() {
        return "Coupons";
    }


    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.couponMobile();
        } else {
            return MyWebApp.resources.coupon();
        }

    }

    DateTimeFormat fmt = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG);



    public Coupons8Panel() {

    }

    protected Map<Widget, ItemHolder> couponClickMap = new HashMap<Widget, ItemHolder>();
    ClickHandler clickCouponHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget b = (Widget) sender;
                ItemHolder coupon = couponClickMap.get(b);
                com.google.gwt.user.client.Window.open(coupon.getDetailsUrl(), "_blank", "");
            }
        }
    };

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    public  Coupons8Panel(MyWebApp mywebapp,MobileResponse couponResponse) {
        super(mywebapp, false);

        if (MyWebApp.isDesktop()) {
            ULPanel ulPanel = new ULPanel();
            ulPanel.getElement().setId("coupons_results");
            /*
            	<li class="coupons_result spa">
		<span class="coupons_ident"></span>
		<a class="coupons_title">@Antoinette Boudoir Spa. 11 W 46th Street 4th Floor, New York, NY, 10036</a>
		<p>$99 colonic hydrotherapy, foot detox and infared bed massage treatment at antoinette boudoir spa (a $210 value)</p>
		<h4>Valid until 2012 November 25</h4>
	</li>
             */

            if (couponResponse.getCoupons().isEmpty()) {
                Label mylabel = new Label("Sorry, no coupons found.");
                mylabel.setStyleName("h1");
                ulPanel.add(mylabel);
            }

            for (ItemHolder coupon : couponResponse.getCoupons()) {
                ListItem listItem = new ListItem();
                listItem.setStyleName("coupons_result");
                InlineLabel coupons_ident = new InlineLabel();
                coupons_ident.setStyleName("coupons_ident");
                listItem.add(coupons_ident);
                Anchor coupons_title = new Anchor();
                coupons_title.setStyleName("coupons_title");
                coupons_title.addClickHandler(clickCouponHandler);
                couponClickMap.put(coupons_title, coupon);
                //coupons_title.setHref(coupon.getDetailsUrl());
                //coupons_title.setTarget("_blank");
                SpotHolder couponSpotHolder = coupon.getSpotHolder();
                coupons_title.setText("@" + couponSpotHolder.getLabel2());

                listItem.add(coupons_title);
                //InlineLabel inlineLabel = new Inli;neLabel(coupon.getTitle());
                HTML title = new HTML("<p>" + coupon.getTitle() + "</p>");

                title.addClickHandler(clickCouponHandler);
                couponClickMap.put(title, coupon);

                listItem.add(title);

                String endDate = "";
                 if (coupon.getEndDate() != null) {
                     endDate =  fmt.format(coupon.getEndDate());
                     HTML validUntil = new HTML("<h4>" + "Valid until " + endDate + "</h4>");
                     listItem.add(validUntil);
                 }

                ulPanel.add(listItem);
            }

            Coupon8Composite cc = new Coupon8Composite(ulPanel);
            add(cc);
            return;


        }
    }

}
