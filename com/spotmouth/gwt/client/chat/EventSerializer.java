package com.spotmouth.gwt.client.chat;


import org.atmosphere.gwt.client.AtmosphereGWTSerializer;
import org.atmosphere.gwt.client.SerialTypes;
import com.spotmouth.gwt.client.dto.Event;


@SerialTypes(value = {Event.class})
public abstract class EventSerializer extends AtmosphereGWTSerializer {
}
