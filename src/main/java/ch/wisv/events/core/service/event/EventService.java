package ch.wisv.events.core.service.event;

import ch.wisv.events.core.model.event.Event;
import ch.wisv.events.api.request.EventOptionsRequest;
import ch.wisv.events.api.request.EventProductRequest;
import ch.wisv.events.api.request.EventRequest;

import java.util.List;

/**
 * EventService.
 */
public interface EventService {

    /**
     * Get all Events
     *
     * @return Collection of Events
     */
    List<Event> getAllEvents();

    /**
     * Get all upcoming Events
     *
     * @return Collection of Events
     */
    List<Event> getUpcomingEvents();

    /**
     * Get all available events
     *
     * @return Collection of Events
     */
    List<Event> getAvailableEvents();

    /**
     * Get Event by key
     *
     * @param key key of an Event
     * @return Event
     */
    Event getByKey(String key);

    /**
     * Add a new Event by a EventRequest
     *
     * @param eventRequest EventRequest
     */
    Event add(EventRequest eventRequest);

    /**
     * Add a product to an Event
     *
     * @param eventProductRequest EventProductRequest
     */
    void addProductToEvent(EventProductRequest eventProductRequest);

    /**
     * Delete a product from an Event
     *
     * @param eventId   eventId
     * @param productId productId
     */
    void deleteProductFromEvent(Long eventId, Long productId);

    /**
     * Update an Event by an EventRequest
     *
     * @param eventRequest EventRequest
     */
    void update(EventRequest eventRequest);

    /**
     * Delete an Event
     *
     * @param event Event
     */
    void delete(Event event);

    /**
     * Update the EventOptions of an Event
     *
     * @param request EventOptionsRequest
     */
    void updateEventOptions(EventOptionsRequest request);

    /**
     * Get all Events that are connected to the same Product
     *
     * @param key key of an Product
     * @return List of Events
     */
    List<Event> getEventByProductKey(String key);

}
