package com.spotmouth.gwt.client.chat;

import com.spotmouth.gwt.client.dto.Event;
import org.atmosphere.gwt.client.AtmosphereGWTSerializer;
import org.atmosphere.gwt.client.SerialTypes;


@SerialTypes(value = {Event.class})
public abstract class EventSerializer extends AtmosphereGWTSerializer {
}
