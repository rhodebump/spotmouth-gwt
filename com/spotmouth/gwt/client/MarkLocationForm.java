package com.spotmouth.gwt.client;

import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.dto.Location;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 8/13/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarkLocationForm extends LeaveMarkForm  {







    public MarkLocationForm() {}

    public MarkLocationForm(MyWebApp mywebapp, Location location, boolean locationOnly, ItemHolder itemHolder) {
        super(mywebapp,location,locationOnly,itemHolder);

    }

}
